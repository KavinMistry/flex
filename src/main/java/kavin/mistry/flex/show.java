package kavin.mistry.flex;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class show implements TabExecutor, CommandExecutor {

    private static final Set<Material> rare = EnumSet.of(
            Material.CREEPER_BANNER_PATTERN, Material.SKULL_BANNER_PATTERN, Material.EXPERIENCE_BOTTLE,
            Material.DRAGON_BREATH, Material.ELYTRA, Material.HEART_OF_THE_SEA, Material.NETHER_STAR,
            Material.TOTEM_OF_UNDYING, Material.FLOW_BANNER_PATTERN, Material.PIGLIN_BANNER_PATTERN,
            Material.GUSTER_BANNER_PATTERN, Material.GLOBE_BANNER_PATTERN, Material.FLOWER_BANNER_PATTERN
    );

    private static final Set<Material> epic = EnumSet.of(
            Material.MOJANG_BANNER_PATTERN, Material.DRAGON_EGG,
            Material.ENCHANTED_GOLDEN_APPLE, Material.HEAVY_CORE
    );
    private static final Logger log = LoggerFactory.getLogger(show.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {

        if(!(sender instanceof Player player))
        {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() == Material.AIR || item.getAmount() <= 0)
        {
            sender.sendMessage(Component.text("You are not holding anything!", NamedTextColor.RED));
            return true;
        }

        TextColor color = NamedTextColor.WHITE;

        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();

            if(meta.hasRarity())
            {
                ItemRarity rarity = meta.getRarity();
                if (rarity == ItemRarity.UNCOMMON) color = NamedTextColor.YELLOW;
                else if (rarity == ItemRarity.RARE) color = NamedTextColor.AQUA;
                else if (rarity == ItemRarity.EPIC) color = NamedTextColor.LIGHT_PURPLE;
            }
            else
            {
                Material mat = item.getType();
                boolean isEnchanted = meta.hasEnchants();

                if(epic.contains(mat))
                {
                    color = NamedTextColor.LIGHT_PURPLE;
                }
                else if(rare.contains(mat))
                {
                    color = NamedTextColor.YELLOW;
                }
                else if(mat == Material.BEACON || mat == Material.CONDUIT || mat == Material.GOLDEN_APPLE || isEnchanted)
                {
                    color = NamedTextColor.AQUA;
                }
            }
        }

        player.playSound(player, Sound.UI_TOAST_IN, 1f, 1.5f);

        Component itemDisplay = Component.empty()
                .append(item.displayName())
                .color(color)
                .hoverEvent(item.asHoverEvent(data -> data));

        Component msg = Component.text()
                .append(Component.text(player.getName(), NamedTextColor.GRAY))
                .append(Component.text(" is holding ", NamedTextColor.WHITE))
                .append(Component.text(item.getAmount(), NamedTextColor.GRAY))
                .append(Component.text("x ", NamedTextColor.DARK_GRAY))
                .append(itemDisplay)
                .build();

        Bukkit.broadcast(msg);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}