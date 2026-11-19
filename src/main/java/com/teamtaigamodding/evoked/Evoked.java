package com.teamtaigamodding.evoked;

import com.teamtaigamodding.evoked.scheduler.EvokedTickHandler;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Evoked.MOD_ID)
public class Evoked {
    public static final String MOD_ID = "evoked";

    public Evoked(IEventBus modEventBus, ModContainer modContainer) {
        EvokedItems.ITEMS.register(modEventBus);
        EvokedLootModifier.LOOT_MODIFIERS.register(modEventBus);
        modEventBus.addListener(Evoked::addCreativeTabItems);

        NeoForge.EVENT_BUS.addListener(EvokedTickHandler::onServerTick);
        NeoForge.EVENT_BUS.addListener(EvokedTickHandler::onServerStopped);

        modContainer.registerConfig(ModConfig.Type.COMMON, EvokedConfig.Common.COMMON_SPEC);
    }

    private static void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EvokedItems.RUNE_OF_LIFE.get());
        } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(EvokedItems.TOTEM_HUSK.get());
        }
    }
}
