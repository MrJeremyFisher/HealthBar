package ca.favro.healthbar.fabric;

import ca.favro.healthbar.HealthBar;
import ca.favro.healthbar.gui.screens.MainConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> new MainConfigScreen(HealthBar.getInstance().getConfig());
    }
}

