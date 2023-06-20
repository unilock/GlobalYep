package cc.unilock.globalyep.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@SuppressWarnings("ALL")
@ConfigSerializable
public class Config {
    @Comment("Whether Advancement messages are enabled")
    private boolean advEnabled = true;
    @Comment("Whether Advancement messages should be sent to the server sending the original message (will usually cause duplicate messages in that server's chat)")
    private boolean advPassthrough = false;
    @Comment("Advancement message to send, formatted using MiniMessage")
    private String advFormat = "<gray>[<server>]</gray> <player> has made the advancement <green><hover:show_text:'<adv_title><newline><adv_desc>'>[<adv_title>]</hover></green>";

    @Comment("Whether Death messages are enabled")
    private boolean deathEnabled = true;
    @Comment("Whether Death messages should be sent to the server sending the original message (will usually cause duplicate messages in that server's chat)")
    private boolean deathPassthrough = false;
    @Comment("Death message to send, formatted using MiniMessage")
    private String deathFormat = "<gray>[<server>]</gray> <player> <death_msg>";

    public boolean getAdvEnabled() {
        return advEnabled;
    }

    public boolean getAdvPassthrough() {
        return advPassthrough;
    }

    public String getAdvFormat() {
        return advFormat;
    }

    public boolean getDeathEnabled() {
        return deathEnabled;
    }

    public boolean getDeathPassthrough() {
        return deathPassthrough;
    }

    public String getDeathFormat() {
        return deathFormat;
    }
}
