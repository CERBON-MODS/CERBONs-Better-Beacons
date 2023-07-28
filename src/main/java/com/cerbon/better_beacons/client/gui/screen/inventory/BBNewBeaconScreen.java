package com.cerbon.better_beacons.client.gui.screen.inventory;

import com.cerbon.better_beacons.menu.custom.BBNewBeaconMenu;
import com.cerbon.better_beacons.util.BBConstants;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class BBNewBeaconScreen extends AbstractContainerScreen<BBNewBeaconMenu> {
    static final ResourceLocation BEACON_LOCATION = new ResourceLocation("textures/gui/container/beacon.png");
    public static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");
    public static final Component SECONDARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.secondary");
    private final List<BBNewBeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();
    @Nullable
    MobEffect primary;
    @Nullable
    MobEffect secondary;
    boolean isEffectsActive;

    public BBNewBeaconScreen(BBNewBeaconMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 230;
        this.imageHeight = 219;
        pMenu.addSlotListener(new ContainerListener() {
            /**
             * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
             * contents of that slot.
             */
            public void slotChanged(@NotNull AbstractContainerMenu p_97973_, int p_97974_, @NotNull ItemStack p_97975_) {
            }

            public void dataChanged(@NotNull AbstractContainerMenu p_169628_, int p_169629_, int p_169630_) {
                BBNewBeaconScreen.this.primary = pMenu.getPrimaryEffect();
                BBNewBeaconScreen.this.secondary = pMenu.getSecondaryEffect();
                BBNewBeaconScreen.this.isEffectsActive = pMenu.isEffectsActive();
            }
        });
    }

    private <T extends AbstractWidget & BBNewBeaconScreen.BeaconButton> void addBeaconButton(T pBeaconButton) {
        this.addRenderableWidget(pBeaconButton);
        this.beaconButtons.add(pBeaconButton);
    }

    protected void init() {
        super.init();
        this.beaconButtons.clear();
        this.addBeaconButton(new BBNewBeaconScreen.BeaconConfirmButton(this.leftPos + 172, this.topPos + 106));
        this.addBeaconButton(new BBNewBeaconScreen.BeaconCancelButton(this.leftPos + 197, this.topPos + 106));

        for(int i = 0; i <= 2; ++i) {
            int j = BeaconBlockEntity.BEACON_EFFECTS[i].length;
            int k = j * 22 + (j - 1) * 2;

            for(int l = 0; l < j; ++l) {
                MobEffect mobeffect = BeaconBlockEntity.BEACON_EFFECTS[i][l];
                BBNewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton = new BBNewBeaconScreen.BeaconPowerButton(this.leftPos + 76 + l * 24 - k / 2, this.topPos + 22 + i * 25, mobeffect, true, i);
                beaconscreen$beaconpowerbutton.active = false;
                this.addBeaconButton(beaconscreen$beaconpowerbutton);
            }
        }

//        int i1 = 3;
        int j1 = BeaconBlockEntity.BEACON_EFFECTS[3].length;
        int k1 = j1 * 22 + (j1 - 1) * 2;

        for(int l1 = 0; l1 < j1; ++l1) {
            MobEffect mobeffect1 = BeaconBlockEntity.BEACON_EFFECTS[3][l1];
            BBNewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton2 = new BBNewBeaconScreen.BeaconPowerButton(this.leftPos + 167 + l1 * 24 - k1 / 2, this.topPos + 47, mobeffect1, false, 3);
            beaconscreen$beaconpowerbutton2.active = false;
            this.addBeaconButton(beaconscreen$beaconpowerbutton2);
        }

        BBNewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton1 = new BBNewBeaconScreen.BeaconUpgradePowerButton(this.leftPos + 156, this.topPos + 72, BeaconBlockEntity.BEACON_EFFECTS[0][0]);
        beaconscreen$beaconpowerbutton1.visible = false;
        this.addBeaconButton(beaconscreen$beaconpowerbutton1);
    }

    public void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    void updateButtons() {
        int i = this.menu.getLevels();
        this.beaconButtons.forEach((p_169615_) -> p_169615_.updateStatus(i));
    }

    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawCenteredString(this.font, PRIMARY_EFFECT_LABEL, 62, 10, 14737632);
        pGuiGraphics.drawCenteredString(this.font, SECONDARY_EFFECT_LABEL, 169, 10, 14737632);
        pGuiGraphics.drawCenteredString(this.font, BBConstants.BEACON_RANGE_LABEL, 77, 105, 14737632);
        pGuiGraphics.drawCenteredString(this.font, "++", 23, 106, 14737632);
        pGuiGraphics.drawCenteredString(this.font, "--", 131, 106, 14737632);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(BEACON_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        pGuiGraphics.renderItem(new ItemStack(Items.NETHERITE_INGOT), i + 14, j + 114);
        pGuiGraphics.renderItem(new ItemStack(Items.DIAMOND), i + 35, j + 114);
        pGuiGraphics.renderItem(new ItemStack(Items.EMERALD), i + 33 + 22, j + 114);
        pGuiGraphics.renderItem(new ItemStack(Items.GOLD_INGOT), i + 33 + 44, j + 114);
        pGuiGraphics.renderItem(new ItemStack(Items.IRON_INGOT), i + 34 + 66, j + 114);
        pGuiGraphics.renderItem(new ItemStack(Items.COPPER_INGOT), i + 34 + 88, j + 114);
        pGuiGraphics.pose().popPose();
    }

    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public interface BeaconButton {
        void updateStatus(int pBeaconTier);
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    public class BeaconCancelButton extends BBNewBeaconScreen.BeaconSpriteScreenButton {
        public BeaconCancelButton(int pX, int pY) {
            super(pX, pY, 112, 220, CommonComponents.GUI_CANCEL);
            this.setTooltip(BBConstants.CANCEL_BUTTON_TOOLTIP);
        }

        public void onPress() {
            BBNewBeaconScreen.this.minecraft.getConnection().send(new ServerboundSetBeaconPacket(Optional.empty(), Optional.empty()));
            BBNewBeaconScreen.this.minecraft.player.closeContainer();
        }

        public void updateStatus(int pBeaconTier) {
            this.active = BBNewBeaconScreen.this.menu.hasPayment() && BBNewBeaconScreen.this.isEffectsActive && BBNewBeaconScreen.this.primary != null;
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    public class BeaconConfirmButton extends BBNewBeaconScreen.BeaconSpriteScreenButton {
        protected BeaconConfirmButton(int pX, int pY) {
            super(pX, pY, 90, 220, CommonComponents.GUI_DONE);
            this.setTooltip(BBConstants.CONFIRM_BUTTON_TOOLTIP);
        }

        public void onPress() {
            BBNewBeaconScreen.this.minecraft.getConnection().send(new ServerboundSetBeaconPacket(Optional.ofNullable(BBNewBeaconScreen.this.primary), Optional.ofNullable(BBNewBeaconScreen.this.secondary)));
            BBNewBeaconScreen.this.minecraft.player.closeContainer();
        }

        public void updateStatus(int pBeaconTier) {
            this.active = BBNewBeaconScreen.this.menu.hasPayment() && BBNewBeaconScreen.this.primary != null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BeaconPowerButton extends BBNewBeaconScreen.BeaconScreenButton {
        private final boolean isPrimary;
        protected final int tier;
        private MobEffect effect;
        private TextureAtlasSprite sprite;

        public BeaconPowerButton(int pX, int pY, MobEffect pEffect, boolean pIsPrimary, int pTier) {
            super(pX, pY);
            this.isPrimary = pIsPrimary;
            this.tier = pTier;
            this.setEffect(pEffect);
        }

        protected void setEffect(MobEffect pEffect) {
            this.effect = pEffect;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(pEffect);
            this.setTooltip(Tooltip.create(this.createEffectDescription(pEffect), null));
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect pEffect) {
            return Component.translatable(pEffect.getDescriptionId());
        }

        public void onPress() {
            if (!this.isSelected()) {
                if (this.isPrimary) {
                    BBNewBeaconScreen.this.primary = this.effect;
                } else {
                    BBNewBeaconScreen.this.secondary = this.effect;
                }

                BBNewBeaconScreen.this.updateButtons();
            }
        }

        protected void renderIcon(@NotNull GuiGraphics pGuiGraphics) {
            pGuiGraphics.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
        }

        public void updateStatus(int pBeaconTier) {
            this.active = this.tier < pBeaconTier;
            this.setSelected(this.effect == (this.isPrimary ? BBNewBeaconScreen.this.primary : BBNewBeaconScreen.this.secondary));
        }

        protected @NotNull MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect);
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class BeaconScreenButton extends AbstractButton implements BBNewBeaconScreen.BeaconButton {
        private boolean selected;

        protected BeaconScreenButton(int pX, int pY) {
            super(pX, pY, 22, 22, CommonComponents.EMPTY);
        }

        protected BeaconScreenButton(int pX, int pY, Component pMessage) {
            super(pX, pY, 22, 22, pMessage);
        }

        public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
//            int i = 219;
            int j = 0;
            if (!this.active) {
                j += this.width * 2;
            } else if (this.selected) {
                j += this.width;
            } else if (this.isHoveredOrFocused()) {
                j += this.width * 3;
            }

            pGuiGraphics.blit(BBNewBeaconScreen.BEACON_LOCATION, this.getX(), this.getY(), j, 219, this.width, this.height);
            this.renderIcon(pGuiGraphics);
        }

        protected abstract void renderIcon(GuiGraphics pGuiGraphics);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean pSelected) {
            this.selected = pSelected;
        }

        public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class BeaconSpriteScreenButton extends BBNewBeaconScreen.BeaconScreenButton {
        private final int iconX;
        private final int iconY;

        protected BeaconSpriteScreenButton(int p_169663_, int p_169664_, int p_169665_, int p_169666_, Component p_169667_) {
            super(p_169663_, p_169664_, p_169667_);
            this.iconX = p_169665_;
            this.iconY = p_169666_;
        }

        protected void renderIcon(@NotNull GuiGraphics p_283624_) {
            p_283624_.blit(BBNewBeaconScreen.BEACON_LOCATION, this.getX() + 2, this.getY() + 2, this.iconX, this.iconY, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BeaconUpgradePowerButton extends BBNewBeaconScreen.BeaconPowerButton {
        public BeaconUpgradePowerButton(int pX, int pY, MobEffect pEffect) {
            super(pX, pY, pEffect, false, 3);
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect pEffect) {
            return Component.translatable(pEffect.getDescriptionId()).append(" II");
        }

        public void updateStatus(int pBeaconTier) {
            if (BBNewBeaconScreen.this.primary != null) {
                this.visible = true;
                this.setEffect(BBNewBeaconScreen.this.primary);
                super.updateStatus(pBeaconTier);
            } else {
                this.visible = false;
            }
        }
    }
}
