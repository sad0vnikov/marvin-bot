package net.sadovnikov.marvinbot.core.db.repository;


import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.DbService;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;

import java.util.*;

public abstract class PluginOption {


    @Inject
    protected DbService db;

    protected String pluginName;


    protected abstract Document createOption(String name, Object value);

    protected abstract Bson makeSearchByNameQuery(String optName);
    protected Bson makeGetAllQuery() {
        return eq("pluginName", pluginName);
    }

    protected abstract MongoCollection<Document> getCollection();

    /**
     * @param name Option name
     * @param value Option value
     * @throws DbException
     */
    public void set(String name, Object value) throws DbException {
        Document doc = getCollection()
                .find(makeSearchByNameQuery(name))
                .first();

        if (doc != null) {
            doc.append("value", value);
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
        Document doc = getDocument(name);
        if (doc == null) {
            return null;
        }

        return doc.getString("value");
    }

    public String get(String name, String defaultValue) throws DbException {
        String value = get(name);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public List<String> getValuesList(String name) throws DbException {
        Document doc = getDocument(name);
        if (doc == null) {
            return null;
        }

        return doc.get(name, List.class);
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

    protected Document getDocument(String name) {
        Document doc =  getCollection()
                .find(makeSearchByNameQuery(name))
                .first();

        return doc;
    }
}
