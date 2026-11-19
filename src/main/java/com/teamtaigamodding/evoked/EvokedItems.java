package com.teamtaigamodding.evoked;

import com.teamtaigamodding.evoked.item.TotemHuskItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EvokedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Evoked.MOD_ID);

    public static final DeferredItem<Item> RUNE_OF_LIFE = ITEMS.registerSimpleItem("rune_of_life", new Item.Properties());
    public static final DeferredItem<TotemHuskItem> TOTEM_HUSK = ITEMS.registerItem("totem_husk", TotemHuskItem::new, new Item.Properties().stacksTo(1));
}
