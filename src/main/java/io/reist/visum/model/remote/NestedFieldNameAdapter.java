/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of Visum.
 *
 * Visum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visum.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.visum.model.remote;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Reist on 10/17/15.
 */
public class NestedFieldNameAdapter implements JsonDeserializer<Object> {

    private final Gson defaultGson;

    public NestedFieldNameAdapter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.addDeserializationExclusionStrategy(new NestedFieldNameExclusionStrategy());
        this.defaultGson = gsonBuilder.create();
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fill(
                defaultGson.fromJson(json, typeOfT),
                json,
                (Class) ((ParameterizedType) typeOfT).getRawType()
        );
    }

    @Nullable
    private Object fill(Object o, JsonElement json, Class<?> rootClass) {
        if (json != null && !json.isJsonPrimitive()) {
            if (List.class.isAssignableFrom(rootClass) || rootClass.isArray()) {
                JsonArray jsonArray = json.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object entity = rootClass.isArray() ?
                            Array.get(o, i) :
                            ((List) o).get(i);
                    if (entity == null) {
                        continue;
                    }
                    JsonObject entityJson = jsonArray.get(i).getAsJsonObject();
                    injectNestedFields(entityJson, entity);
                }
            } else if (o != null) {
                injectNestedFields(json.getAsJsonObject(), o);
            }
        }
        return o;
    }

    private void injectNestedFields(JsonObject jsonObject, Object entity) {
        Class entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field f : fields) {

            String fieldName = f.getName();

            NestedFieldName nestedFieldName = f.getAnnotation(NestedFieldName.class);
            if (nestedFieldName == null) {
                try {
                    boolean accessible = f.isAccessible();
                    if (!accessible) f.setAccessible(true); // todo access via getters
                    Object o = f.get(entity);
                    if (!accessible) f.setAccessible(false);
                    if (o != null) {
                        SerializedName serializedName = f.getAnnotation(SerializedName.class);
                        JsonElement json = jsonObject.get(serializedName != null ? serializedName.value() : fieldName);
                        fill(o, json, o.getClass());
                    }
                } catch (IllegalAccessException e) {
                    throw new JsonParseException(e);
                }
                continue;
            }
            String path = nestedFieldName.value();

            JsonElement fieldElement = null;
            String[] pathSegments = path.split("\\.");
            JsonObject parentJson = jsonObject;
            for (String segment : pathSegments) {
                if (fieldElement != null) {
                    parentJson = fieldElement.getAsJsonObject();
                }
                fieldElement = parentJson.get(segment);
            }

            Object v = defaultGson.fromJson(fieldElement, f.getType());

            try {
                boolean accessible = f.isAccessible();
                if (!accessible) f.setAccessible(true); // todo access via setters
                f.set(entity, v);
                if (!accessible) f.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new JsonParseException(e);
            }

        }
    }

}
