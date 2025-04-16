package ca.favro.healthbar;

import ca.favro.healthbar.config.HealthBarConfig;
import ca.favro.healthbar.gui.screens.MainConfigScreen;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class HealthBar {
    public static final String MOD_ID = "healthbar";

    private final KeyMapping settingsKey;
    private HealthBarConfig healthBarConfig;
    private static HealthBar instance;

    private final ResourceLocation BAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "gui/bar.png");
    private final ResourceLocation BORDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "gui/border.png");
    private final Random random = new Random();

    public HealthBar() {
        settingsKey = new KeyMapping("healthbar.settings.key", GLFW.GLFW_KEY_H, "healthbar.settings.category");
        instance = this;
    }

    public void init() {
        File gameDirectory = Minecraft.getInstance().gameDirectory;
        File configDir = new File(gameDirectory, "/config/");
        File configFile = new File(configDir, "healthbar_config.json");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        healthBarConfig = new HealthBarConfig(configFile);

        healthBarConfig.load();
    }

    public KeyMapping getSettingsKey() {
        return settingsKey;
    }

    public void tick() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null)
            return;

        if (!minecraft.options.hideGui && minecraft.screen == null && getSettingsKey().consumeClick()) {
            minecraft.setScreen(new MainConfigScreen(healthBarConfig));
        }
    }

    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        PoseStack poseStack = guiGraphics.pose();
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.options.hideGui
                || minecraft.player.isCreative()
                || !healthBarConfig.isEnabled()
                || (!healthBarConfig.isBarShowAlways() && minecraft.player.getHealth() >= minecraft.player.getMaxHealth()))
            return;

        float quiverIntensity = 0;

        if (minecraft.player.getHealth() < healthBarConfig.getBarQuiverHealth()) {
            quiverIntensity = (healthBarConfig.getBarQuiverHealth() - (minecraft.player.getHealth() / minecraft.player.getMaxHealth())) * healthBarConfig.getBarQuiverIntensity() / healthBarConfig.getBarQuiverHealth();
        }

        int x = (int) (minecraft.getWindow().getGuiScaledWidth() / healthBarConfig.getBarScale() * healthBarConfig.getxOffset() - ((float) healthBarConfig.getBarWidth() / 2) + (random.nextGaussian() * quiverIntensity));
        int y = (int) (minecraft.getWindow().getGuiScaledHeight() / healthBarConfig.getBarScale() * healthBarConfig.getyOffset() + (random.nextGaussian() * quiverIntensity));

        poseStack.pushPose();
        poseStack.scale(healthBarConfig.getBarScale(), healthBarConfig.getBarScale(), 1);

        // Setup and draw bar
        int barColor = ARGB.colorFromFloat(healthBarConfig.getBarOpacity(), healthBarConfig.getHealthBarColor().getRed() / 255.0f, healthBarConfig.getHealthBarColor().getGreen() / 255.0f, healthBarConfig.getHealthBarColor().getBlue() / 255.0f);
        drawVertexRect(guiGraphics, poseStack, BAR_TEXTURE, x, y, healthBarConfig.getBarHeight(), healthBarConfig.getBarWidth() * (minecraft.player.getHealth() / minecraft.player.getMaxHealth()), barColor);

        // Setup and draw border
        int borderColor = ARGB.colorFromFloat(healthBarConfig.getBarOpacity(), 1, 1, 1);
        drawVertexRect(guiGraphics, poseStack, BORDER_TEXTURE, x, y, healthBarConfig.getBarHeight(), healthBarConfig.getBarWidth(), borderColor);

        poseStack.popPose();

        // Setup and draw text
        if (healthBarConfig.getTextScale() > 0) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            poseStack.pushPose();
            poseStack.scale(healthBarConfig.getTextScale(), healthBarConfig.getTextScale(), 1);

            String healthString = String.format(healthBarConfig.getHealthStringFormat(), minecraft.player.getHealth(), minecraft.player.getMaxHealth());

            x = (int) (minecraft.getWindow().getGuiScaledWidth() / healthBarConfig.getTextScale() * healthBarConfig.getxOffset() - (float) minecraft.font.width(healthString) / 2);
            y = (int) (minecraft.getWindow().getGuiScaledHeight() / healthBarConfig.getTextScale() * healthBarConfig.getyOffset() - 2) - healthBarConfig.getBarHeight() / 2;

            guiGraphics.drawString(minecraft.font, healthString,
                    x,
                    y,
                    Color.WHITE.getRGB(),
                    true
            );

            poseStack.popPose();
        }
    }

    private void drawVertexRect(GuiGraphics guiGraphics, PoseStack poseStack, ResourceLocation resourceLocation, float x, float y, float height, float width, int color) {
        // This is effectively GuiGraphics.innerBlit because there isn't a public method with floats. Grrrr
        guiGraphics.drawSpecial(bufferSource -> {
            RenderType renderType = RenderType.guiTexturedOverlay(resourceLocation);
            Matrix4f matrix4f = poseStack.last().pose();
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            vertexConsumer.addVertex(matrix4f, x, y, 0).setUv(0, 0).setColor(color);
            vertexConsumer.addVertex(matrix4f, x, y + height, 0).setUv(0, 1).setColor(color);
            vertexConsumer.addVertex(matrix4f, x + width, y + height, 0).setUv(1, 1).setColor(color);
            vertexConsumer.addVertex(matrix4f, x + width, y, 0).setUv(1, 0).setColor(color);
        });
    }

    public static HealthBar getInstance() {
        return instance;
    }
}

