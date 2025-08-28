package ca.favro.healthbar.forge;

import ca.favro.healthbar.HealthBar;
import ca.favro.healthbar.gui.screens.MainConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

@Mod(HealthBar.MOD_ID)
public class HealthBarNeoForge {
    private static HealthBar healthBar;

    public HealthBarNeoForge(IEventBus bus, ModContainer modContainer) {
        healthBar = new HealthBar();

        NeoForge.EVENT_BUS.register(this);

        bus.addListener(this::registerBindings);

        bus.addListener(FMLClientSetupEvent.class, (clientSetupEvent) -> {
            healthBar.init();

            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> new MainConfigScreen(healthBar.getConfig()));
        });
    }

    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(healthBar.getSettingsKey());
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Pre event) {
        healthBar.tick();
    }

    @SubscribeEvent
    public void onRender(RenderGuiLayerEvent.Post event) {
        if (event.getName() == VanillaGuiLayers.PLAYER_HEALTH)
            healthBar.render(event.getGuiGraphics(), event.getPartialTick());
    }
}
