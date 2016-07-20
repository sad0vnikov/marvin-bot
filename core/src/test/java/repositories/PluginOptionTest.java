package repositories;


import net.sadovnikov.marvinbot.core.db.FakeDbService;
import net.sadovnikov.marvinbot.core.db.repository.GlobalPluginOption;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.codecs.StringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.junit.Test;
import static junit.framework.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginOptionTest {

    @Test
    public void testGetAllQuery() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        PluginOption option = new GlobalPluginOption(new FakeDbService(), "someTestPlugin");
        Method testAllQueryMethod = GlobalPluginOption.class.getDeclaredMethod("makeGetAllQuery");
        testAllQueryMethod.setAccessible(true);
        BsonDocument result = encodingFilterToBsonDoc( (Bson) testAllQueryMethod.invoke(option) );
        assertEquals(1, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));
    }

    @Test
    public void testGetByNameQuery()  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException  {
        PluginOption option = new GlobalPluginOption(new FakeDbService(), "someTestPlugin");
        Method queryMethod = GlobalPluginOption.class.getDeclaredMethod("makeSearchByNameQuery", String.class);
        queryMethod.setAccessible(true);

        BsonDocument result = encodingFilterToBsonDoc( (Bson) queryMethod.invoke(option, "someOption") );

        assertEquals(2, result.size());
        assertTrue(result.containsKey("pluginName"));
        assertEquals(new BsonString("someTestPlugin"), result.getString("pluginName"));

        assertTrue(result.containsKey("name"));
        assertEquals(new BsonString("someOption"), result.getString("name"));
    }

    protected BsonDocument encodingFilterToBsonDoc(Bson filter) {
        return filter.toBsonDocument(BsonDocument.class, CodecRegistries.fromCodecs(new StringCodec()));
    }

}
