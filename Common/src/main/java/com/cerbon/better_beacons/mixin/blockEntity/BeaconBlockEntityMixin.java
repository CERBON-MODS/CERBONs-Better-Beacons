package com.cerbon.better_beacons.mixin.blockEntity;

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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.LockCode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity implements IBeaconBlockEntityMixin {
    @Shadow @Final public static final MobEffect[][] BEACON_EFFECTS = BBUtils.getBeaconEffectsFromConfigFile();
    @Shadow @Final private static final Set<MobEffect> VALID_EFFECTS = Arrays.stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());

    @Shadow List<BeaconBlockEntity.BeaconBeamSection> beamSections;

    @Shadow MobEffect primaryPower;
    @Shadow MobEffect secondaryPower;

    @Unique private MobEffect bb_tertiaryEffect;
    @Unique private String bb_paymentItem;
    @Unique private int bb_primaryEffectAmplifier;

    @Shadow private LockCode lockKey;
    @Shadow int levels;

    @Shadow @Final private ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> BeaconBlockEntityMixin.this.levels;
                case 1 -> MobEffect.getIdFromNullable(BeaconBlockEntityMixin.this.primaryPower);
                case 2 -> MobEffect.getIdFromNullable(BeaconBlockEntityMixin.this.secondaryPower);
                case 3 -> MobEffect.getIdFromNullable(BeaconBlockEntityMixin.this.bb_tertiaryEffect);
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

                    BeaconBlockEntityMixin.this.primaryPower = BeaconBlockEntityMixin.getValidEffectById(value);
                }
                case 2 -> BeaconBlockEntityMixin.this.secondaryPower = BeaconBlockEntityMixin.getValidEffectById(value);
                case 3 -> BeaconBlockEntityMixin.this.bb_tertiaryEffect = BeaconBlockEntityMixin.getValidEffectById(value);
                case 4 -> BeaconBlockEntityMixin.this.bb_paymentItem = StringToIntMap.getString(value);
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    @Shadow public abstract Component getDisplayName();
    @Shadow static MobEffect getValidEffectById(int effectId) {return null;}

    public BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void bb_addCustomData(CompoundTag tag, CallbackInfo ci) {
        tag.putInt(BBConstants.TERTIARY_EFFECT_KEY, MobEffect.getIdFromNullable(this.bb_tertiaryEffect));
        tag.putInt(BBConstants.PRIMARY_EFFECT_AMPLIFIER_KEY, this.bb_primaryEffectAmplifier);

        if (this.bb_paymentItem != null)
            tag.putString(BBConstants.PAYMENT_ITEM_KEY, this.bb_paymentItem);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void bb_readCustomData(@NotNull CompoundTag tag, CallbackInfo ci) {
        this.bb_tertiaryEffect = getValidEffectById(tag.getInt(BBConstants.TERTIARY_EFFECT_KEY));
        this.bb_primaryEffectAmplifier = tag.getInt(BBConstants.PRIMARY_EFFECT_AMPLIFIER_KEY);

        if (tag.contains(BBConstants.PAYMENT_ITEM_KEY)) {
            this.bb_paymentItem = tag.getString(BBConstants.PAYMENT_ITEM_KEY);
            StringToIntMap.addString(this.bb_paymentItem);
        }
    }

    // This captures the for loop inside tick that computes the beacon segments
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 0, ordinal = 0))
    private static int bb_tick(int val, Level level, BlockPos pos, BlockState state, BeaconBlockEntity beaconBlockEntity) {
        return BeaconRedirectionAndTransparency.tickBeacon(beaconBlockEntity);
    }

    // This captures the variable d0 in the target method and adds to it value the payment item range
    @ModifyVariable(method = "applyEffects", at = @At(value = "LOAD", ordinal = 0))
    private static double bb_increaseRangeBasedOnPaymentItem(double defaultRange, @NotNull Level level, BlockPos pos, int levels, @Nullable MobEffect primary, @Nullable MobEffect secondary) {
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
    private static int bb_setPrimaryEffectAmplifier(int amplifier, Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary) {
        if (BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity)
                return ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getPrimaryEffectAmplifier();
        }
        return amplifier;
    }

    @ModifyConstant(method = "applyEffects", constant = @Constant(intValue = 1, ordinal = 0))
    private static int bb_setUpgradeAmplifier(int amplifier, Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary) {
        if (BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity)
                return ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getPrimaryEffectAmplifier() + 1;
        }
        return amplifier;
    }

    @Inject(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", shift = At.Shift.BY, by = 2))
    private static void bb_applyTertiaryEffects(Level level, BlockPos pos, int levels, MobEffect primary, MobEffect secondary, CallbackInfo ci, @Local(ordinal = 2) int j, @Local(ordinal = 0) List<Player> players, @Local(ordinal = 0) AABB aabb) {
        if (BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
                MobEffect tertiary = ((IBeaconBlockEntityMixin) beaconBlockEntity).bb_getTertiaryEffect();

                if (levels >= 5 && primary != tertiary && secondary != tertiary && tertiary != null) {
                    for (Player player : players)
                        player.addEffect(new MobEffectInstance(tertiary, j, 0, true, true));

                    //Add compatibility with beacons for all mod TODO: Fix it
//                    if (MiscUtils.isModLoaded(BBConstants.BEACONS_FOR_ALL)) {
//                        List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, aabb, BeaconsForAllMod::canApplyEffects);
//
//                        for (LivingEntity livingEntity : livingEntities)
//                            livingEntity.addEffect(new MobEffectInstance(tertiary, j, 0, true, true));
//                    }
                }
            }
        }
    }

    @Inject(method = "updateBase", at = @At("HEAD"), cancellable = true)
    private static void bb_makeBeaconBaseGoesTillLevelFiveAndChangeAmplifierBasedOnTheBeaconBaseBlock(Level level, int beaconX, int beaconY, int beaconZ, CallbackInfoReturnable<Integer> cir){
        BlockEntity blockEntity = level.getBlockEntity(new BlockPos(beaconX, beaconY, beaconZ));
        int pyramidMaxLevel = BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled ? 5 : 4;
        int pyramidLevel = 0;

        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
            IBeaconBlockEntityMixin beaconMixin = ((IBeaconBlockEntityMixin) beaconBlockEntity);
            BlockState firstBlockState = null;
            boolean canIncreaseAmplifier = true;

            int paymentItemRange = PaymentItemsRangeManager.getItemRangeMap().getOrDefault(beaconMixin.bb_getPaymentItem(), 0);
            HashMap<Block, Integer> blockAmplifierMap = BaseBlocksAmplifierManager.getBlockAmplifierMap();

            for(int i = 1; i <= pyramidMaxLevel; pyramidLevel = i++) {
                int y = beaconY - i;
                if (y < level.getMinBuildHeight()) break;

                boolean flag = true;
                for(int x = beaconX - i; x <= beaconX + i && flag; x++) {
                    for(int z = beaconZ - i; z <= beaconZ + i; z++) {
                        BlockState currentBlockState = level.getBlockState(new BlockPos(x, y, z));

                        if (!currentBlockState.is(BlockTags.BEACON_BASE_BLOCKS)) {
                            flag = false;
                            break;
                        }

                        if (firstBlockState == null) {
                            firstBlockState = currentBlockState;

                        } else if (currentBlockState.is(firstBlockState.getBlock()) && canIncreaseAmplifier && BetterBeacons.config.beaconRangeAndAmplifier.isBaseBlockAmplifierEnabled) {
                            beaconMixin.bb_setPrimaryEffectAmplifier(blockAmplifierMap.getOrDefault(currentBlockState.getBlock(), 0));

                        } else {
                            beaconMixin.bb_setPrimaryEffectAmplifier(0);
                            canIncreaseAmplifier = false;
                        }
                    }
                }

                if (!flag) break;
            }

            if (!level.isClientSide() && canIncreaseAmplifier && beaconMixin.bb_getPrimaryEffectAmplifier() > BaseBlocksAmplifierManager.getLowestAmplifier()) {
                for(ServerPlayer serverplayer : BBUtils.getPlayersNearBeacon(beaconBlockEntity.getLevel(), beaconX, beaconY, beaconZ))
                    BBCriteriaTriggers.INCREASE_EFFECTS_STRENGTH.trigger(serverplayer);
            }

            if (!level.isClientSide() && canIncreaseAmplifier && pyramidLevel == pyramidMaxLevel && beaconMixin.bb_getPrimaryEffectAmplifier() == BaseBlocksAmplifierManager.getHighestAmplifier() && paymentItemRange == PaymentItemsRangeManager.getHighestRange()) {
                for(ServerPlayer serverplayer : BBUtils.getPlayersNearBeacon(beaconBlockEntity.getLevel(), beaconX, beaconY, beaconZ))
                    BBCriteriaTriggers.TRUE_FULL_POWER.trigger(serverplayer);
            }
        }
        cir.setReturnValue(pyramidLevel);
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
    public MobEffect bb_getTertiaryEffect(){
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
