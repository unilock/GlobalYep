package cc.unilock.globalyep.yep;

import cc.unilock.globalyep.GlobalYep;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;

public class YepListener {
    private final Logger logger;

    public YepListener(Logger logger) {
        this.logger = logger;
        logger.info("YepListener created");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        logger.info("Received plugin message: " + event.toString());

        if (event.getIdentifier().equals(GlobalYep.YepIdentifier) && event.getSource() instanceof ServerConnection connection) {
            String data = new String(event.getData(), StandardCharsets.UTF_8);

            // username:type:message
            var parts = data.split(":");

            if (parts.length != 3) {
                logger.warn("Invalid yep message: " + data);
                return;
            }

            MessageType type = null;
            try {
                type = MessageType.valueOf(parts[1]);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid yep message type: " + parts[0]);
            }

            if (type == null) return;

            var player = parts[0];
            var server = connection.getServer();
            var message = parts[2];

            switch (type) {
                case ADVANCEMENT -> GlobalYep.sendAdvancement(player, server, AdvancementMessage.fromString(message));
                case DEATH -> GlobalYep.sendDeath(player, server, DeathMessage.fromString(message));
                default -> logger.warn("Invalid yep message type: " + parts[0]);
            }
        }
    }
}
