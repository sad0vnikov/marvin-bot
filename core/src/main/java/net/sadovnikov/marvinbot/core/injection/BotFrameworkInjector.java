package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.samczsun.skype4j.Skype;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.BotFrameworkEventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.Skype4jEventDispatcher;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.CachingContactManager;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.contact.Skype4jContactManager;
import net.sadovnikov.marvinbot.core.service.message.BotFrameworkMessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.Skype4JMessageSenderService;
import net.sadovnikov.marvinbot.core.service.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.service.permissions.Skype4jPermissionChecker;
import net.sadovnikov.mbf4j.Bot;

public class BotFrameworkInjector extends AbstractModule {

    protected Bot bot;

    public BotFrameworkInjector(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void configure() {
        bind(MessageSenderService.class).to(BotFrameworkMessageSenderService.class);
        bind(EventDispatcher.class).to(BotFrameworkEventDispatcher.class);
        bind(ContactManager.class).to(BotFrameworkContactManager.class);
        bind(CachingContactManager.class).to(BotFrameworkContactManager.class);
        bind(PermissionChecker.class).to(Skype4jPermissionChecker.class);
        bind(Bot.class).toInstance(this.bot);
    }
}
