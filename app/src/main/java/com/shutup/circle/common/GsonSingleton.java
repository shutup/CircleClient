package com.shutup.circle.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by shutup on 2017/4/7.
 */

public class GsonSingleton {
    private static Gson sGson;
    private GsonSingleton(){

    }


    public static Gson getGson() {
        if (sGson == null) {
            synchronized (Gson.class) {
                if (sGson == null) {
                    // Creates the json object which will manage the information received
                    GsonBuilder builder = new GsonBuilder();

// Register an adapter to manage the date types as long values
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());
                        }
                    });

                    sGson = builder.create();
                    return sGson;
                }else {
                    return sGson;
                }
            }
        }
        return sGson;
    }
}
