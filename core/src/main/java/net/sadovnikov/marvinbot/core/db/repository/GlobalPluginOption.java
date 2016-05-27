package net.sadovnikov.marvinbot.core.db.repository;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.DbService;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import org.bson.conversions.Bson;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A global plugin option
 */
public class GlobalPluginOption extends PluginOption {


    public GlobalPluginOption(String pluginName) {
        this.pluginName = pluginName;
    }


    @Override
    protected Document createOption(String name, String value) {
        Document doc = new Document();
        doc.put("pluginName", pluginName);
        doc.put("name", name);
        doc.put("value", value);

        return doc;
    }

    @Override
    protected Bson makeSearchByNameQuery(String optName) {
        return and(makeGetAllQuery(), eq("name", optName));
    }

    @Override
    protected Bson makeGetAllQuery() {
        return eq("pluginName", pluginName);
    }

    @Override
    protected MongoCollection<Document> getCollection() {
        return db.getDb().getCollection("global_plugin_options");
    }
}
