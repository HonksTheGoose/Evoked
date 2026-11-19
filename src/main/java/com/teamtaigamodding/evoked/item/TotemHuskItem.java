package com.teamtaigamodding.evoked.item;

import com.teamtaigamodding.evoked.EvokedItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TotemHuskItem extends Item {

    public TotemHuskItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Right-clicking a Totem Husk in an inventory slot with a Rune of Life on the cursor
     * turns the husk into a Totem of Undying and consumes one rune.
     */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack huskItem, ItemStack clickItem, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player) && clickItem.is(EvokedItems.RUNE_OF_LIFE.get())) {
            slot.set(new ItemStack(Items.TOTEM_OF_UNDYING));
            clickItem.shrink(1);
            this.playInsertSound(player);
            return true;
        }
        return false;
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
}
