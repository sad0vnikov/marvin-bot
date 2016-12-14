package net.sadovnikov.marvinbot;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import net.sadovnikov.marvinbot.botframework.MockedBot;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.BotFrameworkEventDispatcher;
import net.sadovnikov.marvinbot.core.injection.FakeDbModule;
import net.sadovnikov.marvinbot.core.injection.PluginManagerInjector;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.CachingContactManager;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.message.BotFrameworkMessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.service.permissions.Skype4jPermissionChecker;
import net.sadovnikov.mbf4j.Bot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class TestBase  {

    protected Injector injector = Guice.createInjector(new FakeDbModule(), new PluginManagerInjector(), new AbstractModule() {
        @Override
        protected void configure() {
            String locale = "EN";
            bind(Locale.class).toInstance(new Locale(locale));
            bindConstant().annotatedWith(Names.named("commandPrefix")).to("!");
            bindConstant().annotatedWith(Names.named("admins")).to("admin");
        }
    }, new AbstractModule() {
        @Override
        protected void configure() {
            bind(MessageSenderService.class).to(BotFrameworkMessageSenderService.class);
            bind(ContactManager.class).to(BotFrameworkContactManager.class);
            bind(CachingContactManager.class).to(BotFrameworkContactManager.class);
            bind(Bot.class).toInstance(new MockedBot());
        }
    }, new AbstractModule() {
        @Override
        protected void configure() {
            bind(Logger.class).toInstance(LogManager.getLogger());
        }
    });
}
