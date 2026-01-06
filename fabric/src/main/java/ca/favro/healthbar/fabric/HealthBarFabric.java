package ca.favro.healthbar.fabric;

import ca.favro.healthbar.HealthBar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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

        KeyBindingHelper.registerKeyBinding(healthBar.getSettingsKey());

        ClientLifecycleEvents.CLIENT_STARTED.register(e -> init());
        HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR,
                Identifier.fromNamespaceAndPath("healthbar", "bar"),
                healthBar::render
        );
    }
}