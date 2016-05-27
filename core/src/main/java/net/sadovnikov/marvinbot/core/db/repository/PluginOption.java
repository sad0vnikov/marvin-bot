package net.sadovnikov.marvinbot.core.db.repository;


import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.DbService;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class PluginOption {


    @Inject
    protected DbService db;

    protected String pluginName;


    protected abstract Document createOption(String name, String value);
    protected abstract Bson makeSearchByNameQuery(String optName);
    protected Bson makeGetAllQuery() {
        return eq("pluginName", pluginName);
    }

    protected abstract MongoCollection<Document> getCollection();


    public void set(String name, String value) throws DbException {
        Document doc = getCollection()
                .find(makeSearchByNameQuery(name))
                .first();

        if (doc != null) {
            doc.put("value", value);
            getCollection().replaceOne(
                    eq("_id", doc.get("_id")),
                    doc
            );

        } else {
            Document newDocument = createOption(name, value);
            getCollection().insertOne(newDocument);
        }

    }

    public String get(String name) throws DbException {
        Document doc =  getCollection()
                .find(makeSearchByNameQuery(name))
                .first();

        if (doc == null) {
            return null;
        }

        return doc.get("value").toString();
    }

    public Map<String,String> getAll() throws DbException {
        Map<String,String> result = new HashMap<>();
        Iterator optionsIterator = getCollection().find(makeGetAllQuery()).iterator();

        while (optionsIterator.hasNext()) {
            Document option = (Document) optionsIterator.next();
            result.put(option.get("name").toString(), option.get("value").toString());
        }

        return result;
    }
}
