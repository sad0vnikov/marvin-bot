package net.sadovnikov.marvinbot.core.events.event_types;


import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.events.ChatEvent;

public class UserJoinedChatEvent extends ChatEvent {

    protected String userName;

    public UserJoinedChatEvent(Channel channel, String userName) {
        this.userName = userName;
        this.channel = channel;
    }

}
