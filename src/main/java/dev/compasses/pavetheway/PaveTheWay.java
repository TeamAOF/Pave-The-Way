package dev.compasses.pavetheway;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DirtPathBlock;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.registries.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(PaveTheWay.MODID)
public class PaveTheWay {
    public static final String MODID = "pavetheway";

    private final Lazy<Block> DIRT_PATH_BLOCK = Lazy.of(() -> new DirtPathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT_PATH)));
    private final Lazy<Item> DIRT_PATH_ITEM = Lazy.of(() -> new BlockItem(DIRT_PATH_BLOCK.get(), new Item.Properties()));

    public PaveTheWay(IEventBus modBus) {
        modBus.addListener(this::registerContent);
        modBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.addListener(this::onToolUsed);
    }

    private void registerContent(final RegisterEvent event) {
        event.register(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, "dirt_path"), DIRT_PATH_BLOCK);
        event.register(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MODID, "dirt_path"), DIRT_PATH_ITEM);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(DIRT_PATH_ITEM.get());
        }
    }

    private void onToolUsed(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() == ItemAbilities.SHOVEL_FLATTEN) {
            if (event.getContext().isSecondaryUseActive()) {
                if (event.getState().is(Blocks.DIRT_PATH) || event.getState().is(DIRT_PATH_BLOCK.get())) {
                    event.setFinalState(Blocks.DIRT.defaultBlockState());
                } else {
                    event.setCanceled(true);
                }
            } else if (event.getState().is(Blocks.DIRT_PATH)) {
                event.setFinalState(DIRT_PATH_BLOCK.get().defaultBlockState());
            }
        }
    }
}
