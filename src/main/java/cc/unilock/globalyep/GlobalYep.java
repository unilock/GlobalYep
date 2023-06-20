package cc.unilock.globalyep;

import cc.unilock.globalyep.config.Config;
import cc.unilock.globalyep.config.Loader;
import cc.unilock.globalyep.util.Constants;
import cc.unilock.globalyep.util.Libraries;
import cc.unilock.globalyep.util.Placeholders;
import cc.unilock.globalyep.yep.AdvancementMessage;
import cc.unilock.globalyep.yep.DeathMessage;
import cc.unilock.globalyep.yep.YepListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "globalyep",
        name = "GlobalYep",
        description = "Global advancements / death messages",
        version = Constants.VERSION,
        authors = {"unilock"},
        dependencies = {@Dependency(id = "miniplaceholders", optional = true)}
)
public final class GlobalYep {
    public static final MinecraftChannelIdentifier YepIdentifier = MinecraftChannelIdentifier.create("velocity", "yep");

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path path;
    private Config config;

    private final PluginManager pluginManager;

    public static GlobalYep instance;

    @Inject
    public GlobalYep(ProxyServer proxy, Logger logger, @DataDirectory Path path) {
        this.logger = logger;
        this.proxy = proxy;
        this.path = path;

        this.pluginManager = proxy.getPluginManager();

        logger.info("Hello from GlobalYep!");

        instance = this;
    }

    public static GlobalYep getPlugin() {
        return instance;
    }

    @Subscribe
    void onProxyInitialization(final ProxyInitializeEvent event) {
        Libraries.load(this, logger, path, proxy.getPluginManager());
        this.config = Loader.loadConfig(logger, path);
        if (this.config == null) {
            return;
        }

        register(new YepListener(logger));
        proxy.getChannelRegistrar().register(YepIdentifier);

        logger.info("GlobalYep initialized");
    }

    public static void sendAdvancement(String player, RegisteredServer server, AdvancementMessage yepMessage) {
        if (getPlugin().config.getAdvEnabled())
            getPlugin().sendMessage(
                    Placeholders.advComponent(
                            getPlugin().config,
                            getPlugin().pluginManager,
                            player,
                            server.getServerInfo().getName(),
                            yepMessage.title,
                            yepMessage.description
                    ),
                    server,
                    getPlugin().config.getAdvPassthrough()
            );
    }

    public static void sendDeath(String player, RegisteredServer server, DeathMessage yepMessage) {
        if (getPlugin().config.getDeathEnabled())
            getPlugin().sendMessage(
                    Placeholders.deathComponent(
                            getPlugin().config,
                            getPlugin().pluginManager,
                            player,
                            server.getServerInfo().getName(),
                            yepMessage.message
                    ),
                    server,
                    getPlugin().config.getDeathPassthrough()
            );
    }

    private void sendMessage(Component msg, RegisteredServer excludedServer, boolean passthrough) {
        if (passthrough) sendMessage(msg);
        else sendMessage(msg, excludedServer);
    }

    private void sendMessage(Component msg) {
        proxy.sendMessage(msg);
    }

    private void sendMessage(Component msg, RegisteredServer excludedServer) {
        for (RegisteredServer server : proxy.getAllServers())
            if (server != excludedServer) {
                server.sendMessage(msg);
            }
    }

    private void register(Object x) {
        proxy.getEventManager().register(this, x);
    }
}
