package ca.favro.healthbar.fabric;

import ca.favro.healthbar.HealthBar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

public class HealthBarFabric implements ClientModInitializer {
    private static HealthBar healthBar;

    private void init() {
        healthBar.init();

        ClientTickEvents.START_CLIENT_TICK.register(e -> healthBar.tick());
    }

    @Override
    public void onInitializeClient() {
        healthBar = new HealthBar();

        KeyMappingHelper.registerKeyMapping(healthBar.getSettingsKey());

        ClientLifecycleEvents.CLIENT_STARTED.register(e -> init());
        HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR,
                Identifier.fromNamespaceAndPath("healthbar", "bar"),
                healthBar::render
        );
    }
}