package ca.favro.healthbar.forge;

import ca.favro.healthbar.HealthBar;
import ca.favro.healthbar.gui.screens.MainConfigScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HealthBar.MOD_ID)
public class HealthBarForge {
    private static HealthBar healthBar;

    public HealthBarForge(FMLJavaModLoadingContext fmlJavaModLoadingContext) {
        healthBar = new HealthBar();

        healthBar.init();

        AddGuiOverlayLayersEvent.BUS.addListener(this::initOverlays);

        MinecraftForge.EVENT_BUS.register(this);

        RegisterKeyMappingsEvent.BUS.addListener(this::registerBindings);

        fmlJavaModLoadingContext.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (parent) -> new MainConfigScreen(healthBar.getConfig())
                )
        );
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(healthBar.getSettingsKey());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent.Pre event) {
        healthBar.tick();
    }


    public void initOverlays(AddGuiOverlayLayersEvent event) {
        ForgeLayeredDraw lDraw = new ForgeLayeredDraw(ResourceLocation.fromNamespaceAndPath("healthbar", "forgelayer"));
        lDraw.add(lDraw.getName(),
                (arg, arg2) -> HealthBar.getInstance().render(arg, arg2)
        );

        event.getLayeredDraw().add(lDraw.getName(), lDraw, () -> true);
        event.getLayeredDraw().move(lDraw.getName(), ForgeLayeredDraw.SUBTITLE_OVERLAY, ForgeLayeredDraw.LayerOffset.ABOVE);
    }

}