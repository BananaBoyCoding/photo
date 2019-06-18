package com.jiehun.component.http.interceptor;

import android.util.Log;

import com.jiehun.component.utils.AbSystemTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static com.jiehun.component.config.BaseLibConfig.context;

/**
 * JieHun
 * describe:签名
 * author zhouyao
 * date 2018/1/18
 */

public class SignInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //
//        Map<String, Object> map = BaseLibConfig.mIExtPlugin.getCommonParams();
        //
        Request oldRequest = chain.request();
        Request.Builder requestBuilder = oldRequest.newBuilder();
        RequestBody requestBody = requestBuilder.build().body();

        //传body参数

//        if (requestBody != null && requestBody.contentLength() > 0) {
//            String subtype = requestBody.contentType().subtype();
//            if (subtype.contains("json")) {
//                requestBody = processApplicationJsonRequestBody(requestBody, map);
//            }
//
//        } else {
//            try {
//                JSONObject obj = new JSONObject();
//                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    obj.put(entry.getKey(), entry.getValue());
//                }
//                requestBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), obj.toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        //传header参数
//        if (map != null && !map.isEmpty()) {
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                if (entry.getValue() == null) {
//                    continue;
//                }
//                requestBuilder.header(entry.getKey(), entry.getValue().toString());
////                requestBuilder.header(entry.getKey(), URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
//            }
//        }
        requestBuilder.header("test", "123");
        Log.e("zymyy","app_version-------->"+ AbSystemTool.getAppVersion());
        Log.e("zymyy","device_id-------->"+ AbSystemTool.getPhoneIMEI(context));
//        String sign =

        if (requestBody != null) {
            oldRequest = requestBuilder.post(requestBody).build();
        } else {
            oldRequest = requestBuilder.build();
        }


        return chain.proceed(oldRequest);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private RequestBody processApplicationJsonRequestBody(RequestBody requestBody, Map<String, Object> map) {
        String customReq = bodyToString(requestBody);
        try {
            JSONObject obj = new JSONObject(customReq);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                obj.put(entry.getKey(), entry.getValue());
            }
//            obj.put("accessToken", token);
//            obj.put("city_id", "110900");
            return RequestBody.create(requestBody.contentType(), obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private String getSign(HashMap<String,Object> bodyMap,HashMap<String,Object> headerMap){
//        HashMap<String,Object> map = new HashMap<>();
//        map.putAll(bodyMap);
//        map.putAll(headerMap);
//
//        String sign =
//    }
}
