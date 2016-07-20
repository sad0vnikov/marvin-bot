package net.sadovnikov.marvinbot.core.db.query;

import org.bson.conversions.Bson;

public abstract class Query {

    protected Bson query;

    public Bson bson() {
        return query;
    }
}
