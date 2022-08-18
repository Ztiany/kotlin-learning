package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.JsonDeserializers.PrimitiveIntegerJsonDeserializer;
import kotlin.Unit;

import java.lang.reflect.Modifier;

/**
 * @author Ztiany
 */
public class GsonUtils {

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            /*容错处理*/
            .registerTypeAdapter(int.class, new PrimitiveIntegerJsonDeserializer())
            .registerTypeAdapter(float.class, new JsonDeserializers.PrimitiveFloatJsonDeserializer())
            .registerTypeAdapter(double.class, new JsonDeserializers.PrimitiveDoubleJsonDeserializer())
            .registerTypeAdapter(Integer.class, new JsonDeserializers.IntegerJsonDeserializer())
            .registerTypeAdapter(Float.class, new JsonDeserializers.FloatJsonDeserializer())
            .registerTypeAdapter(Double.class, new JsonDeserializers.DoubleJsonDeserializer())
            .registerTypeAdapter(String.class, new JsonDeserializers.StringJsonDeserializer())
            .registerTypeAdapter(Void.class, new JsonDeserializers.VoidJsonDeserializer())
            .registerTypeAdapter(Unit.class, new JsonDeserializers.UnitJsonDeserializer())
            .create();

    public static Gson gson() {
        return GSON;
    }

}
