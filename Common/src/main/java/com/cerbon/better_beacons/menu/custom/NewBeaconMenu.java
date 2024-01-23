package com.cerbon.better_beacons.menu.custom;

import com.cerbon.better_beacons.BetterBeacons;
import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.StringToIntMap;
import com.cerbon.cerbons_api.api.static_utilities.RegistryUtils;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NewBeaconMenu extends AbstractContainerMenu {
    private final Container beacon = new SimpleContainer(1) {

        public boolean canPlaceItem(int index, ItemStack itemStack) {
            return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        public int getMaxStackSize() {
            return 1;
        }
    };
    private final NewBeaconMenu.PaymentSlot paymentSlot;
    private final ContainerLevelAccess access;
    private final ContainerData beaconData;
    public static boolean isTertiaryEffectsEnabled = BetterBeacons.config.beaconEffects.isTertiaryEffectsEnabled;

    public NewBeaconMenu(int containerId, Container container) {
        this(containerId, container, new SimpleContainerData(6), ContainerLevelAccess.NULL);
    }

    public NewBeaconMenu(int containerId, Container container, ContainerData beaconData, ContainerLevelAccess access) {
        super(BBMenuTypes.NEW_BEACON_MENU.get(), containerId);
        checkContainerDataCount(beaconData, 6);

        this.beaconData = beaconData;
        this.access = access;
        this.paymentSlot = new PaymentSlot(this.beacon, 0, isTertiaryEffectsEnabled ? 151 : 153, 109);
        this.addSlot(this.paymentSlot);
        this.addDataSlots(beaconData);

        //Inventory slots
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++)
                this.addSlot(new Slot(container, x + y * 9 + 9, isTertiaryEffectsEnabled ? 48 + 18 * x : 35 + x * 18, 137 + 18 * y));
        }

        //Hotbar slots
        for(int x = 0; x < 9; x++)
            this.addSlot(new Slot(container, x, isTertiaryEffectsEnabled ? 48 + 18 * x : 35 + x * 18, 195));
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            ItemStack itemstack = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());

            if (!itemstack.isEmpty())
                player.drop(itemstack, false);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, Blocks.BEACON);
    }

    @Override
    public void setData(int id, int data) {
        super.setData(id, data);
        this.broadcastChanges();
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true))
                    return ItemStack.EMPTY;

                slot.onQuickCraft(itemstack1, itemstack);

            } else if (this.moveItemStackTo(itemstack1, 0, 1, false))  //Forge Fix Shift Clicking in beacons with stacks larger than 1.
                return ItemStack.EMPTY;

            else if (index >= 1 && index < 28 && !this.moveItemStackTo(itemstack1, 28, 37, false))
                return ItemStack.EMPTY;

            else if (index >= 28 && index < 37 && !this.moveItemStackTo(itemstack1, 1, 28, false))
                return ItemStack.EMPTY;

            else if (!this.moveItemStackTo(itemstack1, 1, 37, false))
                return ItemStack.EMPTY;


            if (itemstack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public int getLevels() {
        return this.beaconData.get(0);
    }

    @Nullable
    public MobEffect getPrimaryEffect() {
        return MobEffect.byId(this.beaconData.get(1));
    }

    @Nullable
    public MobEffect getSecondaryEffect() {
        return MobEffect.byId(this.beaconData.get(2));
    }

    @Nullable
    public MobEffect getTertiaryEffect() {
        return MobEffect.byId(this.beaconData.get(3));
    }

    public String getPaymentItem() {
        return StringToIntMap.getString(this.beaconData.get(4));
    }

    public int getPrimaryEffectAmplifier() {
        return this.beaconData.get(5);
    }

    public boolean isEffectsActive() {
        return MobEffect.byId(this.beaconData.get(1)) != null;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void updateEffects(Optional<MobEffect> primaryEffect, Optional<MobEffect> secondaryEffect, Optional<MobEffect> tertiaryEffect) {
        if (this.paymentSlot.hasItem()) {
            this.beaconData.set(1, primaryEffect.map(MobEffect::getId).orElse(-1));
            this.beaconData.set(2, secondaryEffect.map(MobEffect::getId).orElse(-1));
            this.beaconData.set(3, tertiaryEffect.map(MobEffect::getId).orElse(-1));
            this.beaconData.set(4, StringToIntMap.addString(RegistryUtils.getItemKeyAsString(this.paymentSlot.getItem().getItem())));
            this.paymentSlot.remove(1);
            this.access.execute(Level::blockEntityChanged);
        }
    }

    public void removeActiveEffects(){
        this.beaconData.set(1 , -1);
        this.beaconData.set(2, -1);
        this.beaconData.set(3, -1);
        this.access.execute(Level::blockEntityChanged);
    }

    public boolean hasPayment() {
        return !this.beacon.getItem(0).isEmpty();
    }

    public static class PaymentSlot extends Slot {
        public PaymentSlot(Container container, int containerIndex, int xPos, int yPos) {
            super(container, containerIndex, xPos, yPos);
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
