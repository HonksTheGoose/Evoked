package com.teamtaigamodding.evoked.Mixins;

import com.teamtaigamodding.evoked.EvokedConfig;
import com.teamtaigamodding.evoked.scheduler.EvokedTickHandler;
import com.teamtaigamodding.evoked.scheduler.GiveTotemTask;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class EvokedTotemMixin {

    /** ~2.13 seconds, matching the totem pop animation. */
    private static final int EVOKED_HUSK_DELAY_TICKS = 43;

    /**
     * After vanilla has consumed a totem, give back a Totem Husk once the pop animation is over.
     * The effect/health signature identifies "a totem was just used".
     */
    @Inject(method = "checkTotemDeathProtection", at = @At("TAIL"))
    private void evoked$giveHuskAfterTotem(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            return;
        }
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectInstance absorption = entity.getEffect(MobEffects.ABSORPTION);
        MobEffectInstance regeneration = entity.getEffect(MobEffects.REGENERATION);
        MobEffectInstance fireResistance = entity.getEffect(MobEffects.FIRE_RESISTANCE);
        boolean isReal = absorption != null && regeneration != null && fireResistance != null
                && absorption.getDuration() == 100 && absorption.getAmplifier() == 1
                && regeneration.getDuration() == 900
                && fireResistance.getDuration() == 800;

        if (entity.getHealth() == 1.0F && isReal) {
            EvokedTickHandler.scheduleTask(new GiveTotemTask(entity), EVOKED_HUSK_DELAY_TICKS);
        }
    }

    /**
     * Vanilla totems ignore void damage (it bypasses invulnerability). When enabled in the config,
     * pop a held totem, lift the entity back up to y=160 and let it float down safely.
     */
    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void evoked$voidTotem(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!EvokedConfig.Common.COMMON.TotemsWorkInVoid.get()
                || !damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)
                || entity.getY() >= -64.0) {
            return;
        }

        ItemStack totem = null;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = entity.getItemInHand(hand);
            if (held.is(Items.TOTEM_OF_UNDYING)) {
                totem = held.copy();
                held.shrink(1);
                break;
            }
        }
        if (totem == null) {
            return;
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
            CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, totem);
        }

        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 300, 0));
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.teleport(entity.getX(), 160.0, entity.getZ(), entity.getYRot(), entity.getXRot());
        } else {
            entity.moveTo(entity.getX(), 160.0, entity.getZ(), entity.getYRot(), entity.getXRot());
        }
        // Brief levitation cancels the huge downward velocity built up while falling into the void.
        entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));

        EvokedTickHandler.scheduleTask(new GiveTotemTask(entity), EVOKED_HUSK_DELAY_TICKS);
        entity.level().broadcastEntityEvent(entity, (byte) 35);
        cir.setReturnValue(true);
    }
}
