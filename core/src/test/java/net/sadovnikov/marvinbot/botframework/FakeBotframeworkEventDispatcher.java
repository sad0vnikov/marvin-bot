package net.sadovnikov.marvinbot.botframework;

import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.events.Event;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.BotFrameworkEventDispatcher;

public class FakeBotframeworkEventDispatcher extends BotFrameworkEventDispatcher {


    public FakeBotframeworkEventDispatcher(MockedBot bot, Injector injector) {
        super(bot, injector);
    }

    @Override
    public void dispatch(Event ev) {
        super.dispatch(ev);
    }
}
