package com.cerbon.better_beacons.mixin.block.entity;

import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import com.cerbon.better_beacons.util.BeaconRedirectionAndTransparency;
import com.cerbon.better_beacons.util.IBeaconBlockEntityMixin;
import com.cerbon.better_beacons.util.json.BeaconPaymentItemsRangeManager;
import com.cerbon.better_beacons.world.inventory.BBContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity implements IBeaconBlockEntityMixin {
    @Shadow @Final public static final MobEffect[][] BEACON_EFFECTS = BBUtils.getBeaconEffectsFromConfigFile();
    @Shadow @Final @SuppressWarnings("unused") private static final Set<MobEffect> VALID_EFFECTS = Arrays.stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());
    @Shadow private LockCode lockKey;
    @Shadow @Final private ContainerData dataAccess;

    @Unique private String better_beacons_PaymentItem;
    @Unique private BBContainerData better_beacons_dataAccess = (key, value) -> {
        if (key.equals(BBConstants.PAYMENT_ITEM_KEY))
            BeaconBlockEntityMixin.this.better_beacons_PaymentItem = value;
    };

    @Shadow public abstract Component getDisplayName();

    public BeaconBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void better_beacons_addCustomData(CompoundTag tag, CallbackInfo ci){
        if (this.better_beacons_PaymentItem != null)
            tag.putString(BBConstants.PAYMENT_ITEM_KEY, this.better_beacons_PaymentItem);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void better_beacons_readCustomData(@NotNull CompoundTag tag, CallbackInfo ci){
        if (tag.contains(BBConstants.PAYMENT_ITEM_KEY))
            this.better_beacons_PaymentItem = tag.getString(BBConstants.PAYMENT_ITEM_KEY);
    }

    // This captures the for loop inside tick that computes the beacon segments
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 0, ordinal = 0))
    private static int better_beacons_tick(int val, Level level, BlockPos pos, BlockState state, BeaconBlockEntity beaconBlockEntity) {
        return BeaconRedirectionAndTransparency.tickBeacon(beaconBlockEntity);
    }

    @ModifyVariable(method = "applyEffects", at = @At(value = "LOAD", ordinal = 0))
    private static double better_beacons_increaseRangeBasedOnPaymentItem(double defaultRange, @NotNull Level level, BlockPos pos, int levels, @Nullable MobEffect primary, @Nullable MobEffect secondary){
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity){
            String paymentItem = ((IBeaconBlockEntityMixin) beaconBlockEntity).better_beacons_getPaymentItem();

            if (paymentItem != null)
                return BeaconPaymentItemsRangeManager.getItemRangeMap().getOrDefault(paymentItem, 0) + defaultRange;
        }
        return defaultRange;
    }

    @Inject(method = "createMenu", at = @At("RETURN"), cancellable = true)
    private void better_beacons_addNewBeaconMenu(int containerId, Inventory playerInventory, Player player, @NotNull CallbackInfoReturnable<AbstractContainerMenu> cir){
        cir.setReturnValue(BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName()) ? new NewBeaconMenu(containerId, playerInventory, this.dataAccess, this.better_beacons_dataAccess, ContainerLevelAccess.create(Objects.requireNonNull(this.level), this.getBlockPos())) : null);
    }

    @Override
    public String better_beacons_getPaymentItem(){
        return this.better_beacons_PaymentItem;
    }
}
