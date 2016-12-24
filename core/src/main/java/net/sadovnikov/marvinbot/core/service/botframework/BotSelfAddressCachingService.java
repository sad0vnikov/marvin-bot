package net.sadovnikov.marvinbot.core.service.botframework;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.mbf4j.Address;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Every action bot sends to Bot Framework needs to have 'from' field which contains bot ID
 * ID is unique for every channel and there is no way get it in advance
 * So, we have to get Bot's id from 'recipient' field in incoming events and save it
 */
public class BotSelfAddressCachingService {

    private DbService dbService;
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Inject
    public BotSelfAddressCachingService(DbService dbService) {
        this.dbService = dbService;
    }

    public void saveBotAddressForChannel(Channel channel, Address address) {
        Document docToInsert = new Document().append("channel_id", channel.id())
                .append("bot_id", address.id())
                .append("bot_name", address.name());

        logger.debug("setting bot id = " + address.id() + " for channel " + channel.id());
        if (getBotAddressForChannel(channel).isPresent()) {
            Document findCriteria = new Document().append("channel_id", channel.id());
            getCollection().replaceOne(findCriteria, docToInsert);
        } else {
            getCollection().insertOne(docToInsert);
        }

    }

    public Optional<Address> getBotAddressForChannel(Channel channel) {
        Document findCriteria = new Document().append("channel_id", channel.id());
        Document result = getCollection().find(findCriteria).first();
        if (result == null) {
            return Optional.empty();
        }

        String botId = (String)result.get("bot_id");
        String botName = (String) result.get("bot_name");
        Address address = new Address(botId, botName);

        return Optional.of(address);

    }

    protected MongoCollection<Document> getCollection() {
        return dbService.getDb().getCollection("bot_id_cache");
    }
}
