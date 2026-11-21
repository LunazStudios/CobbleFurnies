package com.lunazstudios.cobblefurnies.client.outline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom block outlines inspired by Highlight and ResourcefulLib (Team Resourceful).
 *
 * @see <a href="https://github.com/Team-Resourceful/Highlight">Highlight</a>
 * @see <a href="https://github.com/Team-Resourceful/ResourcefulLib/tree/1.21.1">ResourcefulLib</a>
 */
final class ArcadeOutlineGeometry {
    private static final double PRECISION = 10_000_000.0;
    private final List<List<Segment>> orientations;

    private ArcadeOutlineGeometry(Set<Edge> edges) {
        List<List<Segment>> rotated = new ArrayList<>(4);
        for (int turns = 0; turns < 4; turns++) {
            List<Segment> segments = new ArrayList<>(edges.size());
            for (Edge edge : edges) {
                Point start = edge.start().rotate(turns);
                Point end = edge.end().rotate(turns);
                double deltaX = end.x() - start.x();
                double deltaY = end.y() - start.y();
                double deltaZ = end.z() - start.z();
                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                segments.add(new Segment(start.x(), start.y(), start.z(), end.x(), end.y(), end.z(),
                        deltaX / length, deltaY / length, deltaZ / length));
            }
            rotated.add(List.copyOf(segments));
        }
        orientations = List.copyOf(rotated);
    }

    static ArcadeOutlineGeometry fromJson(JsonObject outline) {
        Set<Edge> edges = new LinkedHashSet<>();
        for (JsonElement entry : outline.getAsJsonArray("paths")) {
            JsonArray path = entry.getAsJsonArray();
            if (path.size() < 2) {
                throw new IllegalArgumentException("Arcade outline paths must contain at least two points");
            }
            Point start = point(path.get(0).getAsJsonArray());
            for (int index = 1; index < path.size(); index++) {
                Point end = point(path.get(index).getAsJsonArray());
                if (!start.equals(end)) {
                    edges.add(start.compareTo(end) <= 0 ? new Edge(start, end) : new Edge(end, start));
                }
                start = end;
            }
        }
        return new ArcadeOutlineGeometry(edges);
    }

    List<Segment> segments(int quarterTurns) {
        return orientations.get(Math.floorMod(quarterTurns, 4));
    }

    private static Point point(JsonArray values) {
        if (values.size() != 3) {
            throw new IllegalArgumentException("Arcade outline coordinates must contain three values");
        }
        double[] coordinates = new double[3];
        for (int index = 0; index < coordinates.length; index++) {
            coordinates[index] = values.get(index).getAsDouble();
            if (!Double.isFinite(coordinates[index])) {
                throw new IllegalArgumentException("Arcade outline coordinates must be finite");
            }
        }
        return new Point(coordinates[0] / 16.0, coordinates[1] / 16.0, coordinates[2] / 16.0);
    }

    record Segment(double startX, double startY, double startZ, double endX, double endY, double endZ,
                   double normalX, double normalY, double normalZ) {
    }

    private record Edge(Point start, Point end) {
    }

    private record Point(double x, double y, double z) implements Comparable<Point> {
        private Point {
            x = Math.rint(x * PRECISION) / PRECISION;
            y = Math.rint(y * PRECISION) / PRECISION;
            z = Math.rint(z * PRECISION) / PRECISION;
            x = x == 0.0 ? 0.0 : x;
            y = y == 0.0 ? 0.0 : y;
            z = z == 0.0 ? 0.0 : z;
        }

        private Point rotate(int turns) {
            return switch (turns) {
                case 1 -> new Point(1.0 - z, y, x);
                case 2 -> new Point(1.0 - x, y, 1.0 - z);
                case 3 -> new Point(z, y, 1.0 - x);
                default -> this;
            };
        }

        @Override
        public int compareTo(Point other) {
            int comparison = Double.compare(x, other.x);
            if (comparison == 0) {
                comparison = Double.compare(y, other.y);
            }
            return comparison == 0 ? Double.compare(z, other.z) : comparison;
        }
    }
}
