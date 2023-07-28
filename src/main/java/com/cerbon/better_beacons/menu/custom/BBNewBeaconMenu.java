package com.cerbon.better_beacons.menu.custom;

import com.cerbon.better_beacons.menu.BBMenuTypes;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBContainerData;
import com.cerbon.better_beacons.util.BBSimpleContainerData;
import com.cerbon.better_beacons.util.BBUtils;
import net.minecraft.network.FriendlyByteBuf;
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

import javax.annotation.Nullable;
import java.util.Optional;

public class BBNewBeaconMenu extends AbstractContainerMenu {
//    private static final int PAYMENT_SLOT = 0;
//    private static final int SLOT_COUNT = 1;
//    private static final int DATA_COUNT = 3;
//    private static final int INV_SLOT_START = 1;
//    private static final int INV_SLOT_END = 28;
//    private static final int USE_ROW_SLOT_START = 28;
//    private static final int USE_ROW_SLOT_END = 37;
    private final Container beacon = new SimpleContainer(1) {
        /**
         * Returns {@code true} if automation is allowed to insert the given stack (ignoring stack size) into the given
         * slot. For guis use Slot.isItemValid
         */
        public boolean canPlaceItem(int index, ItemStack itemStack) {
            return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        /**
         * Returns the maximum stack size for an inventory slot. Seems to always be 64, possibly will be extended.
         */
        public int getMaxStackSize() {
            return 1;
        }
    };

    @SuppressWarnings("unused")
    public BBNewBeaconMenu(int pContainerId, Container pContainer, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, pContainer, new SimpleContainerData(3), new BBSimpleContainerData(), ContainerLevelAccess.NULL);
    }

    private final BBNewBeaconMenu.PaymentSlot paymentSlot;
    private final ContainerLevelAccess access;
    private final ContainerData beaconData;
    private final BBContainerData bbBeaconData;

    public BBNewBeaconMenu(int pContainerId, Container pContainer, ContainerData pBeaconData, BBContainerData bbBeaconData, ContainerLevelAccess pAccess) {
        super(BBMenuTypes.NEW_BEACON_MENU.get(), pContainerId);
        checkContainerDataCount(pBeaconData, 3);
        this.beaconData = pBeaconData;
        this.bbBeaconData = bbBeaconData;
        this.access = pAccess;
        this.paymentSlot = new PaymentSlot(this.beacon, 0, 153, 109);
        this.addSlot(this.paymentSlot);
        this.addDataSlots(pBeaconData);
//        int i = 36;
//        int j = 137;

        for(int k = 0; k < 3; ++k) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(pContainer, l + k * 9 + 9, 36 + l * 18, 137 + k * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(pContainer, i1, 36 + i1 * 18, 195));
        }

    }

    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        if (!pPlayer.level().isClientSide) {
            ItemStack itemstack = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
            if (!itemstack.isEmpty()) {
                pPlayer.drop(itemstack, false);
            }

        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(this.access, pPlayer, Blocks.BEACON);
    }

    public void setData(int pId, int pData) {
        super.setData(pId, pData);
        this.broadcastChanges();
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (this.moveItemStackTo(itemstack1, 0, 1, false)) { //Forge Fix Shift Clicking in beacons with stacks larger than 1.
                return ItemStack.EMPTY;
            } else if (pIndex >= 1 && pIndex < 28) {
                if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 28 && pIndex < 37) {
                if (!this.moveItemStackTo(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void updateEffects(Optional<MobEffect> pPrimaryEffect, Optional<MobEffect> pSecondaryEffect) {
        if (this.paymentSlot.hasItem()) {
            this.beaconData.set(1, pPrimaryEffect.map(MobEffect::getId).orElse(-1));
            this.beaconData.set(2, pSecondaryEffect.map(MobEffect::getId).orElse(-1));
            this.bbBeaconData.setString(BBConstants.PAYMENT_ITEM_KEY, BBUtils.getItemNameWithCreatorModId(this.paymentSlot.getItem()));
            this.paymentSlot.remove(1);
            this.access.execute(Level::blockEntityChanged);
        }

    }

    public boolean hasPayment() {
        return !this.beacon.getItem(0).isEmpty();
    }

    public static class PaymentSlot extends Slot {
        public PaymentSlot(Container pContainer, int pContainerIndex, int pXPosition, int pYPosition) {
            super(pContainer, pContainerIndex, pXPosition, pYPosition);
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        public int getMaxStackSize() {
            return 1;
        }
    }
}
