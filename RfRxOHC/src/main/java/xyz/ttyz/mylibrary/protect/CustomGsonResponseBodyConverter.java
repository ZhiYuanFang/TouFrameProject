package xyz.ttyz.mylibrary.protect;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by tou on 2019/1/7.
 */

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    private void judgeJSONAndChange(Object object) throws JSONException {
        if(object instanceof JSONObject){
            JSONObject json = (JSONObject) object;
            Iterator<String> iterator = json.keys();
            List<String> changeKeyList = new ArrayList<>();
            while (iterator.hasNext()){
                String key = iterator.next();
                Object obj = json.get(key);
                if(obj instanceof JSONObject || obj instanceof JSONArray){
                    judgeJSONAndChange(obj);
                } else if(StringUtil.safeString(obj).isEmpty()){
                    changeKeyList.add(key);
                }
            }
            for(String key : changeKeyList){
                System.out.println("LoggingI GsonResponse Change KEY : " + key + "'s value to null");
                json.put(key, null);
            }
        }
        if(object instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) object;
            if(jsonArray.length() > 0){
                for(int i = 0 ; i < jsonArray.length(); i ++){
                    Object obj = jsonArray.get(i);
                    if(obj instanceof JSONObject || obj instanceof JSONArray){
                        judgeJSONAndChange(obj);
                    }
                }
            }
        }
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String originalBody = value.string();
        // 获取json中的code，对json进行预处理
        JSONObject json = null;
        try {
            json = new JSONObject(originalBody);
            // 当code不为0时，设置data为{}，这样转化就不会出错了
            judgeJSONAndChange(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(null != json){
            value = ResponseBody.create(value.contentType(), json.toString());
        }

        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
