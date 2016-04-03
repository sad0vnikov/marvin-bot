package events.eventDispatchers;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import events.EventDispatcher;
import events.eventTypes.MessageEvent;

public class Skype4jEventDispatcher extends EventDispatcher {

    Skype skype;

    public Skype4jEventDispatcher(Skype skype) {
        this.skype = skype;
    }


    public void run() {

        this.skype.getEventDispatcher().registerListener(new Listener() {

            @EventHandler
            public void onMessage(MessageReceivedEvent ev) {
                ReceivedMessage skype4jMessage = ev.getMessage();
                String msgContent = skype4jMessage.getContent().asPlaintext();
                skype4jMessage.getChat().getIdentity();
                String senderId = skype4jMessage.getChat().getIdentity();
                message.ReceivedMessage msg = new message.ReceivedMessage(senderId, msgContent);
                dispatch(new MessageEvent(msg));
            }
        });

        System.out.println("Signed in, waiting for events");

        try {
            this.skype.subscribe();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
}
