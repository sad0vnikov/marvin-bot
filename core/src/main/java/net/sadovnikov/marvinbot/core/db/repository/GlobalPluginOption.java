package net.sadovnikov.marvinbot.core.db.repository;

import com.mongodb.client.MongoCollection;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import org.bson.conversions.Bson;


/**
 * A global plugin option
 */
public class GlobalPluginOption extends PluginOption {


    public GlobalPluginOption(DbService db, String pluginName) {
        super(db);
        this.pluginName = pluginName;
    }


    @Override
    protected Document createOption(String name, Object value) {
        Document doc = new Document();
        doc.append("pluginName", pluginName);
        doc.append("name", name);
        doc.append("value", value);

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
