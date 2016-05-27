package net.sadovnikov.marvinbot.core.db.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * A plugin option for concrete chat
 */
public class PluginChatOption extends GlobalPluginOption {

    String chatId;

    public PluginChatOption(String chatId, String pluginName) {
        super(pluginName);
        this.chatId = chatId;
    }

    @Override
    protected Document createOption(String name, String value) {
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
        return db.getDb().getCollection("chat_plugin_options");
    }

}
