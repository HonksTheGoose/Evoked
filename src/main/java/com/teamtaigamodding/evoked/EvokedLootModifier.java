package com.teamtaigamodding.evoked;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class EvokedLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Evoked.MOD_ID);

    public static final Supplier<MapCodec<RemoveOneItemModifier>> REMOVE_ITEM =
            LOOT_MODIFIERS.register("remove_item", () -> RemoveOneItemModifier.CODEC);
}
