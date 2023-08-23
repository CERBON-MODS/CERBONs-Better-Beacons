package com.cerbon.better_beacons.mixin.block.entity;

import com.cerbon.better_beacons.menu.custom.BBNewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import com.cerbon.better_beacons.util.json.BBBeaconPaymentItemsRangeManager;
import com.cerbon.better_beacons.world.inventory.BBContainerData;
import com.cerbon.better_beacons.util.IBeaconBlockEntityMixin;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin extends BlockEntity implements IBeaconBlockEntityMixin {
    @Shadow @Final public static final MobEffect[][] BEACON_EFFECTS = BBUtils.getBeaconEffectsFromConfigFile();
    @Shadow @Final @SuppressWarnings("unused") private static final Set<MobEffect> VALID_EFFECTS = Arrays.stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());
    @Shadow private LockCode lockKey;
    @Shadow @Final private ContainerData dataAccess;

    @Shadow public abstract Component getDisplayName();

    @Unique private String better_beacons_PaymentItem;
    @Unique private BBContainerData better_beacons_dataAccess = (key, value) -> {
        if (key.equals(BBConstants.PAYMENT_ITEM_KEY)){
            BeaconBlockEntityMixin.this.better_beacons_PaymentItem = value;
        }
    };

    public BeaconBlockEntityMixin(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void better_beacons_addCustomData(CompoundTag pTag, CallbackInfo ci){
        if (this.better_beacons_PaymentItem != null){
            pTag.putString(BBConstants.PAYMENT_ITEM_KEY, this.better_beacons_PaymentItem);
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void better_beacons_readCustomData(CompoundTag pTag, CallbackInfo ci){
        if (pTag.contains(BBConstants.PAYMENT_ITEM_KEY)){
            this.better_beacons_PaymentItem = pTag.getString(BBConstants.PAYMENT_ITEM_KEY);
        }
    }

    @ModifyVariable(method = "applyEffects", at = @At(value = "LOAD", ordinal = 0))
    private static double better_beacons_increaseRangeBasedOnPaymentItem(double defaultRange, Level pLevel, BlockPos pPos, int pLevels, @Nullable MobEffect pPrimary, @Nullable MobEffect pSecondary){
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity){
            String paymentItem = ((IBeaconBlockEntityMixin) beaconBlockEntity).better_beacons_getPaymentItem();

            if (paymentItem != null){
                return BBBeaconPaymentItemsRangeManager.getAllValuesLists().stream()
                        .flatMap(valuesList -> valuesList.values().stream())
                        .filter(value -> paymentItem.equals(BBUtils.getItemKeyAsString(value.item())) && value.range() >= 0)
                        .mapToDouble(value -> defaultRange + value.range())
                        .findFirst()
                        .orElse(defaultRange);
            }
        }
        return defaultRange;
    }

    @Inject(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", ordinal = 1), cancellable = true)
    private static void better_beacons_setHealthBoostEffectAmplifierTo1(Level pLevel, BlockPos pPos, int pLevels, MobEffect pPrimary, @NotNull MobEffect pSecondary, @NotNull CallbackInfo ci, @Local(ordinal = 2) int j, @Local(ordinal = 0) @NotNull Player player1){
        player1.addEffect(new MobEffectInstance(pSecondary, j, pSecondary.equals(MobEffects.HEALTH_BOOST) ? 1 : 0 , true, true));
        ci.cancel();
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "createMenu", at = @At("RETURN"), cancellable = true)
    private void better_beacons_addNewBeaconMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer, CallbackInfoReturnable<AbstractContainerMenu> cir){
        cir.setReturnValue(BaseContainerBlockEntity.canUnlock(pPlayer, this.lockKey, this.getDisplayName()) ? new BBNewBeaconMenu(pContainerId, pPlayerInventory, this.dataAccess, this.better_beacons_dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos())) : null);
    }

    @Override
    public String better_beacons_getPaymentItem(){
        return this.better_beacons_PaymentItem;
    }
}
