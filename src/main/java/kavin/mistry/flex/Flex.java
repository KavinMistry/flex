package kavin.mistry.flex;

import org.bukkit.plugin.java.JavaPlugin;

public final class Flex extends JavaPlugin {

    @Override
    public void onEnable()
    {
        getCommand("show").setExecutor(new show());
    }

    @Override
    public void onDisable()
    {

    }
}
