package cc.unilock.globalyep.util;

import cc.unilock.globalyep.config.Config;
import com.velocitypowered.api.plugin.PluginManager;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class Placeholders {
    public static Component advComponent(Config config, PluginManager pluginManager, @NotNull String player, @NotNull String server, String title, String description) {
        final TagResolver.Builder builder = TagResolver.builder().resolvers(
                Placeholder.unparsed("player", player),
                Placeholder.unparsed("server", server),
                Placeholder.unparsed("adv_title", title),
                Placeholder.unparsed("adv_desc", description)
        );

        if (pluginManager.isLoaded("miniplaceholders")) {
            builder.resolver(MiniPlaceholders.getGlobalPlaceholders());
        }
        final TagResolver resolver = builder.build();

        return miniMessage().deserialize(config.getAdvFormat(), resolver);
    }

    public static Component deathComponent(Config config, PluginManager pluginManager, @NotNull String player, @NotNull String server, String message) {
        final TagResolver.Builder builder = TagResolver.builder().resolvers(
                Placeholder.unparsed("player", player),
                Placeholder.unparsed("server", server),
                Placeholder.unparsed("death_msg", message)
        );

        if (pluginManager.isLoaded("miniplaceholders")) {
            builder.resolver(MiniPlaceholders.getGlobalPlaceholders());
        }
        final TagResolver resolver = builder.build();

        return miniMessage().deserialize(config.getDeathFormat(), resolver);
    }
}
