package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.BotFrameworkEventDispatcher;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.CachingContactManager;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.message.BotFrameworkMessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.mbf4j.Bot;

public class BotFrameworkInjector extends AbstractModule {

    protected Bot bot;

    public BotFrameworkInjector(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void configure() {
        bind(MessageSenderService.class).to(BotFrameworkMessageSenderService.class);
        bind(ContactManager.class).to(BotFrameworkContactManager.class);
        bind(EventDispatcher.class).to(BotFrameworkEventDispatcher.class);
        bind(CachingContactManager.class).to(BotFrameworkContactManager.class);
        bind(Bot.class).toInstance(this.bot);
        bind(net.sadovnikov.mbf4j.events.EventDispatcher.class).to(Bot.class);

    }
}
