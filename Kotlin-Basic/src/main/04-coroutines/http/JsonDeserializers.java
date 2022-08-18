package http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import kotlin.Unit;

import java.lang.reflect.Type;

/**
 * @author Ztiany
 */
class JsonDeserializers {

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
