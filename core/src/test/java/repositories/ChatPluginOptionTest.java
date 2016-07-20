package repositories;


import net.sadovnikov.marvinbot.core.db.FakeDbService;
import net.sadovnikov.marvinbot.core.db.repository.PluginChatOption;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
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
        PluginOption option = new PluginChatOption(new FakeDbService(), "someChatId", "someTestPlugin");
        Method testAllQueryMethod = PluginChatOption.class.getDeclaredMethod("makeGetAllQuery");
        testAllQueryMethod.setAccessible(true);
        BsonDocument result = encodingFilterToBsonDoc( (Bson) testAllQueryMethod.invoke(option) );
        assertEquals(2, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));
        assertTrue(result.containsKey("chatId"));
        assertEquals(new BsonString("someChatId"), result.getString("chatId"));
    }

    @Test
    public void testGetByNameQuery()  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException  {
        PluginOption option = new PluginChatOption(new FakeDbService(), "someChatId", "someTestPlugin");
        Method queryMethod = PluginChatOption.class.getDeclaredMethod("makeSearchByNameQuery", String.class);
        queryMethod.setAccessible(true);

        BsonDocument result = encodingFilterToBsonDoc( (Bson) queryMethod.invoke(option, "someOption") );

        assertEquals(3, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));

        assertTrue(result.containsKey("chatId"));
        assertEquals(new BsonString("someChatId"), result.getString("chatId"));

        assertTrue(result.containsKey("name"));
        assertEquals(new BsonString("someOption"), result.getString("name"));
    }

    protected BsonDocument encodingFilterToBsonDoc(Bson filter) {
        return filter.toBsonDocument(BsonDocument.class, CodecRegistries.fromCodecs(new StringCodec()));
    }

}
