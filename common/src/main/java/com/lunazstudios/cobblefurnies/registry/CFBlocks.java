package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.block.*;
import com.lunazstudios.cobblefurnies.block.item.TooltipBlockItem;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class CFBlocks {

    public static class Properties {
        private static final Supplier<BlockBehaviour.Properties> GRASS = () -> Block.Properties.of().strength(0.6F).sound(SoundType.GRASS).mapColor(MapColor.GRASS);
        private static final Supplier<BlockBehaviour.Properties> WOOD = () -> woodProperties();
        private static final Supplier<BlockBehaviour.Properties> NETHER_WOOD = () -> Block.Properties.of().strength(2.0F, 3.0F).sound(SoundType.NETHER_WOOD).mapColor(MapColor.WOOD);
        private static final Supplier<BlockBehaviour.Properties> BAMBOO_WOOD = () -> woodProperties().sound(SoundType.BAMBOO_WOOD);
        private static final Supplier<BlockBehaviour.Properties> CHERRY_WOOD = () -> woodProperties().sound(SoundType.CHERRY_WOOD);
        private static final Supplier<BlockBehaviour.Properties> WOOL = () -> Block.Properties.of().strength(0.8F).sound(SoundType.WOOL).ignitedByLava().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.GUITAR);
        private static final Supplier<BlockBehaviour.Properties> MARBLE = () -> marbleProperties();
        private static final Supplier<BlockBehaviour.Properties> CONCRETE = () -> concreteProperties();
        private static final Supplier<BlockBehaviour.Properties> LAMP = () -> woodProperties()
                .lightLevel(state -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0);


        private static BlockBehaviour.Properties woodProperties() {
            return Block.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS);
        }

        private static BlockBehaviour.Properties concreteProperties() {
            return Block.Properties.of().strength(1.8F).sound(SoundType.STONE).mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM);
        }

        private static BlockBehaviour.Properties marbleProperties() {
            return Block.Properties.of().strength(1.5F, 6.0F).sound(SoundType.CALCITE).mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM);
        }
    }


    public static final Supplier<Block> LIGHT_TOILET = registerBlock("light_toilet", key -> new ToiletBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> DARK_TOILET = registerBlock("dark_toilet", key -> new ToiletBlock(Properties.MARBLE.get().setId(key)));

    public static final Supplier<Block> LIGHT_FREEZER = registerBlockOnly("light_freezer", key -> new FreezerBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> LIGHT_FRIDGE = registerBlock("light_fridge", key -> new FridgeBlock(Properties.CONCRETE.get().setId(key), CFBlocks.LIGHT_FREEZER.get()));
    public static final Supplier<Block> DARK_FREEZER = registerBlockOnly("dark_freezer", key -> new FreezerBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> DARK_FRIDGE = registerBlock("dark_fridge", key -> new FridgeBlock(Properties.CONCRETE.get().setId(key), CFBlocks.DARK_FREEZER.get()));

    public static final Supplier<Block> FURNI_CRAFTER = registerBlock("furnicrafter", key -> new FurniCrafterBlock(Properties.CONCRETE.get().setId(key)));

    public static final Supplier<Block> TV = registerBlock("tv", key -> new TVBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> CES = registerBlock("ces", key -> new CESBlock(Properties.CONCRETE.get().setId(key)));
    /*
    public static final Supplier<Block> PORYGON_ARCADE = registerBlock("porygon_arcade", () -> new ArcadeBlock(
            BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_PINK).noOcclusion()));
    public static final Supplier<Block> BULBASAUR_ARCADE = registerBlock("bulbasaur_arcade", () -> new ArcadeBlock(
            BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_GREEN).noOcclusion()));
    */

    public static final Supplier<Block> KITCHEN_FLOOR = registerBlock("kitchen_floor", key -> new Block(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> BAMBOO_FLOOR = registerBlock("bamboo_floor", key -> new Block(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_PLANKS = registerBlock("bamboo_planks", key -> new Block(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> AZURE_MOSAIC = registerBlock("azure_mosaic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BUTTER_MOSAIC = registerBlock("butter_mosaic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> VANILLA_MOSAIC = registerBlock("vanilla_mosaic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GOLDENROD_MOSAIC = registerBlock("goldenrod_mosaic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> ICED_PORCELAIN = registerBlock("iced_porcelain", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> MINTED_PORCELAIN = registerBlock("minted_porcelain", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> IRONLACE_CERAMIC = registerBlock("ironlace_ceramic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PALE_CERAMIC = registerBlock("pale_ceramic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> POOL_CERAMIC = registerBlock("pool_ceramic", key -> new Block(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LAB_FLOOR = registerBlock("lab_floor", key -> new Block(Properties.MARBLE.get().setId(key)));


    public static final Supplier<Block> BONSAI_PLANT = registerBlock("bonsai_plant", key -> new BonsaiPlantBlock(Properties.WOOD.get().setId(key).noOcclusion()));
    public static final Supplier<Block> MINI_TOPIARY = registerBlock("mini_topiary", key -> new MiniTopiaryBlock(Properties.WOOD.get().setId(key).noOcclusion()));
    public static final Supplier<Block> POTTED_POTHOS = registerBlock("potted_pothos", key -> new PottedPothosBlock(Properties.WOOD.get().setId(key).noOcclusion()));

    public static final Supplier<Block> RED_POKE_WOOL = registerBlock("red_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> RED_POKE_WOOL_CARPET = registerBlock("red_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> ORANGE_POKE_WOOL = registerBlock("orange_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> ORANGE_POKE_WOOL_CARPET = registerBlock("orange_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> YELLOW_POKE_WOOL = registerBlock("yellow_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> YELLOW_POKE_WOOL_CARPET = registerBlock("yellow_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BROWN_POKE_WOOL = registerBlock("brown_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BROWN_POKE_WOOL_CARPET = registerBlock("brown_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIME_POKE_WOOL = registerBlock("lime_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIME_POKE_WOOL_CARPET = registerBlock("lime_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> GREEN_POKE_WOOL = registerBlock("green_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> GREEN_POKE_WOOL_CARPET = registerBlock("green_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> CYAN_POKE_WOOL = registerBlock("cyan_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> CYAN_POKE_WOOL_CARPET = registerBlock("cyan_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_POKE_WOOL = registerBlock("light_blue_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_POKE_WOOL_CARPET = registerBlock("light_blue_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BLUE_POKE_WOOL = registerBlock("blue_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BLUE_POKE_WOOL_CARPET = registerBlock("blue_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> MAGENTA_POKE_WOOL = registerBlock("magenta_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> MAGENTA_POKE_WOOL_CARPET = registerBlock("magenta_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> PURPLE_POKE_WOOL = registerBlock("purple_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> PURPLE_POKE_WOOL_CARPET = registerBlock("purple_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> PINK_POKE_WOOL = registerBlock("pink_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> PINK_POKE_WOOL_CARPET = registerBlock("pink_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> WHITE_POKE_WOOL = registerBlock("white_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> WHITE_POKE_WOOL_CARPET = registerBlock("white_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_POKE_WOOL = registerBlock("light_gray_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_POKE_WOOL_CARPET = registerBlock("light_gray_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> GRAY_POKE_WOOL = registerBlock("gray_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> GRAY_POKE_WOOL_CARPET = registerBlock("gray_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BLACK_POKE_WOOL = registerBlock("black_poke_wool", key -> new Block(Properties.WOOL.get().setId(key)));
    public static final Supplier<Block> BLACK_POKE_WOOL_CARPET = registerBlock("black_poke_wool_carpet", key -> new CarpetBlock(Properties.WOOL.get().setId(key)));

    public static final Supplier<Block> RED_CURTAIN = registerBlock("red_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_CURTAIN = registerBlock("orange_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_CURTAIN = registerBlock("yellow_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_CURTAIN = registerBlock("brown_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_CURTAIN = registerBlock("lime_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_CURTAIN = registerBlock("green_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_CURTAIN = registerBlock("cyan_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_CURTAIN = registerBlock("light_blue_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_CURTAIN = registerBlock("blue_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_CURTAIN = registerBlock("magenta_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_CURTAIN = registerBlock("purple_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_CURTAIN = registerBlock("pink_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_CURTAIN = registerBlock("white_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_CURTAIN = registerBlock("light_gray_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_CURTAIN = registerBlock("gray_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_CURTAIN = registerBlock("black_curtain", key -> new CurtainBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> RED_LAMP = registerBlock("red_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> ORANGE_LAMP = registerBlock("orange_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> YELLOW_LAMP = registerBlock("yellow_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> BROWN_LAMP = registerBlock("brown_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> LIME_LAMP = registerBlock("lime_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> GREEN_LAMP = registerBlock("green_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> CYAN_LAMP = registerBlock("cyan_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_LAMP = registerBlock("light_blue_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> BLUE_LAMP = registerBlock("blue_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> MAGENTA_LAMP = registerBlock("magenta_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> PURPLE_LAMP = registerBlock("purple_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> PINK_LAMP = registerBlock("pink_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> WHITE_LAMP = registerBlock("white_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_LAMP = registerBlock("light_gray_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> GRAY_LAMP = registerBlock("gray_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));
    public static final Supplier<Block> BLACK_LAMP = registerBlock("black_lamp", key -> new LampBlock(Properties.LAMP.get().setId(key)));

    public static final Supplier<Block> RED_CHAIR = registerBlock("red_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> RED_DARK_CHAIR = registerBlock("red_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_CHAIR = registerBlock("orange_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_DARK_CHAIR = registerBlock("orange_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_CHAIR = registerBlock("yellow_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_DARK_CHAIR = registerBlock("yellow_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_CHAIR = registerBlock("brown_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_DARK_CHAIR = registerBlock("brown_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_CHAIR = registerBlock("lime_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_DARK_CHAIR = registerBlock("lime_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_CHAIR = registerBlock("green_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_DARK_CHAIR = registerBlock("green_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_CHAIR = registerBlock("cyan_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_DARK_CHAIR = registerBlock("cyan_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_CHAIR = registerBlock("light_blue_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_DARK_CHAIR = registerBlock("light_blue_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_CHAIR = registerBlock("blue_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_DARK_CHAIR = registerBlock("blue_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_CHAIR = registerBlock("magenta_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_DARK_CHAIR = registerBlock("magenta_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_CHAIR = registerBlock("purple_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_DARK_CHAIR = registerBlock("purple_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_CHAIR = registerBlock("pink_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_DARK_CHAIR = registerBlock("pink_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_CHAIR = registerBlock("white_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_DARK_CHAIR = registerBlock("white_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_CHAIR = registerBlock("light_gray_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_DARK_CHAIR = registerBlock("light_gray_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_CHAIR = registerBlock("gray_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_DARK_CHAIR = registerBlock("gray_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_CHAIR = registerBlock("black_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_DARK_CHAIR = registerBlock("black_dark_chair", key -> new ChairBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> OAK_CHAIR = registerBlock("oak_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> SPRUCE_CHAIR = registerBlock("spruce_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BIRCH_CHAIR = registerBlock("birch_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> DARK_OAK_CHAIR = registerBlock("dark_oak_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> JUNGLE_CHAIR = registerBlock("jungle_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ACACIA_CHAIR = registerBlock("acacia_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MANGROVE_CHAIR = registerBlock("mangrove_chair", key -> new WoodChairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_CHAIR = registerBlock("bamboo_chair", key -> new WoodChairBlock(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> CHERRY_CHAIR = registerBlock("cherry_chair", key -> new WoodChairBlock(Properties.CHERRY_WOOD.get().setId(key)));
    public static final Supplier<Block> CRIMSON_CHAIR = registerBlock("crimson_chair", key -> new WoodChairBlock(Properties.NETHER_WOOD.get().setId(key)));
    public static final Supplier<Block> WARPED_CHAIR = registerBlock("warped_chair", key -> new WoodChairBlock(Properties.NETHER_WOOD.get().setId(key)));

    public static final Supplier<Block> OAK_NIGHTSTAND = registerBlockWithTooltip("oak_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> SPRUCE_NIGHTSTAND = registerBlockWithTooltip("spruce_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BIRCH_NIGHTSTAND = registerBlockWithTooltip("birch_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> DARK_OAK_NIGHTSTAND = registerBlockWithTooltip("dark_oak_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> JUNGLE_NIGHTSTAND = registerBlockWithTooltip("jungle_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ACACIA_NIGHTSTAND = registerBlockWithTooltip("acacia_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MANGROVE_NIGHTSTAND = registerBlockWithTooltip("mangrove_nightstand", key -> new NightStandBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_NIGHTSTAND = registerBlockWithTooltip("bamboo_nightstand", key -> new NightStandBlock(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> CHERRY_NIGHTSTAND = registerBlockWithTooltip("cherry_nightstand", key -> new NightStandBlock(Properties.CHERRY_WOOD.get().setId(key)));
    public static final Supplier<Block> CRIMSON_NIGHTSTAND = registerBlockWithTooltip("crimson_nightstand", key -> new NightStandBlock(Properties.NETHER_WOOD.get().setId(key)));
    public static final Supplier<Block> WARPED_NIGHTSTAND = registerBlockWithTooltip("warped_nightstand", key -> new NightStandBlock(Properties.NETHER_WOOD.get().setId(key)));

    public static final Supplier<Block> OAK_WALL_SUPPORT = registerBlock("oak_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> SPRUCE_WALL_SUPPORT = registerBlock("spruce_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BIRCH_WALL_SUPPORT = registerBlock("birch_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> DARK_OAK_WALL_SUPPORT = registerBlock("dark_oak_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> JUNGLE_WALL_SUPPORT = registerBlock("jungle_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ACACIA_WALL_SUPPORT = registerBlock("acacia_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MANGROVE_WALL_SUPPORT = registerBlock("mangrove_wall_support", key -> new WallSupportBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_WALL_SUPPORT = registerBlock("bamboo_wall_support", key -> new WallSupportBlock(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> CHERRY_WALL_SUPPORT = registerBlock("cherry_wall_support", key -> new WallSupportBlock(Properties.CHERRY_WOOD.get().setId(key)));
    public static final Supplier<Block> CRIMSON_WALL_SUPPORT = registerBlock("crimson_wall_support", key -> new WallSupportBlock(Properties.NETHER_WOOD.get().setId(key)));
    public static final Supplier<Block> WARPED_WALL_SUPPORT = registerBlock("warped_wall_support", key -> new WallSupportBlock(Properties.NETHER_WOOD.get().setId(key)));

    public static final Supplier<Block> RED_STOOL = registerBlock("red_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> RED_DARK_STOOL = registerBlock("red_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_STOOL = registerBlock("orange_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_DARK_STOOL = registerBlock("orange_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_STOOL = registerBlock("yellow_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_DARK_STOOL = registerBlock("yellow_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_STOOL = registerBlock("brown_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_DARK_STOOL = registerBlock("brown_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_STOOL = registerBlock("lime_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_DARK_STOOL = registerBlock("lime_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_STOOL = registerBlock("green_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_DARK_STOOL = registerBlock("green_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_STOOL = registerBlock("cyan_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_DARK_STOOL = registerBlock("cyan_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_STOOL = registerBlock("light_blue_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_DARK_STOOL = registerBlock("light_blue_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_STOOL = registerBlock("blue_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_DARK_STOOL = registerBlock("blue_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_STOOL = registerBlock("magenta_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_DARK_STOOL = registerBlock("magenta_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_STOOL = registerBlock("purple_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_DARK_STOOL = registerBlock("purple_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_STOOL = registerBlock("pink_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_DARK_STOOL = registerBlock("pink_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_STOOL = registerBlock("white_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_DARK_STOOL = registerBlock("white_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_STOOL = registerBlock("light_gray_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_DARK_STOOL = registerBlock("light_gray_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_STOOL = registerBlock("gray_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_DARK_STOOL = registerBlock("gray_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_STOOL = registerBlock("black_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_DARK_STOOL = registerBlock("black_dark_stool", key -> new StoolBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> RED_SOFA = registerBlock("red_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_SOFA = registerBlock("orange_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_SOFA = registerBlock("yellow_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_SOFA = registerBlock("brown_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_SOFA = registerBlock("lime_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_SOFA = registerBlock("green_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_SOFA = registerBlock("cyan_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_SOFA = registerBlock("light_blue_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_SOFA = registerBlock("blue_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_SOFA = registerBlock("magenta_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_SOFA = registerBlock("purple_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_SOFA = registerBlock("pink_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_SOFA = registerBlock("white_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_SOFA = registerBlock("light_gray_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_SOFA = registerBlock("gray_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_SOFA = registerBlock("black_sofa", key -> new SofaBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> RED_ARMCHAIR = registerBlock("red_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_ARMCHAIR = registerBlock("orange_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_ARMCHAIR = registerBlock("yellow_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_ARMCHAIR = registerBlock("brown_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_ARMCHAIR = registerBlock("lime_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_ARMCHAIR = registerBlock("green_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_ARMCHAIR = registerBlock("cyan_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_ARMCHAIR = registerBlock("light_blue_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_ARMCHAIR = registerBlock("blue_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_ARMCHAIR = registerBlock("magenta_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_ARMCHAIR = registerBlock("purple_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_ARMCHAIR = registerBlock("pink_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_ARMCHAIR = registerBlock("white_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_ARMCHAIR = registerBlock("light_gray_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_ARMCHAIR = registerBlock("gray_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_ARMCHAIR = registerBlock("black_armchair", key -> new ArmchairBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> RED_TABLE = registerBlock("red_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> RED_DARK_TABLE = registerBlock("red_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_TABLE = registerBlock("orange_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ORANGE_DARK_TABLE = registerBlock("orange_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_TABLE = registerBlock("yellow_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> YELLOW_DARK_TABLE = registerBlock("yellow_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_TABLE = registerBlock("brown_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BROWN_DARK_TABLE = registerBlock("brown_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_TABLE = registerBlock("lime_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIME_DARK_TABLE = registerBlock("lime_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_TABLE = registerBlock("green_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GREEN_DARK_TABLE = registerBlock("green_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_TABLE = registerBlock("cyan_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> CYAN_DARK_TABLE = registerBlock("cyan_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_TABLE = registerBlock("light_blue_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_DARK_TABLE = registerBlock("light_blue_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_TABLE = registerBlock("blue_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLUE_DARK_TABLE = registerBlock("blue_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_TABLE = registerBlock("magenta_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MAGENTA_DARK_TABLE = registerBlock("magenta_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_TABLE = registerBlock("purple_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PURPLE_DARK_TABLE = registerBlock("purple_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_TABLE = registerBlock("pink_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> PINK_DARK_TABLE = registerBlock("pink_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_TABLE = registerBlock("white_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> WHITE_DARK_TABLE = registerBlock("white_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_TABLE = registerBlock("light_gray_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_DARK_TABLE = registerBlock("light_gray_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_TABLE = registerBlock("gray_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> GRAY_DARK_TABLE = registerBlock("gray_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_TABLE = registerBlock("black_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BLACK_DARK_TABLE = registerBlock("black_dark_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));

    public static final Supplier<Block> OAK_TABLE = registerBlock("oak_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> SPRUCE_TABLE = registerBlock("spruce_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BIRCH_TABLE = registerBlock("birch_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> DARK_OAK_TABLE = registerBlock("dark_oak_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> JUNGLE_TABLE = registerBlock("jungle_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ACACIA_TABLE = registerBlock("acacia_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MANGROVE_TABLE = registerBlock("mangrove_table", key -> new TableBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_TABLE = registerBlock("bamboo_table", key -> new TableBlock(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> CHERRY_TABLE = registerBlock("cherry_table", key -> new TableBlock(Properties.CHERRY_WOOD.get().setId(key)));
    public static final Supplier<Block> CRIMSON_TABLE = registerBlock("crimson_table", key -> new TableBlock(Properties.NETHER_WOOD.get().setId(key)));
    public static final Supplier<Block> WARPED_TABLE = registerBlock("warped_table", key -> new TableBlock(Properties.NETHER_WOOD.get().setId(key)));

    public static final Supplier<Block> RED_SINK = registerBlock("red_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> ORANGE_SINK = registerBlock("orange_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> YELLOW_SINK = registerBlock("yellow_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BROWN_SINK = registerBlock("brown_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIME_SINK = registerBlock("lime_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GREEN_SINK = registerBlock("green_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> CYAN_SINK = registerBlock("cyan_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_SINK = registerBlock("light_blue_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLUE_SINK = registerBlock("blue_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> MAGENTA_SINK = registerBlock("magenta_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PURPLE_SINK = registerBlock("purple_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PINK_SINK = registerBlock("pink_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> WHITE_SINK = registerBlock("white_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_SINK = registerBlock("light_gray_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GRAY_SINK = registerBlock("gray_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLACK_SINK = registerBlock("black_sink", key -> new SinkBlock(Properties.MARBLE.get().setId(key)));

    public static final Supplier<Block> RED_STOVE = registerStove("red_stove");
    public static final Supplier<Block> ORANGE_STOVE = registerStove("orange_stove");
    public static final Supplier<Block> YELLOW_STOVE = registerStove("yellow_stove");
    public static final Supplier<Block> BROWN_STOVE = registerStove("brown_stove");
    public static final Supplier<Block> LIME_STOVE = registerStove("lime_stove");
    public static final Supplier<Block> GREEN_STOVE = registerStove("green_stove");
    public static final Supplier<Block> CYAN_STOVE = registerStove("cyan_stove");
    public static final Supplier<Block> LIGHT_BLUE_STOVE = registerStove("light_blue_stove");
    public static final Supplier<Block> BLUE_STOVE = registerStove("blue_stove");
    public static final Supplier<Block> MAGENTA_STOVE = registerStove("magenta_stove");
    public static final Supplier<Block> PURPLE_STOVE = registerStove("purple_stove");
    public static final Supplier<Block> PINK_STOVE = registerStove("pink_stove");
    public static final Supplier<Block> WHITE_STOVE = registerStove("white_stove");
    public static final Supplier<Block> LIGHT_GRAY_STOVE = registerStove("light_gray_stove");
    public static final Supplier<Block> GRAY_STOVE = registerStove("gray_stove");
    public static final Supplier<Block> BLACK_STOVE = registerStove("black_stove");

    public static final Supplier<Block> RED_CABINETRY = registerBlock("red_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> ORANGE_CABINETRY = registerBlock("orange_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> YELLOW_CABINETRY = registerBlock("yellow_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BROWN_CABINETRY = registerBlock("brown_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIME_CABINETRY = registerBlock("lime_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GREEN_CABINETRY = registerBlock("green_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> CYAN_CABINETRY = registerBlock("cyan_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_CABINETRY = registerBlock("light_blue_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLUE_CABINETRY = registerBlock("blue_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> MAGENTA_CABINETRY = registerBlock("magenta_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PURPLE_CABINETRY = registerBlock("purple_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PINK_CABINETRY = registerBlock("pink_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> WHITE_CABINETRY = registerBlock("white_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_CABINETRY = registerBlock("light_gray_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GRAY_CABINETRY = registerBlock("gray_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLACK_CABINETRY = registerBlock("black_cabinetry", key -> new CabinetryBlock(Properties.MARBLE.get().setId(key)));

    public static final Supplier<Block> RED_DRAWER = registerBlock("red_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> ORANGE_DRAWER = registerBlock("orange_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> YELLOW_DRAWER = registerBlock("yellow_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BROWN_DRAWER = registerBlock("brown_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIME_DRAWER = registerBlock("lime_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GREEN_DRAWER = registerBlock("green_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> CYAN_DRAWER = registerBlock("cyan_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_DRAWER = registerBlock("light_blue_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLUE_DRAWER = registerBlock("blue_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> MAGENTA_DRAWER = registerBlock("magenta_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PURPLE_DRAWER = registerBlock("purple_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> PINK_DRAWER = registerBlock("pink_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> WHITE_DRAWER = registerBlock("white_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_DRAWER = registerBlock("light_gray_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> GRAY_DRAWER = registerBlock("gray_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));
    public static final Supplier<Block> BLACK_DRAWER = registerBlock("black_drawer", key -> new DrawerBlock(Properties.MARBLE.get().setId(key)));

    public static final Supplier<Block> RED_UPPER_CABINET = registerBlockWithTooltip("red_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> ORANGE_UPPER_CABINET = registerBlockWithTooltip("orange_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> YELLOW_UPPER_CABINET = registerBlockWithTooltip("yellow_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> BROWN_UPPER_CABINET = registerBlockWithTooltip("brown_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> LIME_UPPER_CABINET = registerBlockWithTooltip("lime_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> GREEN_UPPER_CABINET = registerBlockWithTooltip("green_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> CYAN_UPPER_CABINET = registerBlockWithTooltip("cyan_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> LIGHT_BLUE_UPPER_CABINET = registerBlockWithTooltip("light_blue_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> BLUE_UPPER_CABINET = registerBlockWithTooltip("blue_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> MAGENTA_UPPER_CABINET = registerBlockWithTooltip("magenta_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> PURPLE_UPPER_CABINET = registerBlockWithTooltip("purple_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> PINK_UPPER_CABINET = registerBlockWithTooltip("pink_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> WHITE_UPPER_CABINET = registerBlockWithTooltip("white_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> LIGHT_GRAY_UPPER_CABINET = registerBlockWithTooltip("light_gray_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> GRAY_UPPER_CABINET = registerBlockWithTooltip("gray_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));
    public static final Supplier<Block> BLACK_UPPER_CABINET = registerBlockWithTooltip("black_upper_cabinet", key -> new UpperCabinetBlock(Properties.CONCRETE.get().setId(key)));

    public static final Supplier<Block> OAK_CABINET = registerBlock("oak_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> SPRUCE_CABINET = registerBlock("spruce_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BIRCH_CABINET = registerBlock("birch_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> DARK_OAK_CABINET = registerBlock("dark_oak_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> JUNGLE_CABINET = registerBlock("jungle_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> ACACIA_CABINET = registerBlock("acacia_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> MANGROVE_CABINET = registerBlock("mangrove_cabinet", key -> new CabinetBlock(Properties.WOOD.get().setId(key)));
    public static final Supplier<Block> BAMBOO_CABINET = registerBlock("bamboo_cabinet", key -> new CabinetBlock(Properties.BAMBOO_WOOD.get().setId(key)));
    public static final Supplier<Block> CHERRY_CABINET = registerBlock("cherry_cabinet", key -> new CabinetBlock(Properties.CHERRY_WOOD.get().setId(key)));
    public static final Supplier<Block> CRIMSON_CABINET = registerBlock("crimson_cabinet", key -> new CabinetBlock(Properties.NETHER_WOOD.get().setId(key)));
    public static final Supplier<Block> WARPED_CABINET = registerBlock("warped_cabinet", key -> new CabinetBlock(Properties.NETHER_WOOD.get().setId(key)));

    public static <T extends Block> Supplier<T> registerBlock(String name, Function<ResourceKey<Block>, T> block) {
        Supplier<T> supplier = CFRegistry.registerBlock(name, () -> block.apply(ResourceKey.create(Registries.BLOCK, CobbleFurnies.id(name))));
        CFRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, CobbleFurnies.id(name))).useBlockDescriptionPrefix()), "tab");
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockHidden(String name, Function<ResourceKey<Block>, T> block) {
        Supplier<T> supplier = CFRegistry.registerBlock(name, () -> block.apply(ResourceKey.create(Registries.BLOCK, CobbleFurnies.id(name))));
        CFRegistry.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, CobbleFurnies.id(name))).useBlockDescriptionPrefix()), null);
        return supplier;
    }

    public static <T extends Block> Supplier<T> registerBlockOnly(String name, Function<ResourceKey<Block>, T> block) {
        return CFRegistry.registerBlock(name, () -> block.apply(ResourceKey.create(Registries.BLOCK, CobbleFurnies.id(name))));
    }

    private static Supplier<Block> registerStove(String name) {
        return registerBlock(name, key -> new StoveBlock(Properties.MARBLE.get().setId(key)));
    }

    public static <T extends Block> Supplier<T> registerBlockWithTooltip(String name, Function<ResourceKey<Block>, T> block) {
        Supplier<T> supplier = CFRegistry.registerBlock(name, () -> block.apply(ResourceKey.create(Registries.BLOCK, CobbleFurnies.id(name))));

        String tooltipKey = "tooltip." + CobbleFurnies.MOD_ID + "." + name;

        CFRegistry.registerItem(
                name,
                () -> new TooltipBlockItem(supplier.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, CobbleFurnies.id(name))).useBlockDescriptionPrefix(), tooltipKey),
                "tab"
        );

        return supplier;
    }

    public static void init() {}
}
