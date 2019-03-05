package com.app.cookbook.xinhe.foodfamily.util;

import com.app.cookbook.xinhe.foodfamily.main.entity.OtherGuanZhuAnswer;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mob.tools.utils.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shiyujia02 on 2018/1/25.
 */

public class DataTypeAdaptor extends TypeAdapter<OtherGuanZhuAnswer>{
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Data.class) {
                return (TypeAdapter<T>) new DataTypeAdaptor(gson);
            }
            return null;
        }
    };

    private final Gson gson;

    DataTypeAdaptor(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, OtherGuanZhuAnswer value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("id");
        gson.getAdapter(String.class).write(out, value.getId());
        out.name("title");
        gson.getAdapter(String.class).write(out, value.getTitle());
        out.name("content");
        gson.getAdapter(String.class).write(out, value.getContent());
        out.name("countUsers");
        gson.getAdapter(String.class).write(out, value.getCountUsers());
        out.name("answer");
        gson.getAdapter(Object.class).write(out, value.getAnswer());
        out.endObject();
    }

    @Override
    public OtherGuanZhuAnswer read(JsonReader in) throws IOException {
        OtherGuanZhuAnswer data = new OtherGuanZhuAnswer();
        Map<String, Object> dataMap = (Map<String, Object>) readInternal(in);
        data.setId((String) dataMap.get("id"));
        data.setAnswer(dataMap.get("answer")) ;
        data.setContent((String)dataMap.get("content"));
        data.setTitle((String)dataMap.get("title"));
        data.setCountUsers((String)dataMap.get("countUsers"));
        return data;
    }


    private Object readInternal(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(readInternal(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), readInternal(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                //将其作为一个字符串读取出来
                String numberStr = in.nextString();
                //返回的numberStr不会为null
                if (numberStr.contains(".") || numberStr.contains("e")
                        || numberStr.contains("E")) {
                    return Double.parseDouble(numberStr);
                }
                return Long.parseLong(numberStr);

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }

}
