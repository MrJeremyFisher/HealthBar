package ca.favro.healthbar.forge;

import ca.favro.healthbar.HealthBar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

@Mod(HealthBar.MOD_ID)
public class HealthBarNeoForge {
	private static HealthBar healthBar;

	public HealthBarNeoForge(IEventBus bus) {
		healthBar = new HealthBar();

		healthBar.init();

		NeoForge.EVENT_BUS.register(this);

		bus.addListener(this::registerBindings);
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
			healthBar.render(event.getGuiGraphics());
	}
}
