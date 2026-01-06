package ca.favro.healthbar.gui.components;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;

public class SliderButton extends AbstractSliderButton {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final float minValue;
    private final float maxValue;
    private final Component content;
    private final boolean integer;
    private float displayedValue;

    public SliderButton(int x, int y, int width, float minValue, float maxValue, Component content, float value, boolean integer) {
        super(x, y, width, 20, content, (value - minValue) / (maxValue - minValue));

        this.maxValue = maxValue;
        this.minValue = minValue;
        this.displayedValue = value;
        this.content = content;
        this.integer = integer;

        this.updateMessage();
    }

    @Override
    public void applyValue() {
        this.displayedValue = integer ? (float) Mth.floor(Mth.clampedLerp(this.value, this.minValue, this.maxValue)) : (float) Mth.clampedLerp(this.value, this.minValue, this.maxValue);
    }


    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%s: %s", content.getString(), decimalFormat.format(this.displayedValue))));
    }

    public void onClick(double d, double e) {
    }

    public void onRelease(double d, double e) {
    }

    public float getValue() {
        return this.displayedValue;
    }
}