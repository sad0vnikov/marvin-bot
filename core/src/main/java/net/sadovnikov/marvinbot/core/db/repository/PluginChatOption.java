package net.sadovnikov.marvinbot.core.db.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * A plugins option for concrete chat
 */
public class PluginChatOption extends GlobalPluginOption {

    String chatId;

    public PluginChatOption(DbService db, String chatId, String pluginName) {
        super(db, pluginName);
        this.chatId = chatId;
    }

    /**
     * Returns chats list which have a given option and the option has given value
     * @param optionName
     * @param value
     * @return
     */
    public Set<String> findChatswithOptionValues(String optionName, Object value) {

        HashSet<String> list = new HashSet<>();
        FindIterable result = getCollection().find(
                and(eq("pluginName", pluginName), eq("name", optionName), eq("value", value))
        );

        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            Document option = (Document) resultIterator.next();
            list.add(option.getString("chatId"));
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
    public Set<String> findChatsOptionValuesIn(String optionName, Object[] values) {

        HashSet<String> chats = new HashSet<>();
        FindIterable result = getCollection().find(
            and(eq("pluginName", pluginName), eq("name", optionName), in("value", values))
        );

        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            Document option = (Document) resultIterator.next();
            chats.add(option.getString("chatId"));
        }

        return chats;
    }

    @Override
    protected Document createOption(String name, Object value) {
        Document doc = super.createOption(name, value);
        doc.put("chatId", chatId);
        return doc;
    }

    @Override
    protected Bson makeSearchByNameQuery(String optName) {
        return and(eq("pluginName", pluginName), eq("chatId", chatId), eq("name", optName));
    }

    @Override
    protected Bson makeGetAllQuery() {
        return and(eq("pluginName", pluginName), eq("chatId", chatId));
    }

    @Override
    protected MongoCollection<Document> getCollection() {
        MongoDatabase database = db.getDb();
        return database.getCollection("chat_plugin_options");
    }

}
