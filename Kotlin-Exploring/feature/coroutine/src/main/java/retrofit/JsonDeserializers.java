package retrofit;

import com.google.gson.*;
import kotlin.Unit;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * @author Ztiany
 */
class JsonDeserializers {

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

    static Gson gson() {
        return GSON;
    }


    static class PrimitiveIntegerJsonDeserializer implements JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                return 0;
            }
        }

    }

    static class PrimitiveFloatJsonDeserializer implements JsonDeserializer<Float> {

        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                return 0F;
            }
        }

    }

    static class PrimitiveDoubleJsonDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                return 0D;
            }
        }

    }

    static class IntegerJsonDeserializer implements JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                return null;
            }
        }

    }

    static class FloatJsonDeserializer implements JsonDeserializer<Float> {

        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                return null;
            }
        }

    }

    static class DoubleJsonDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                return null;
            }
        }

    }

    static class StringJsonDeserializer implements JsonDeserializer<String> {

        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsString();
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class VoidJsonDeserializer implements JsonDeserializer<Void> {

        @Override
        public Void deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }

    }

    static class UnitJsonDeserializer implements JsonDeserializer<Unit> {

        @Override
        public Unit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            System.out.println("UnitJsonDeserializer.deserialize");
            return Unit.INSTANCE;
        }

    }

}
