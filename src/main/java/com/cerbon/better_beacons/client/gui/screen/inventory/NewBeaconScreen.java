package com.cerbon.better_beacons.client.gui.screen.inventory;

import com.cerbon.better_beacons.config.BBClientConfigs;
import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.packet.BBPacketHandler;
import com.cerbon.better_beacons.packet.custom.BeaconC2SPacket;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import com.cerbon.better_beacons.util.NumberToRoman;
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
public class NewBeaconScreen extends AbstractContainerScreen<NewBeaconMenu> {
    private static final ResourceLocation BEACON_TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/beacon2.png");
    public static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");
    public static final Component SECONDARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.secondary");
    private final List<NewBeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();
    @Nullable MobEffect primary;
    @Nullable MobEffect secondary;
    @Nullable MobEffect tertiary;
    boolean isEffectsActive;
    String paymentItem;
    int upgradeAmplifier;

    public NewBeaconScreen(NewBeaconMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 219;

        menu.addSlotListener(new ContainerListener() {
            /**
             * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
             * contents of that slot.
             */
            public void slotChanged(@NotNull AbstractContainerMenu containerToSend, int dataSlotIndex, @NotNull ItemStack itemStack) {}

            public void dataChanged(@NotNull AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
                NewBeaconScreen.this.primary = menu.getPrimaryEffect();
                NewBeaconScreen.this.secondary = menu.getSecondaryEffect();
                NewBeaconScreen.this.tertiary = menu.getTertiaryEffect();
                NewBeaconScreen.this.isEffectsActive = menu.isEffectsActive();
                NewBeaconScreen.this.paymentItem = menu.getPaymentItem();
                NewBeaconScreen.this.upgradeAmplifier = menu.getUpgradeAmplifier();
            }
        });
    }

    private <T extends AbstractWidget & NewBeaconScreen.BeaconButton> void addBeaconButton(T beaconButton) {
        this.addRenderableWidget(beaconButton);
        this.beaconButtons.add(beaconButton);
    }

    @Override
    protected void init() {
        super.init();
        this.beaconButtons.clear();

        this.addBeaconButton(new NewBeaconScreen.BeaconConfirmButton(this.leftPos + 171, this.topPos + 106));
        this.addBeaconButton(new NewBeaconScreen.BeaconCancelButton(this.leftPos + 196, this.topPos + 106));

        // Primary powers
        for(int i = 0; i <= 2; i++) {
            int j = BeaconBlockEntity.BEACON_EFFECTS[i].length;
            int k = j * 22 + (j - 1) * 2;

            for(int l = 0; l < j; l++) {
                MobEffect mobeffect = BeaconBlockEntity.BEACON_EFFECTS[i][l];
                NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton = new NewBeaconScreen.BeaconPowerButton(this.leftPos + 70 + l * 24 - k / 2, this.topPos + 22 + i * 25, mobeffect, true, false, i);
                beaconscreen$beaconpowerbutton.active = false;
                this.addBeaconButton(beaconscreen$beaconpowerbutton);
            }
        }

        // Secondary Powers
        int j1 = BeaconBlockEntity.BEACON_EFFECTS[3].length;
        int k1 = j1 * 22 + (j1 - 1) * 2;

        for(int l1 = 0; l1 < j1; l1++) {
            MobEffect mobeffect1 = BeaconBlockEntity.BEACON_EFFECTS[3][l1];
            NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton2 = new NewBeaconScreen.BeaconPowerButton(this.leftPos + 163 + l1 * 24 - k1 / 2, this.topPos + 47, mobeffect1, false, true, 3);
            beaconscreen$beaconpowerbutton2.active = false;
            this.addBeaconButton(beaconscreen$beaconpowerbutton2);
        }

        // Tertiary Powers
        int j2 = BeaconBlockEntity.BEACON_EFFECTS[4].length;

        for (int i = 0; i < j2; i++){
            MobEffect mobEffect2 = BeaconBlockEntity.BEACON_EFFECTS[4][i];
            NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton3 = new NewBeaconScreen.BeaconPowerButton(this.leftPos + 221, this.topPos +  47 + i * 25, mobEffect2, false, false, 4);
            beaconscreen$beaconpowerbutton3.active = false;
            this.addBeaconButton(beaconscreen$beaconpowerbutton3);
        }

        // Upgrade button (Secondary Effect)
        NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton1 = new NewBeaconScreen.BeaconUpgradePowerButton(this.leftPos + 152, this.topPos + 72, BeaconBlockEntity.BEACON_EFFECTS[0][0]);
        beaconscreen$beaconpowerbutton1.visible = false;
        this.addBeaconButton(beaconscreen$beaconpowerbutton1);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.updateButtons();
    }

    void updateButtons() {
        int i = this.menu.getLevels();
        this.beaconButtons.forEach(button -> button.updateStatus(i));
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawCenteredString(this.font, PRIMARY_EFFECT_LABEL, 59, 10, 14737632);
        guiGraphics.drawCenteredString(this.font, SECONDARY_EFFECT_LABEL, 164, 10, 14737632);
        guiGraphics.drawCenteredString(this.font, BBConstants.TERTIARY_POWER_LABEL, 233, 10, 14737632);
        guiGraphics.drawCenteredString(this.font, BBConstants.BEACON_RANGE_LABEL, 74, 105, 14737632);
        guiGraphics.drawCenteredString(this.font, BBConstants.CURRENT_PAYMENT_LABEL, 239, 106, 14737632);
        guiGraphics.drawCenteredString(this.font, "++", 20, 106, 14737632);
        guiGraphics.drawCenteredString(this.font, "--", 128, 106, 14737632);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(BEACON_TEXTURE_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        guiGraphics.renderItem(new ItemStack(Items.NETHERITE_INGOT), i + 12, j + 114);
        guiGraphics.renderItem(new ItemStack(Items.DIAMOND), i + 33, j + 114);
        guiGraphics.renderItem(new ItemStack(Items.EMERALD), i + 31 + 22, j + 114);
        guiGraphics.renderItem(new ItemStack(Items.GOLD_INGOT), i + 31 + 44, j + 114);
        guiGraphics.renderItem(new ItemStack(Items.IRON_INGOT), i + 31 + 66, j + 114);
        guiGraphics.renderItem(new ItemStack(Items.COPPER_INGOT), i + 31 + 88, j + 114);

        if (this.paymentItem != null)
            guiGraphics.renderItem(new ItemStack(BBUtils.getItemByKey(this.paymentItem)), i + 165 + 66, j + 114);

        guiGraphics.pose().popPose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public interface BeaconButton {
        void updateStatus(int beaconTier);
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    public class BeaconCancelButton extends NewBeaconScreen.BeaconSpriteScreenButton {
        public BeaconCancelButton(int x, int y) {
            super(x, y, 112, 220, CommonComponents.GUI_CANCEL);

            if (BBClientConfigs.ENABLE_CANCEL_BUTTON_TOOLTIP.get())
                if (BBClientConfigs.CANCEL_BUTTON_REMOVE_EFFECTS.get())
                    this.setTooltip(BBConstants.CANCEL_BUTTON_REMOVE_EFFECTS_TOOLTIP);
                else
                    this.setTooltip(BBConstants.CANCEL_BUTTON_CLOSE_CONTAINER_TOOLTIP);
        }

        public void onPress() {
            if (BBClientConfigs.CANCEL_BUTTON_REMOVE_EFFECTS.get())
                BBPacketHandler.sendToServer(new BeaconC2SPacket(Optional.empty(), Optional.empty(), Optional.empty()));
            NewBeaconScreen.this.minecraft.player.closeContainer();
        }

        public void updateStatus(int beaconTier) {
            this.active = (NewBeaconScreen.this.isEffectsActive && NewBeaconScreen.this.primary != null) || !BBClientConfigs.CANCEL_BUTTON_REMOVE_EFFECTS.get();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    public class BeaconConfirmButton extends NewBeaconScreen.BeaconSpriteScreenButton {
        protected BeaconConfirmButton(int x, int y) {
            super(x, y, 90, 220, CommonComponents.GUI_DONE);
            if (BBClientConfigs.ENABLE_CONFIRM_BUTTON_TOOLTIP.get())
                this.setTooltip(BBConstants.CONFIRM_BUTTON_TOOLTIP);
        }

        public void onPress() {
            BBPacketHandler.sendToServer(new BeaconC2SPacket(Optional.ofNullable(NewBeaconScreen.this.primary), Optional.ofNullable(NewBeaconScreen.this.secondary), Optional.ofNullable(NewBeaconScreen.this.tertiary)));
            NewBeaconScreen.this.minecraft.player.closeContainer();
        }

        public void updateStatus(int beaconTier) {
            this.active = NewBeaconScreen.this.menu.hasPayment() && NewBeaconScreen.this.primary != null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BeaconPowerButton extends NewBeaconScreen.BeaconScreenButton {
        private final boolean isPrimary;
        private final boolean isSecondary;
        protected final int tier;
        private MobEffect effect;
        private TextureAtlasSprite sprite;

        public BeaconPowerButton(int x, int y, MobEffect effect, boolean isPrimary, boolean isSecondary, int tier) {
            super(x, y);
            this.isPrimary = isPrimary;
            this.isSecondary = isSecondary;
            this.tier = tier;
            this.setEffect(effect);
        }

        protected void setEffect(MobEffect effect) {
            this.effect = effect;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(effect);
            this.setTooltip(Tooltip.create(this.createEffectDescription(effect), null));
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect effect) {
            return Component.translatable(effect.getDescriptionId());
        }

        public void onPress() {
            if (!this.isSelected()) {
                if (this.isPrimary)
                    NewBeaconScreen.this.primary = this.effect;
                else if (this.isSecondary)
                    NewBeaconScreen.this.secondary = this.effect;
                else
                    NewBeaconScreen.this.tertiary = this.effect;

                NewBeaconScreen.this.updateButtons();
            }
        }

        protected void renderIcon(@NotNull GuiGraphics guiGraphics) {
            guiGraphics.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
        }

        public void updateStatus(int beaconTier) {
            this.active = this.tier < beaconTier;

            if (this.isPrimary)
                this.setSelected(this.effect == NewBeaconScreen.this.primary);
            else if (this.isSecondary)
                this.setSelected(this.effect == NewBeaconScreen.this.secondary);
            else
                this.setSelected(this.effect == NewBeaconScreen.this.tertiary);
        }

        protected @NotNull MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect);
        }
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class BeaconScreenButton extends AbstractButton implements NewBeaconScreen.BeaconButton {
        private boolean selected;

        protected BeaconScreenButton(int x, int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY);
        }

        protected BeaconScreenButton(int x, int y, Component message) {
            super(x, y, 22, 22, message);
        }

        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int j = 0;

            if (!this.active)
                j += this.width * 2;
            else if (this.selected)
                j += this.width;
            else if (this.isHoveredOrFocused())
                j += this.width * 3;

            guiGraphics.blit(NewBeaconScreen.BEACON_TEXTURE_LOCATION, this.getX(), this.getY(), j, 219, this.width, this.height);
            this.renderIcon(guiGraphics);
        }

        protected abstract void renderIcon(GuiGraphics guiGraphics);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class BeaconSpriteScreenButton extends NewBeaconScreen.BeaconScreenButton {
        private final int iconX;
        private final int iconY;

        protected BeaconSpriteScreenButton(int x, int y, int iconX, int iconY, Component component) {
            super(x, y, component);
            this.iconX = iconX;
            this.iconY = iconY;
        }

        protected void renderIcon(@NotNull GuiGraphics guiGraphics) {
            guiGraphics.blit(NewBeaconScreen.BEACON_TEXTURE_LOCATION, this.getX() + 2, this.getY() + 2, this.iconX, this.iconY, 18, 18);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BeaconUpgradePowerButton extends NewBeaconScreen.BeaconPowerButton {
        public BeaconUpgradePowerButton(int x, int y, MobEffect effect) {
            super(x, y, effect, false, true, 3);
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect effect) {
            return Component.translatable(effect.getDescriptionId()).append(" ").append(NumberToRoman.convert(NewBeaconScreen.this.upgradeAmplifier + 1));
        }

        public void updateStatus(int beaconTier) {
            if (NewBeaconScreen.this.primary != null) {
                this.visible = true;
                this.setEffect(NewBeaconScreen.this.primary);
                super.updateStatus(beaconTier);
            } else {
                this.visible = false;
            }
        }
    }
}
