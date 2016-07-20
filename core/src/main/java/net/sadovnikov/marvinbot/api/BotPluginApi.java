package net.sadovnikov.marvinbot.api;

import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.db.repository.GlobalPluginOption;
import net.sadovnikov.marvinbot.core.db.repository.PluginChatOption;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;

import java.util.Locale;

/**
 * This is a facade for accessing Marvin's capabilities from plugins
 */
public class BotPluginApi {

    protected MessageSenderService messageSender;
    protected ContactManager contactManager;
    protected String pluginName;
    protected Locale locale;
    protected MongoDbService db;

    public BotPluginApi(MessageSenderService messageSender, ContactManager contactManager,
                        String pluginName, Locale locale, MongoDbService db) {

        this.messageSender  = messageSender;
        this.contactManager = contactManager;
        this.pluginName     = pluginName;
        this.locale         = locale;
        this.db             = db;
    }

    public MessageSenderService message() {
        return messageSender;
    }

    public ContactManager contact() {
        return contactManager;
    }

    public PluginOptions pluginOptions() {
        return new PluginOptions(pluginName);
    }

    public final class PluginOptions {

        private String pluginName;

        PluginOptions(String pluginName) {
            this.pluginName = pluginName;
        }

        public GlobalPluginOption global() {
            return new GlobalPluginOption(db, pluginName);
        }

        public PluginChatOption chat(String chatId) {
            return new PluginChatOption(db, chatId, pluginName);
        }
    }

    public Locale getLocale() {
        return locale;
    }
}
