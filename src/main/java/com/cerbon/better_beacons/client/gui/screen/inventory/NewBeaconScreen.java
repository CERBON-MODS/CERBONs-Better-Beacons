package com.cerbon.better_beacons.client.gui.screen.inventory;

import com.cerbon.better_beacons.config.BBClientConfigs;
import com.cerbon.better_beacons.config.BBCommonConfigs;
import com.cerbon.better_beacons.menu.custom.NewBeaconMenu;
import com.cerbon.better_beacons.packet.BBPacketHandler;
import com.cerbon.better_beacons.packet.custom.BeaconC2SPacket;
import com.cerbon.better_beacons.util.BBConstants;
import com.cerbon.better_beacons.util.BBUtils;
import com.cerbon.better_beacons.util.NumberToRoman;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
    private static final ResourceLocation BEACON_TEXTURE_LOCATION = new ResourceLocation(NewBeaconMenu.isTertiaryEffectsEnabled ? "textures/gui/container/beacon2.png" : "textures/gui/container/beacon1.png");
    public static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");
    public static final Component SECONDARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.secondary");
    private final List<NewBeaconScreen.BeaconButton> beaconButtons = Lists.newArrayList();
    @Nullable MobEffect primary;
    @Nullable MobEffect secondary;
    @Nullable MobEffect tertiary;
    boolean isEffectsActive;
    String paymentItem;
    int primaryEffectAmplifier;

    public NewBeaconScreen(NewBeaconMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = NewBeaconMenu.isTertiaryEffectsEnabled ? 256 : 230;
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
                NewBeaconScreen.this.primaryEffectAmplifier = menu.getPrimaryEffectAmplifier();
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

        this.addBeaconButton(new NewBeaconScreen.BeaconConfirmButton(NewBeaconMenu.isTertiaryEffectsEnabled ? this.leftPos + 171 : this.leftPos + 172, this.topPos + 106));
        this.addBeaconButton(new NewBeaconScreen.BeaconCancelButton(NewBeaconMenu.isTertiaryEffectsEnabled ? this.leftPos + 196 : this.leftPos + 197, this.topPos + 106));

        // Primary powers
        for(int i = 0; i <= 2; i++) {
            int j = BeaconBlockEntity.BEACON_EFFECTS[i].length;
            int k = j * 22 + (j - 1) * 2;

            for(int l = 0; l < j; l++) {
                MobEffect mobeffect = BeaconBlockEntity.BEACON_EFFECTS[i][l];
                NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton = new NewBeaconScreen.BeaconPowerButton(NewBeaconMenu.isTertiaryEffectsEnabled ? this.leftPos + 74 + l * 24 - k / 2 : this.leftPos + 76 + l * 24 - k / 2, this.topPos + 22 + i * 25, mobeffect, true, false, i);
                beaconscreen$beaconpowerbutton.active = false;
                this.addBeaconButton(beaconscreen$beaconpowerbutton);
            }
        }

        // Secondary Powers
        int j1 = BeaconBlockEntity.BEACON_EFFECTS[3].length;
        int k1 = j1 * 22 + (j1 - 1) * 2;

        for(int l1 = 0; l1 < j1; l1++) {
            MobEffect mobeffect1 = BeaconBlockEntity.BEACON_EFFECTS[3][l1];
            NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton2 = new NewBeaconScreen.BeaconPowerButton(NewBeaconMenu.isTertiaryEffectsEnabled ? this.leftPos + 164 + l1 * 24 - k1 / 2 : this.leftPos + 168 + l1 * 24 - k1 / 2, this.topPos + 47, mobeffect1, false, true, 3);
            beaconscreen$beaconpowerbutton2.active = false;
            this.addBeaconButton(beaconscreen$beaconpowerbutton2);
        }

        // Tertiary Powers
        if (NewBeaconMenu.isTertiaryEffectsEnabled){
            int j2 = BeaconBlockEntity.BEACON_EFFECTS[4].length;

            for (int i = 0; i < j2; i++){
                MobEffect mobEffect2 = BeaconBlockEntity.BEACON_EFFECTS[4][i];
                NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton3 = new NewBeaconScreen.BeaconPowerButton(this.leftPos + 222, this.topPos +  47 + i * 25, mobEffect2, false, false, 4);
                beaconscreen$beaconpowerbutton3.active = false;
                this.addBeaconButton(beaconscreen$beaconpowerbutton3);
            }
        }

        // Upgrade button (Secondary Effect)
        NewBeaconScreen.BeaconPowerButton beaconscreen$beaconpowerbutton1 = new NewBeaconScreen.BeaconUpgradePowerButton(NewBeaconMenu.isTertiaryEffectsEnabled ? this.leftPos + 153 : this.leftPos + 156, this.topPos + 72, BeaconBlockEntity.BEACON_EFFECTS[0][0]);
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
    protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        drawCenteredString(poseStack, this.font, PRIMARY_EFFECT_LABEL, NewBeaconMenu.isTertiaryEffectsEnabled ? 60 : 63, 10, 14737632);
        drawCenteredString(poseStack, this.font, SECONDARY_EFFECT_LABEL, NewBeaconMenu.isTertiaryEffectsEnabled ? 165 : 170, 10, 14737632);

        if (BBCommonConfigs.ENABLE_PAYMENT_ITEM_RANGE.get()){
            drawCenteredString(poseStack, this.font, BBConstants.BEACON_RANGE_LABEL, NewBeaconMenu.isTertiaryEffectsEnabled ? 74 : 77, 105, 14737632);
            drawCenteredString(poseStack, this.font, "++", NewBeaconMenu.isTertiaryEffectsEnabled ? 20 : 23, 106, 14737632);
            drawCenteredString(poseStack, this.font, "--", NewBeaconMenu.isTertiaryEffectsEnabled ? 128 : 131, 106, 14737632);
        }else
            drawCenteredString(poseStack, this.font, BBConstants.PAYMENT_ITEM_LABEL, NewBeaconMenu.isTertiaryEffectsEnabled ? 74 : 77, 105, 14737632);

        if (NewBeaconMenu.isTertiaryEffectsEnabled){
            drawCenteredString(poseStack, this.font, BBConstants.TERTIARY_POWER_LABEL, 233, 10, 14737632);
            drawCenteredString(poseStack, this.font, BBConstants.CURRENT_PAYMENT_LABEL, 239, 106, 14737632);
        }

        for (NewBeaconScreen.BeaconButton beaconscreen$beaconbutton : this.beaconButtons){
            if (beaconscreen$beaconbutton.isShowingTooltip()){
                beaconscreen$beaconbutton.renderToolTip1(poseStack, mouseX - this.leftPos, mouseY - this.topPos);
                break;
            }
        }
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BEACON_TEXTURE_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.NETHERITE_INGOT), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 12 : i + 14, j + 114);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.DIAMOND), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 33 : i + 35, j + 114);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.EMERALD), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 31 + 22 : i + 33 + 22, j + 114);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.GOLD_INGOT), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 31 + 44 : i + 33 + 44, j + 114);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.IRON_INGOT), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 31 + 66 : i + 34 + 66, j + 114);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.COPPER_INGOT), NewBeaconMenu.isTertiaryEffectsEnabled ? i + 31 + 88 : i + 34 + 88, j + 114);
        this.itemRenderer.blitOffset = 0.0F;

        if (this.paymentItem != null && NewBeaconMenu.isTertiaryEffectsEnabled)
            this.itemRenderer.renderAndDecorateItem(new ItemStack(BBUtils.getItemByKey(this.paymentItem)), i + 165 + 66, j + 114);

        poseStack.popPose();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @OnlyIn(Dist.CLIENT)
    public interface BeaconButton {
        boolean isShowingTooltip();

        void renderToolTip1(PoseStack poseStack, int relativeMouseX, int relativeMouseY);

        void updateStatus(int beaconTier);
    }

    @SuppressWarnings("DataFlowIssue")
    @OnlyIn(Dist.CLIENT)
    public class BeaconCancelButton extends NewBeaconScreen.BeaconSpriteScreenButton {
        public BeaconCancelButton(int x, int y) {
            super(x, y, 112, 220, CommonComponents.GUI_CANCEL);
        }

        @Override
        public void renderToolTip1(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            if (BBClientConfigs.ENABLE_CANCEL_BUTTON_TOOLTIP.get())
                if (BBClientConfigs.CANCEL_BUTTON_REMOVE_EFFECTS.get())
                    NewBeaconScreen.this.renderTooltip(poseStack, BBConstants.CANCEL_BUTTON_REMOVE_EFFECTS_TOOLTIP, mouseX, mouseY);
                else
                    NewBeaconScreen.this.renderTooltip(poseStack, BBConstants.CANCEL_BUTTON_CLOSE_CONTAINER_TOOLTIP, mouseX, mouseY);
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
        }

        @Override
        public void renderToolTip1(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            if (BBClientConfigs.ENABLE_CONFIRM_BUTTON_TOOLTIP.get())
                NewBeaconScreen.this.renderTooltip(poseStack, BBConstants.CONFIRM_BUTTON_TOOLTIP, mouseX, mouseY);
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
        private Component tooltip;

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
            this.tooltip = this.createEffectDescription(effect);
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect effect) {
            MutableComponent component = Component.translatable(effect.getDescriptionId());

            if (this.isPrimary && NewBeaconScreen.this.primaryEffectAmplifier > 0)
                component.append(" " + NumberToRoman.convert(NewBeaconScreen.this.primaryEffectAmplifier + 1));

            return component;
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

        @Override
        public void renderToolTip1(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            NewBeaconScreen.this.renderTooltip(poseStack, this.tooltip, mouseX, mouseY);
        }

        protected void renderIcon(@NotNull PoseStack poseStack) {
            RenderSystem.setShaderTexture(0, this.sprite.atlas().location());
            blit(poseStack, this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
        }

        public void updateStatus(int beaconTier) {
            this.active = this.tier < beaconTier;
            this.tooltip = createEffectDescription(effect);

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

        public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BEACON_TEXTURE_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int j = 0;

            if (!this.active)
                j += this.width * 2;
            else if (this.selected)
                j += this.width;
            else if (this.isHoveredOrFocused())
                j += this.width * 3;

            blit(poseStack, this.x, this.y, j, 219, this.width, this.height);
            this.renderIcon(poseStack);
        }

        protected abstract void renderIcon(PoseStack poseStack);

        public boolean isSelected() {
            return this.selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isShowingTooltip() {
            return this.isHovered;
        }

        public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract class BeaconSpriteScreenButton extends NewBeaconScreen.BeaconScreenButton {
        private final int iconX;
        private final int iconY;

        protected BeaconSpriteScreenButton(int x, int y, int iconX, int iconY, Component component) {
            super(x, y, component);
            this.iconX = iconX;
            this.iconY = iconY;
        }

        protected void renderIcon(@NotNull PoseStack poseStack) {
            blit(poseStack, this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
        }

        @Override
        public void renderToolTip1(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
            NewBeaconScreen.this.renderTooltip(poseStack, NewBeaconScreen.this.title, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class BeaconUpgradePowerButton extends NewBeaconScreen.BeaconPowerButton {
        public BeaconUpgradePowerButton(int x, int y, MobEffect effect) {
            super(x, y, effect, false, true, 3);
        }

        protected MutableComponent createEffectDescription(@NotNull MobEffect effect) {
            return Component.translatable(effect.getDescriptionId()).append(" " + NumberToRoman.convert(NewBeaconScreen.this.primaryEffectAmplifier + 2));
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
