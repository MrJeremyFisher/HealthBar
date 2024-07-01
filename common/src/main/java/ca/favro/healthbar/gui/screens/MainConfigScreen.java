package ca.favro.healthbar.gui.screens;

import ca.favro.healthbar.config.HealthBarConfig;
import ca.favro.healthbar.gui.components.SliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class MainConfigScreen extends Screen {
	private final HealthBarConfig healthBarConfig;
	private SliderButton redSlider;
	private SliderButton greenSlider;
	private SliderButton blueSlider;
	private SliderButton opacitySlider;
	private EditBox healthStringFormatField;
	private SliderButton scaleSlider;
	private SliderButton textScaleSlider;
	private SliderButton xOffsetSlider;
	private SliderButton yOffsetSlider;
	private SliderButton widthSlider;
	private SliderButton heightSlider;
	private SliderButton quiverHealthSlider;
	private SliderButton quiverIntensitySlider;

	public MainConfigScreen(HealthBarConfig healthBarConfig) {
		super(Component.translatable("healthbar.screen.main"));
		this.healthBarConfig = healthBarConfig;
	}

	public void init() {
		int y = this.height / 4 - 16;
		int x = this.width / 2 - 100;

		addRenderableWidget(
				redSlider = new SliderButton(x, y, 66, 0f, 1f, Component.translatable("healthbar.screen.slider.red"), healthBarConfig.getHealthBarColor().getRed() / 255f, false)
		);

		addRenderableWidget(
				greenSlider = new SliderButton(x + 66 + 1, y, 66, 0f, 1f, Component.translatable("healthbar.screen.slider.green"), healthBarConfig.getHealthBarColor().getGreen() / 255f, false)
		);

		addRenderableWidget(
				blueSlider = new SliderButton(x + 66 + 1 + 66 + 1, y, 66, 0f, 1f, Component.translatable("healthbar.screen.slider.blue"), healthBarConfig.getHealthBarColor().getBlue() / 255f, false)
		);

		y += 24;

		addRenderableWidget(
				opacitySlider = new SliderButton(x + 66 + 1, y, 132, 0f, 1f, Component.translatable("healthbar.screen.slider.opacity"), healthBarConfig.getBarOpacity(), false)
		);

		y += 24;

		addRenderableWidget(
				scaleSlider = new SliderButton(x, y, 100, 0.1f, 20, Component.translatable("healthbar.screen.slider.scale"), healthBarConfig.getBarScale(), false)
		);
		addRenderableWidget(
				textScaleSlider = new SliderButton(x + 101, y, 100, 0f, 8f, Component.translatable("healthbar.screen.slider.text_scale"), healthBarConfig.getTextScale(), false)
		);

		y += 24;

		addRenderableWidget(
				xOffsetSlider = new SliderButton(x, y, 100, 0f, 1f, Component.translatable("healthbar.screen.slider.offset.x"), healthBarConfig.getxOffset(), false)
		);
		addRenderableWidget(
				yOffsetSlider = new SliderButton(x + 101, y, 100, 0f, 1f, Component.translatable("healthbar.screen.slider.offset.y"), healthBarConfig.getyOffset(), false)
		);

		y += 24;

		addRenderableWidget(
				widthSlider = new SliderButton(x, y, 100, 0f, 128, Component.translatable("healthbar.screen.slider.width"), healthBarConfig.getBarWidth(), true)
		);
		addRenderableWidget(
				heightSlider = new SliderButton(x + 101, y, 100, 0f, 128, Component.translatable("healthbar.screen.slider.height"), healthBarConfig.getBarHeight(), true)
		);

		y += 24;

		addRenderableWidget(Button.builder(Component.translatable("healthbar.screen.toggle", (healthBarConfig.isBarShowAlways() ? "On" : "Off")),
						(btn) -> healthBarConfig.setBarShowAlways(!healthBarConfig.isBarShowAlways()))
				.bounds(x, y, 200, 20)
				.build()
		);

		y += 24;

		addRenderableWidget(
				quiverHealthSlider = new SliderButton(x, y, 100, 0.5f, 20, Component.translatable("healthbar.screen.slider.max_quiver"), healthBarConfig.getBarQuiverHealth(), true)
		);
		addRenderableWidget(
				quiverIntensitySlider = new SliderButton(x + 101, y, 100, 0f, 1f, Component.translatable("healthbar.screen.slider.quiver_intensity"), healthBarConfig.getBarQuiverIntensity(), false)
		);

		y += 24;

		healthStringFormatField = new EditBox(this.font, x + 101, y, 100, 20, CommonComponents.EMPTY);
		healthStringFormatField.setValue(healthBarConfig.getHealthStringFormat());

		addRenderableWidget(
				healthStringFormatField
		);
	}

	@Override
	public void tick() {
		boolean isChanged;

		isChanged = healthBarConfig.setHealthStringFormat(healthStringFormatField.getValue());
		isChanged = healthBarConfig.setTextScale(textScaleSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setxOffset(xOffsetSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setyOffset(yOffsetSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarWidth((int) widthSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarHeight((int) heightSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarScale(scaleSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarOpacity(opacitySlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarQuiverHealth(quiverHealthSlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setBarQuiverIntensity(quiverIntensitySlider.getValue()) || isChanged;
		isChanged = healthBarConfig.setHealthBarColor(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue())) || isChanged;

		if (isChanged) {
			healthBarConfig.save();
		}
	}
}
