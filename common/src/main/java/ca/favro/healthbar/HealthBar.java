package ca.favro.healthbar;

import ca.favro.healthbar.config.HealthBarConfig;
import ca.favro.healthbar.gui.screens.MainConfigScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.io.File;
import java.util.Random;

public class HealthBar {
	public static final String MOD_ID = "healthbar";

	private final KeyMapping settingsKey;
	private HealthBarConfig healthBarConfig;

	private final ResourceLocation BAR_TEXTURE = new ResourceLocation(MOD_ID, "gui/bar.png");
	private final ResourceLocation BORDER_TEXTURE = new ResourceLocation(MOD_ID, "gui/border.png");

	private final Random random = new Random();
	public HealthBar() {
		settingsKey = new KeyMapping("healthbar.settings.key", GLFW.GLFW_KEY_H, "healthbar.settings.category");
	}

	public void init() {
		File gameDirectory = Minecraft.getInstance().gameDirectory;
		File configDir = new File(gameDirectory, "/config/");
		File configFile = new File(configDir, "healthbar_config.json");

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

	public void render(GuiGraphics guiGraphics) {
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

		RenderSystem.enableBlend();
		poseStack.pushPose();
		poseStack.scale(healthBarConfig.getBarScale(), healthBarConfig.getBarScale(), 1);

		// Setup and draw bar
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BAR_TEXTURE);
		RenderSystem.setShaderColor(healthBarConfig.getHealthBarColor().getRed() / 255.0f, healthBarConfig.getHealthBarColor().getGreen() / 255.0f, healthBarConfig.getHealthBarColor().getBlue() / 255.0f, healthBarConfig.getBarOpacity());

		drawVertexRect(poseStack, x, y, healthBarConfig.getBarHeight(), healthBarConfig.getBarWidth() * (minecraft.player.getHealth() / minecraft.player.getMaxHealth()));

		// Setup and draw border
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BORDER_TEXTURE);
		RenderSystem.setShaderColor(1, 1, 1, healthBarConfig.getBarOpacity());

		drawVertexRect(poseStack, x, y, healthBarConfig.getBarHeight(), healthBarConfig.getBarWidth());

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

		RenderSystem.enableBlend();
	}

	private void drawVertexRect(PoseStack poseStack, float x, float y, float height, float width) {
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder buff = tess.getBuilder();
		Matrix4f mat = poseStack.last().pose();
		buff.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buff.vertex(mat, x, y + height, 0).uv(0, 1).endVertex(); // TL
		buff.vertex(mat, x + width, y + height, 0).uv(1, 1).endVertex(); // TR
		buff.vertex(mat, x + width, y, 0).uv(1, 0).endVertex(); // BR
		buff.vertex(mat, x, y, 0).uv(0, 0).endVertex(); // BL
		tess.end();
	}
}

