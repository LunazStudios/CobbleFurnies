import argparse
import copy
import json
import math
import re
from pathlib import Path


NAMESPACE = "cobblefurnies"
PART_NAMES = ("lever", "button_1", "button_2", "neon")
RESOURCE_ROOT = Path(__file__).resolve().parents[1] / "common/src/main/resources"
FACES = {"north", "east", "south", "west", "up", "down"}
ROTATION_ANGLES = {-45, -22.5, 0, 22.5, 45}


def coordinates(value, label, length=3):
    if not isinstance(value, list) or len(value) != length:
        raise ValueError(f"{label} must contain {length} coordinates")
    for coordinate in value:
        if isinstance(coordinate, bool) or not isinstance(coordinate, (int, float)):
            raise ValueError(f"{label} coordinates must be numbers")
        if not math.isfinite(coordinate):
            raise ValueError(f"{label} coordinates must be finite")
    return value


def validate_identifier(identifier):
    if not isinstance(identifier, str) or not re.fullmatch(r"[a-z0-9_.-]+:[a-z0-9_./-]+", identifier):
        raise ValueError(f"Invalid resource identifier: {identifier!r}")
    namespace, path = identifier.split(":", 1)
    if any(part in {"", ".", ".."} for part in path.split("/")):
        raise ValueError(f"Invalid resource path: {identifier!r}")
    return namespace, path


def resolve_texture(key, textures, visiting=None):
    visiting = set() if visiting is None else visiting
    if key in visiting or key not in textures:
        raise ValueError(f"Unresolved or cyclic texture reference: #{key}")
    visiting.add(key)
    value = textures[key]
    if not isinstance(value, str):
        raise ValueError(f"Texture {key!r} must be a string")
    if value.startswith("#"):
        return resolve_texture(value[1:], textures, visiting)
    validate_identifier(value)
    return value


def validate_model(model):
    if not isinstance(model, dict):
        raise ValueError("Arcade model must be a JSON object")
    elements = model.get("elements")
    textures = model.get("textures")
    if not isinstance(elements, list) or not elements:
        raise ValueError("Arcade model must contain an elements array")
    if not isinstance(textures, dict) or not textures:
        raise ValueError("Arcade model must contain a textures object")
    for key in textures:
        resolve_texture(key, textures)
    if "ambientocclusion" in model and not isinstance(model["ambientocclusion"], bool):
        raise ValueError("ambientocclusion must be a boolean")
    if "render_type" in model:
        validate_identifier(model["render_type"])
    for index, element in enumerate(elements):
        if not isinstance(element, dict):
            raise ValueError(f"Element {index} must be an object")
        start = coordinates(element.get("from"), f"Element {index} from")
        end = coordinates(element.get("to"), f"Element {index} to")
        if any(start[axis] > end[axis] for axis in range(3)):
            raise ValueError(f"Element {index} has reversed bounds")
        if any(value < -16 or value > 32 for value in start + end):
            raise ValueError(f"Element {index} exceeds the vanilla model coordinate range")
        rotation = element.get("rotation")
        if rotation is not None:
            if not isinstance(rotation, dict) or rotation.get("axis") not in {"x", "y", "z"}:
                raise ValueError(f"Element {index} has an invalid rotation axis")
            coordinates(rotation.get("origin"), f"Element {index} rotation origin")
            angle = rotation.get("angle")
            if isinstance(angle, bool) or not isinstance(angle, (int, float)) or angle not in ROTATION_ANGLES:
                raise ValueError(f"Element {index} has an unsupported rotation angle")
            if "rescale" in rotation and not isinstance(rotation["rescale"], bool):
                raise ValueError(f"Element {index} rescale must be a boolean")
        faces = element.get("faces")
        if not isinstance(faces, dict) or not faces or not set(faces).issubset(FACES):
            raise ValueError(f"Element {index} has invalid faces")
        for direction, face in faces.items():
            if not isinstance(face, dict) or not isinstance(face.get("texture"), str):
                raise ValueError(f"Element {index} {direction} face must reference a texture")
            texture = face["texture"]
            if texture.startswith("#"):
                resolve_texture(texture[1:], textures)
            else:
                validate_identifier(texture)
            if "uv" in face:
                coordinates(face["uv"], f"Element {index} {direction} UV", 4)


def extract_groups(model):
    groups = model.get("groups")
    if not isinstance(groups, list):
        raise ValueError("Arcade model must contain a groups array")
    members = {name: set() for name in PART_NAMES}
    found = set()
    referenced = set()

    def visit(node, owner=None):
        if isinstance(node, int) and not isinstance(node, bool):
            if node < 0 or node >= len(model["elements"]):
                raise ValueError(f"Group references invalid element index {node}")
            if node in referenced:
                raise ValueError(f"Element {node} is referenced by multiple groups")
            referenced.add(node)
            if owner is not None:
                members[owner].add(node)
            return
        if not isinstance(node, dict) or not isinstance(node.get("name"), str):
            raise ValueError("Group children must be element indices or named groups")
        children = node.get("children")
        if not isinstance(children, list):
            raise ValueError(f"Group {node['name']!r} must contain a children array")
        name = node["name"].casefold()
        if name in members:
            if name in found or owner is not None:
                raise ValueError(f"Arcade group {name!r} is duplicated or overlaps another animated group")
            if name == "lever":
                origin = coordinates(node.get("origin"), "Arcade lever group origin")
                expected = (11.5, 15.5, -0.5)
                if any(not math.isclose(actual, value, rel_tol=0, abs_tol=1e-6)
                       for actual, value in zip(origin, expected)):
                    raise ValueError("Arcade lever group origin must be [11.5, 15.5, -0.5] to match the shared renderer")
            found.add(name)
            owner = name
        for child in children:
            visit(child, owner)

    for group in groups:
        visit(group)
    for name in PART_NAMES:
        if not members[name]:
            raise ValueError(f"Arcade group {name!r} is missing or empty")
    return members


def transformed_center_y(element):
    center = [(start + end) / 2 for start, end in zip(element["from"], element["to"])]
    rotation = element.get("rotation")
    if rotation is None:
        return center[1]
    origin = rotation["origin"]
    x, y, z = [center[axis] - origin[axis] for axis in range(3)]
    angle = math.radians(rotation["angle"])
    sin = math.sin(angle)
    cos = math.cos(angle)
    axis = rotation["axis"]
    if axis == "x":
        y = y * cos - z * sin
    elif axis == "z":
        y = x * sin + y * cos
    if rotation.get("rescale", False) and axis != "y":
        y /= cos
    return y + origin[1]


def derived_element(element, offset=0, emissive=False):
    result = copy.deepcopy(element)
    result["from"][1] -= offset
    result["to"][1] -= offset
    if "rotation" in result:
        result["rotation"]["origin"][1] -= offset
    if any(value < -16 or value > 32 for value in result["from"] + result["to"]):
        raise ValueError("An arcade element cannot fit in its assigned block model")
    for face in result["faces"].values():
        face.pop("cullface", None)
    if emissive:
        result["shade"] = False
    return result


def derived_model(source, elements, emissive=False):
    result = {}
    for key in ("credit", "texture_size"):
        if key in source:
            result[key] = copy.deepcopy(source[key])
    result["ambientocclusion"] = False if emissive else source.get("ambientocclusion", True)
    result["render_type"] = source.get("render_type", "minecraft:cutout")
    result["textures"] = copy.deepcopy(source["textures"])
    result["elements"] = elements
    return result


def build_resources(variant, model):
    if not isinstance(variant, str) or not re.fullmatch(r"[a-z0-9_]+", variant):
        raise ValueError("Arcade variant names must use lowercase letters, numbers, and underscores")
    validate_model(model)
    groups = extract_groups(model)
    animated = set().union(*groups.values())
    parts = {"lower": [], "upper": []}
    for index, element in enumerate(model["elements"]):
        if index not in animated:
            half = "lower" if transformed_center_y(element) < 16 else "upper"
            parts[half].append(derived_element(element, 16 if half == "upper" else 0))
    for name, indices in groups.items():
        parts[name] = [derived_element(model["elements"][index], emissive=name == "neon")
                       for index in sorted(indices)]

    block_id = f"{variant}_arcade"
    assets = Path("assets") / NAMESPACE
    resources = {}
    for name, elements in parts.items():
        path = assets / "models/block/arcade" / f"{block_id}_{name}.json"
        resources[path] = derived_model(model, elements, emissive=name == "neon")
    resources[assets / "models/item" / f"{block_id}.json"] = {
        "parent": f"{NAMESPACE}:block/arcade/{variant}"
    }
    variants = {}
    for half in ("lower", "upper"):
        for facing, rotation in (("north", 0), ("east", 90), ("south", 180), ("west", 270)):
            value = {"model": f"{NAMESPACE}:block/arcade/{block_id}_{half}"}
            if rotation:
                value["y"] = rotation
            variants[f"facing={facing},half={half}"] = value
    resources[assets / "blockstates" / f"{block_id}.json"] = {"variants": variants}
    resources[Path("data") / NAMESPACE / "loot_table/blocks" / f"{block_id}.json"] = {
        "type": "minecraft:block",
        "pools": [{
            "rolls": 1,
            "entries": [{"type": "minecraft:item", "name": f"{NAMESPACE}:{block_id}"}],
            "conditions": [{"condition": "minecraft:survives_explosion"}]
        }]
    }
    return resources


def validate_local_textures(model, resource_root):
    for key in model["textures"]:
        namespace, path = validate_identifier(resolve_texture(key, model["textures"]))
        if namespace == NAMESPACE and not (resource_root / "assets" / namespace / "textures" / f"{path}.png").is_file():
            raise ValueError(f"Missing arcade texture: {namespace}:{path}")


def main():
    parser = argparse.ArgumentParser(description="Create block models and resources for selected arcade variants")
    parser.add_argument("variants", nargs="+")
    parser.add_argument("--check", action="store_true", help="Check derived resources without writing files")
    args = parser.parse_args()
    planned = {}
    try:
        for variant in args.variants:
            if not re.fullmatch(r"[a-z0-9_]+", variant):
                raise ValueError(f"Invalid arcade variant name: {variant!r}")
            source = RESOURCE_ROOT / "assets" / NAMESPACE / "models/block/arcade" / f"{variant}.json"
            with source.open(encoding="utf-8-sig") as reader:
                model = json.load(reader)
            resources = build_resources(variant, model)
            validate_local_textures(model, RESOURCE_ROOT)
            planned.update(resources)
        mismatches = []
        for relative_path, data in planned.items():
            path = RESOURCE_ROOT / relative_path
            if args.check:
                if not path.is_file() or json.loads(path.read_text(encoding="utf-8-sig")) != data:
                    mismatches.append(relative_path)
            else:
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_text(json.dumps(data, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")
        if mismatches:
            for path in mismatches:
                print(f"Outdated arcade resource: {path.as_posix()}")
            return 1
        action = "Checked" if args.check else "Created"
        print(f"{action} {len(planned)} arcade resources for {', '.join(args.variants)}")
        return 0
    except (OSError, ValueError, RecursionError) as exception:
        parser.error(str(exception))


if __name__ == "__main__":
    raise SystemExit(main())
