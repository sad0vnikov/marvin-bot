package net.sadovnikov.marvinbot.repositories;


import net.sadovnikov.marvinbot.core.db.FakeDbService;
import net.sadovnikov.marvinbot.core.db.repository.PluginChatOption;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.codecs.StringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ChatPluginOptionTest {

    @Test
    public void testGetAllQuery() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Chat chat = new Chat(new Channel(ChannelTypes.SKYPE), "someChatId");
        PluginOption option = new PluginChatOption(new FakeDbService(), chat, "someTestPlugin");
        Method testAllQueryMethod = PluginChatOption.class.getDeclaredMethod("makeGetAllQuery");
        testAllQueryMethod.setAccessible(true);
        BsonDocument result = encodingFilterToBsonDoc( (Bson) testAllQueryMethod.invoke(option) );
        assertEquals(3, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));
        assertTrue(result.containsKey("chatId"));
        assertEquals(new BsonString("someChatId"), result.getString("chatId"));
        assertTrue(result.containsKey("channel"));
        assertEquals(new BsonString(ChannelTypes.SKYPE.toString()), result.getString("channel"));

    }

    @Test
    public void testGetByNameQuery()  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException  {
        Chat chat = new Chat(new Channel(ChannelTypes.SKYPE), "someChatId");
        PluginOption option = new PluginChatOption(new FakeDbService(), chat, "someTestPlugin");
        Method queryMethod = PluginChatOption.class.getDeclaredMethod("makeSearchByNameQuery", String.class);
        queryMethod.setAccessible(true);

        BsonDocument result = encodingFilterToBsonDoc( (Bson) queryMethod.invoke(option, "someOption") );

        assertEquals(4, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));

        assertTrue(result.containsKey("chatId"));
        assertEquals(new BsonString("someChatId"), result.getString("chatId"));

        assertTrue(result.containsKey("name"));
        assertEquals(new BsonString("someOption"), result.getString("name"));

        assertTrue(result.containsKey("channel"));
        assertEquals(new BsonString(ChannelTypes.SKYPE.toString()), result.getString("channel"));
    }

    protected BsonDocument encodingFilterToBsonDoc(Bson filter) {
        return filter.toBsonDocument(BsonDocument.class, CodecRegistries.fromCodecs(new StringCodec()));
    }

}
