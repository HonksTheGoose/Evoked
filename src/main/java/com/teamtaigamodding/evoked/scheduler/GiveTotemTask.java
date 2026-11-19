package com.teamtaigamodding.evoked.scheduler;

import com.teamtaigamodding.evoked.EvokedItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GiveTotemTask implements Runnable {
    private final LivingEntity entity;

    public GiveTotemTask(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        if (!this.entity.isAlive()) {
            return;
        }
        ItemStack totemHusk = new ItemStack(EvokedItems.TOTEM_HUSK.get());
        if (this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, totemHusk);
        } else if (this.entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
            this.entity.setItemInHand(InteractionHand.OFF_HAND, totemHusk);
        } else if (this.entity instanceof Player player && !player.getInventory().add(totemHusk)) {
            player.drop(totemHusk, false);
        }
    }
}
