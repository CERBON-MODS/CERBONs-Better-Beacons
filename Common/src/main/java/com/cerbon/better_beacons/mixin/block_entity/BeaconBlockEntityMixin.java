package com.cerbon.better_beacons.mixin.block_entity;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.advancement.BBCriteriaTriggers;
import com.cerbon.better_beacons.datapack.BaseBlocksAmplifierManager;
import com.cerbon.better_beacons.datapack.PaymentItemsRangeManager;
import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import com.cerbon.better_beacons.util.StringToIntMap;
import com.cerbon.better_beacons.util.mixin.BeaconRedirectionAndTransparency;
import com.cerbon.better_beacons.util.mixin.IBeaconBlockEntityMixin;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.jcraft.jorbis.Block;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.LockCode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity implements IBeaconBlockEntityMixin {
    @Shadow @Final public static final List<List<Holder<MobEffect>>> BEACON_EFFECTS = BBUtils.getBeaconEffectsFromConfigFile();
    @Shadow @Final private static final Set<Holder<MobEffect>> VALID_EFFECTS = BEACON_EFFECTS.stream().flatMap(Collection::stream).collect(Collectors.toSet());

    @Shadow List<BeaconBlockEntity.BeaconBeamSection> beamSections;

    @Shadow Holder<MobEffect> primaryPower;
    @Shadow Holder<MobEffect> secondaryPower;

    @Unique private Holder<MobEffect> bb_tertiaryEffect;
    @Unique private String bb_paymentItem;
    @Unique private int bb_primaryEffectAmplifier;

    @Shadow private LockCode lockKey;
    @Shadow int levels;

    @Shadow @Final private ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BeaconBlockEntityMixin.this.levels;
                case 1 -> BeaconMenu.encodeEffect(BeaconBlockEntityMixin.this.primaryPower);
                case 2 -> BeaconMenu.encodeEffect(BeaconBlockEntityMixin.this.secondaryPower);
                case 3 -> BeaconMenu.encodeEffect(BeaconBlockEntityMixin.this.bb_tertiaryEffect);
                case 4 -> StringToIntMap.getInt(BeaconBlockEntityMixin.this.bb_paymentItem);
                case 5 -> BeaconBlockEntityMixin.this.bb_primaryEffectAmplifier;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> BeaconBlockEntityMixin.this.levels = value;
                case 1 -> {
                    if (!BeaconBlockEntityMixin.this.level.isClientSide && !BeaconBlockEntityMixin.this.beamSections.isEmpty())
                        BeaconBlockEntity.playSound(BeaconBlockEntityMixin.this.level, BeaconBlockEntityMixin.this.worldPosition, SoundEvents.BEACON_POWER_SELECT);

                    BeaconBlockEntityMixin.this.primaryPower = filterEffect(BeaconMenu.decodeEffect(value));
                }
                case 2 -> BeaconBlockEntityMixin.this.secondaryPower = filterEffect(BeaconMenu.decodeEffect(value));
                case 3 -> BeaconBlockEntityMixin.this.bb_tertiaryEffect = filterEffect(BeaconMenu.decodeEffect(value));
                case 4 -> BeaconBlockEntityMixin.this.bb_paymentItem = StringToIntMap.getString(value);
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    @Shadow public abstract Component getDisplayName();
    @Shadow static Holder<MobEffect> filterEffect(@Nullable Holder<MobEffect> effect) {
        return VALID_EFFECTS.contains(effect) ? effect : null;
    }
    @Shadow @Nullable private static Holder<MobEffect> loadEffect(CompoundTag tag, String key) {return null;}
    @Shadow private static void storeEffect(CompoundTag tag, String key, Holder<MobEffect> effect) {}

    public BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void bb_addCustomData(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        storeEffect(tag, BBConstants.TERTIARY_EFFECT_KEY, this.bb_tertiaryEffect);
        tag.putInt(BBConstants.PRIMARY_EFFECT_AMPLIFIER_KEY, this.bb_primaryEffectAmplifier);

        if (this.bb_paymentItem != null)
            tag.putString(BBConstants.PAYMENT_ITEM_KEY, this.bb_paymentItem);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void bb_readCustomData(@NotNull CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        this.bb_tertiaryEffect = loadEffect(tag, BBConstants.TERTIARY_EFFECT_KEY);
        this.bb_primaryEffectAmplifier = tag.getInt(BBConstants.PRIMARY_EFFECT_AMPLIFIER_KEY);

        if (tag.contains(BBConstants.PAYMENT_ITEM_KEY)) {
            this.bb_paymentItem = tag.getString(BBConstants.PAYMENT_ITEM_KEY);
            StringToIntMap.addString(this.bb_paymentItem);
        }
    }

    // This captures the for loop inside tick that computes the beacon segments
    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 0))
    private static int bb_tick(int val, Level level, BlockPos pos, BlockState state, BeaconBlockEntity beaconBlockEntity) {
        return BeaconRedirectionAndTransparency.tickBeacon(beaconBlockEntity);
    }

    // This captures the variable d0 in the target method and adds to it value the payment item range
    @ModifyVariable(method = "applyEffects", at = @At(value = "LOAD", ordinal = 0))
    private static double bb_increaseRangeBasedOnPaymentItem(double defaultRange, @NotNull Level level, BlockPos pos, int beaconLevel, @Nullable Holder<MobEffect> primaryEffect, @Nullable Holder<MobEffect> secondaryEffect) {
        if (BetterBeacons.config.beaconRangeAndAmplifier.isPaymentItemRangeEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity){
                String paymentItem = ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getPaymentItem();

                if (paymentItem != null)
                    return PaymentItemsRangeManager.getItemRangeMap().getOrDefault(paymentItem, 0) + defaultRange;
            }
        }
        return defaultRange;
    }

    @ModifyConstant(method = "applyEffects", constant = @Constant(intValue = 0, ordinal = 0))
    private static int bb_setPrimaryEffectAmplifier(int amplifier, Level level, BlockPos pos, int beaconLevel, @Nullable Holder<MobEffect> primaryEffect, @Nullable Holder<MobEffect> secondaryEffect) {
        if (BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity)
                return ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getPrimaryEffectAmplifier();
        }
        return amplifier;
    }

    @ModifyConstant(method = "applyEffects", constant = @Constant(intValue = 1, ordinal = 0))
    private static int bb_setUpgradeAmplifier(int amplifier, Level level, BlockPos pos, int beaconLevel, @Nullable Holder<MobEffect> primaryEffect, @Nullable Holder<MobEffect> secondaryEffect) {
        if (BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity)
                return ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getPrimaryEffectAmplifier() + 1;
        }
        return amplifier;
    }

    @Inject(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.BY, by = 2))
    private static void bb_applyTertiaryEffects(Level level, BlockPos pos, int levels, @Nullable Holder<MobEffect> primary, @Nullable Holder<MobEffect> secondary, CallbackInfo ci, @Local(ordinal = 2) int j, @Local(ordinal = 0) List<Player> players, @Local(ordinal = 0) AABB aabb) {
        if (BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
                Holder<MobEffect> tertiary = ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getTertiaryEffect();

                if (levels >= 5 && primary != tertiary && secondary != tertiary && tertiary != null) {
                    for (Player player : players)
                        player.addEffect(new MobEffectInstance(tertiary, j, 0, true, true));

                    //Add compatibility with beacons for all mod
                    if (MiscUtils.isModLoaded(BBConstants.BEACONS_FOR_ALL)) {
                        try {
                            Class<?> bfaClass = Class.forName("com.illusivesoulworks.beaconsforall.BeaconsForAllMod");
                            Method canApplyEffectsMethod = bfaClass.getMethod("canApplyEffects", LivingEntity.class);

                            List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, aabb, livingEntity -> {
                                try {
                                    return (boolean) canApplyEffectsMethod.invoke(null, livingEntity);

                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    BBConstants.LOGGER.error("Cannot invoke method 'canApplyEffects' from BeaconsForAllMod", e);
                                    return false;
                                }
                            });

                            for (LivingEntity livingEntity : livingEntities)
                                livingEntity.addEffect(new MobEffectInstance(tertiary, j, 0, true, true));

                        } catch (ClassNotFoundException | NoSuchMethodException e) {
                            BBConstants.LOGGER.error("Class/Method from BeaconsForAllMod mod does not exist", e);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "updateBase", at = @At("HEAD"), cancellable = true)
    private static void bb_updateBeaconBaseAmplifier(Level level, int beaconX, int beaconY, int beaconZ, CallbackInfoReturnable<Integer> cir) {
        BlockPos beaconPos = new BlockPos(beaconX, beaconY, beaconZ);
        BlockEntity blockEntity = level.getBlockEntity(beaconPos);
        if (!(blockEntity instanceof BeaconBlockEntity)) cir.setReturnValue(0);

        IBeaconBlockEntityMixin beaconMixin = (IBeaconBlockEntityMixin) blockEntity;

        int pyramidMaxLevel = BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled ? 5 : 4;
        int currPyramidLevel = 0;

        List<BlockState> pyramidBlocks = new ObjectArrayList<>();

        for(int pyramidLevel = 1; pyramidLevel <= pyramidMaxLevel; currPyramidLevel = pyramidLevel++) {
            int y = beaconY - pyramidLevel;
            if (y < level.getMinBuildHeight()) break;

            boolean isBeaconBaseBlock = true;
            for(int x = beaconX - pyramidLevel; x <= beaconX + pyramidLevel && isBeaconBaseBlock; x++) {
                for(int z = beaconZ - pyramidLevel; z <= beaconZ + pyramidLevel; z++) {
                    BlockState currentBlockState = level.getBlockState(new BlockPos(x, y, z));

                    if (!currentBlockState.is(BlockTags.BEACON_BASE_BLOCKS)) {
                        isBeaconBaseBlock = false;
                        break;
                    }
                    else pyramidBlocks.add(currentBlockState);
                }
            }
            if (!isBeaconBaseBlock) break;
        }

        int pyramidBlocksSize = pyramidBlocks.size();
        int amplifier = pyramidBlocksSize > 0 ?
                pyramidBlocks.stream()
                        .mapToInt(blockState -> BaseBlocksAmplifierManager
                                .getBlockAmplifierMap()
                                .getOrDefault(blockState.getBlock(), 0))
                        .sum() / pyramidBlocksSize
                : 0;

        beaconMixin.bb_setPrimaryEffectAmplifier(amplifier);

        int paymentItemRange = PaymentItemsRangeManager
                .getItemRangeMap()
                .getOrDefault(beaconMixin.bb_getPaymentItem(), 0);

        if (!level.isClientSide()) {
            if (currPyramidLevel == pyramidMaxLevel && amplifier == BaseBlocksAmplifierManager.getHighestAmplifier() && paymentItemRange == PaymentItemsRangeManager.getHighestRange())
                for (ServerPlayer serverPlayer : BBUtils.getPlayersNearBeacon(blockEntity.getLevel(), beaconX, beaconY, beaconZ)) {
                    System.out.println("Giving advancement");
                    BBCriteriaTriggers.TRUE_FULL_POWER.trigger(serverPlayer);
                    BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH.trigger(serverPlayer);
                }

            else if (amplifier > BaseBlocksAmplifierManager.getLowestAmplifier())
                for (ServerPlayer serverPlayer : BBUtils.getPlayersNearBeacon(blockEntity.getLevel(), beaconX, beaconY, beaconZ))
                    BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH.trigger(serverPlayer);
        }

        cir.setReturnValue(currPyramidLevel);
    }

    @Inject(method = "createMenu", at = @At("RETURN"), cancellable = true)
    private void better_beacons_addNewBeaconMenu(int containerId, Inventory playerInventory, Player player, @NotNull CallbackInfoReturnable<AbstractContainerMenu> cir) {
        cir.setReturnValue(BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName()) ? new NewBeaconMenu(containerId, playerInventory, this.dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos())) : null);
    }

    @Override
    public String bb_getPaymentItem(){
        return this.bb_paymentItem;
    }

    @Override
    public Holder<MobEffect> bb_getTertiaryEffect(){
        return this.bb_tertiaryEffect;
    }

    @Override
    public int bb_getPrimaryEffectAmplifier() {
        return this.bb_primaryEffectAmplifier;
    }

    @Override
    public void bb_setPrimaryEffectAmplifier(int amplifier) {
        this.bb_primaryEffectAmplifier = amplifier;
    }
}
