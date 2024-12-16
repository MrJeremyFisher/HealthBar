package ca.favro.healthbar.forge;

import ca.favro.healthbar.HealthBar;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HealthBar.MOD_ID)
public class HealthBarForge {
	private static HealthBar healthBar;

	public HealthBarForge(FMLJavaModLoadingContext fmlJavaModLoadingContext) {
		healthBar = new HealthBar();

		healthBar.init();

		MinecraftForge.EVENT_BUS.register(this);

		fmlJavaModLoadingContext.getModEventBus().addListener(this::registerBindings);
	}

	@SubscribeEvent
	public void registerBindings(RegisterKeyMappingsEvent event) {
		event.register(healthBar.getSettingsKey());
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START)
			healthBar.tick();
	}
}