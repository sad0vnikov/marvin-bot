package net.sadovnikov.marvinbot.core.db.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.chat.GroupChat;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * A plugins option for concrete chat
 */
public class PluginChatOption extends GlobalPluginOption {

    AbstractChat chat;

    public PluginChatOption(DbService db, AbstractChat chat, String pluginName) {
        super(db, pluginName);
        this.chat = chat;
    }

    public PluginChatOption(DbService db, String pluginName) {
        super(db, pluginName);
    }

    /**
     * Returns chats list which have a given option and the option has given value
     * @param optionName
     * @param value
     * @return
     */
    public Set<AbstractChat> findChatswithOptionValues(String optionName, Object value) {

        HashSet<AbstractChat> list = new HashSet<>();
        FindIterable result = getCollection().find(
                and(eq("pluginName", pluginName), eq("name", optionName), eq("value", value))
        );

        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            Document option = (Document) resultIterator.next();
            String chatId = option.getString("chatId");
            String channelType = option.getString("channel");
            Channel channel = new Channel(ChannelTypes.valueOf(channelType));
            Boolean isGroup = option.getBoolean("isGroup");
            Chat chat = new Chat(channel, chatId);
            if (isGroup) {
                chat = new GroupChat(channel, chatId);
            }
            list.add(chat);
        }

        return list;
    }

    /**
     * Returns chats list which have a given option and the option has given values.
     * If a value is array, all the given values must be present
     * @param optionName
     * @param values
     * @return
     */
    public Set<AbstractChat> findChatsOptionValuesIn(String optionName, Object[] values) {

        HashSet<AbstractChat> chats = new HashSet<>();
        FindIterable result = getCollection().find(
            and(eq("pluginName", pluginName), eq("name", optionName), in("value", values))
        );

        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            Document option = (Document) resultIterator.next();
            String chatId = option.getString("chatId");
            String channelType = option.getString("channel");
            Channel channel = new Channel(ChannelTypes.valueOf(channelType));
            Boolean isGroup = option.getBoolean("isGroup");
            Chat chat = new Chat(channel, chatId);
            if (isGroup) {
                chat = new GroupChat(channel, chatId);
            }
            chats.add(chat);
        }

        return chats;
    }

    @Override
    protected Document createOption(String name, Object value) {
        Document doc = super.createOption(name, value);
        doc.put("chatId", chat.chatId());
        doc.put("channel", chat.channel());
        return doc;
    }

    @Override
    protected Bson makeSearchByNameQuery(String optName) {
        return and(eq("pluginName", pluginName), eq("chatId", chat.channel()), eq("channel", chat.channel()), eq("name", optName));
    }

    @Override
    protected Bson makeGetAllQuery() {
        return and(eq("pluginName", pluginName), eq("chatId", chat.channel()), eq("channel", chat.channel()));
    }

    @Override
    protected MongoCollection<Document> getCollection() {
        MongoDatabase database = db.getDb();
        return database.getCollection("chat_plugin_options");
    }

}
