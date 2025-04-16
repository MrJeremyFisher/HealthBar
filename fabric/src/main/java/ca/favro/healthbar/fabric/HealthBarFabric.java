package ca.favro.healthbar.fabric;

import ca.favro.healthbar.HealthBar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.resources.ResourceLocation;

public class HealthBarFabric implements ClientModInitializer {
    private static HealthBar healthBar;

    private void init() {
        healthBar.init();

        ClientTickEvents.START_CLIENT_TICK.register(e -> healthBar.tick());
    }

    @Override
    public void onInitializeClient() {
        healthBar = new HealthBar();

        KeyBindingHelper.registerKeyBinding(healthBar.getSettingsKey());

        ClientLifecycleEvents.CLIENT_STARTED.register(e -> init());
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerAfter(IdentifiedLayer.EXPERIENCE_LEVEL,
                ResourceLocation.fromNamespaceAndPath("healthbar", "bar"),
                healthBar::render));
    }
}