package ca.favro.healthbar.config;

import com.google.gson.Gson;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class HealthBarConfig {
    private final File configFile;
    private boolean enabled = true;
    private String healthStringFormat = "%.1f / %.1f";
    private float textScale = 0.8f;
    private float xOffset = 0.5f; // X and Y offsets are a % of screen width/height
    private float yOffset = 0.75f;
    private int barWidth = 64;
    private int barHeight = 8;
    private float barScale = 1.0f;
    private boolean barShowAlways = true;
    private float barOpacity = 0.6f;
    private float barQuiverHealth = 0.25f;
    private float barQuiverIntensity = 1.0f;
    private Color healthBarColor = new Color(1f, 0.1f, 0.1f);

    public HealthBarConfig(File file) {
        configFile = file;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
    }

    public File getConfigFile() {
        return configFile;
    }

    public String getHealthStringFormat() {
        return healthStringFormat;
    }

    public boolean setHealthStringFormat(String healthStringFormat) {
        if (Objects.equals(this.healthStringFormat, healthStringFormat)) {
            return false;
        }

        this.healthStringFormat = healthStringFormat;
        return true;
    }

    public float getTextScale() {
        return textScale;
    }

    public boolean setTextScale(float textScale) {
        if (this.textScale == textScale) {
            return false;
        }

        this.textScale = textScale;
        return true;
    }

    public float getxOffset() {
        return xOffset;
    }

    public boolean setxOffset(float xOffset) {
        if (this.xOffset == xOffset) {
            return false;
        }

        this.xOffset = xOffset;
        return true;
    }

    public float getyOffset() {
        return yOffset;
    }

    public boolean setyOffset(float yOffset) {
        if (this.yOffset == yOffset) {
            return false;
        }

        this.yOffset = yOffset;

        return true;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public boolean setBarWidth(int barWidth) {
        if (this.barWidth == barWidth) {
            return false;
        }

        this.barWidth = barWidth;

        return true;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public boolean setBarHeight(int barHeight) {
        if (this.barHeight == barHeight) {
            return false;
        }

        this.barHeight = barHeight;

        return true;
    }

    public float getBarScale() {
        return barScale;
    }

    public boolean setBarScale(float barScale) {
        if (this.barScale == barScale) {
            return false;
        }

        this.barScale = barScale;

        return true;
    }

    public boolean isBarShowAlways() {
        return barShowAlways;
    }

    public void setBarShowAlways(boolean barShowAlways) {
        if (this.barShowAlways == barShowAlways) {
            return;
        }

        this.barShowAlways = barShowAlways;
    }

    public float getBarOpacity() {
        return barOpacity;
    }

    public boolean setBarOpacity(float barOpacity) {
        if (this.barOpacity == barOpacity) {
            return false;
        }

        this.barOpacity = barOpacity;

        return true;
    }

    public float getBarQuiverHealth() {
        return barQuiverHealth;
    }

    public boolean setBarQuiverHealth(float barQuiverHealth) {
        if (this.barQuiverHealth == barQuiverHealth) {
            return false;
        }

        this.barQuiverHealth = barQuiverHealth;

        return true;
    }

    public float getBarQuiverIntensity() {
        return barQuiverIntensity;
    }

    public boolean setBarQuiverIntensity(float barQuiverIntensity) {
        if (this.barQuiverIntensity == barQuiverIntensity) {
            return false;
        }

        this.barQuiverIntensity = barQuiverIntensity;

        return true;
    }

    public Color getHealthBarColor() {
        return healthBarColor;
    }

    public boolean setHealthBarColor(Color healthBarColor) {
        if (Objects.equals(this.healthBarColor, healthBarColor)) {
            return false;
        }

        this.healthBarColor = healthBarColor;

        return true;
    }

    public void save() {
        Config config = new Config();

        config.enabled = this.isEnabled();
        config.healthStringFormat = this.getHealthStringFormat();
        config.textScale = this.getTextScale();
        config.xOffset = this.getxOffset();
        config.yOffset = this.getyOffset();
        config.barWidth = this.getBarWidth();
        config.barHeight = this.getBarHeight();
        config.barScale = this.getBarScale();
        config.barShowAlways = this.isBarShowAlways();
        config.barOpacity = this.getBarOpacity();
        config.barQuiverHealth = this.getBarQuiverHealth();
        config.barQuiverIntensity = this.getBarQuiverIntensity();
        config.color = this.getHealthBarColor().getRGB();

        Gson gson = new Gson();
        String json = gson.toJson(config);

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean load() {
        Config config = null;

        try (FileReader reader = new FileReader(configFile)) {
            config = new Gson().fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (config == null) return false;

        this.setEnabled(config.enabled);
        this.setHealthStringFormat(config.healthStringFormat);
        this.setTextScale(config.textScale);
        this.setxOffset(config.xOffset);
        this.setyOffset(config.yOffset);
        this.setBarWidth(config.barWidth);
        this.setBarHeight(config.barHeight);
        this.setBarScale(config.barScale);
        this.setBarShowAlways(config.barShowAlways);
        this.setBarOpacity(config.barOpacity);
        this.setBarQuiverHealth(config.barQuiverHealth);
        this.setBarQuiverIntensity(config.barQuiverIntensity);
        this.setHealthBarColor(new Color(config.color));

        return true;
    }

    private static class Config {
        public boolean enabled;
        public String healthStringFormat;
        public float textScale;
        public float xOffset;
        public float yOffset;
        public int barWidth;
        public int barHeight;
        public float barScale;
        public boolean barShowAlways;
        public float barOpacity;
        public float barQuiverHealth;
        public float barQuiverIntensity;
        public int color;
    }
}
