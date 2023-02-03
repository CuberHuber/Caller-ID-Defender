package app.sten.wit;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIAgent {

    private final int SELF_ID = 6;
    private final String API_URL = "http://95.163.241.107:8000";
    private final String API_PUT_ACTION_PATH = "/clients/action";
    private final String API_AUTH_PATH = "/clients/";

    private final String NAME = "Yegor";
    private final String SELF_PHONE = "+79195994669";
    private final String PASSWORD = "test";

    public void auth_request() {
//        Тестовая функция. Нужно обрабатывать незарегистрированные и зарегистрированные аккаунты
        Thread thread = new Thread() {
            public void run() {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();

                String json = String.format("{\r\n" +
                        " \"name\" : \"%s\",\r\n" +
                        " \"phone\" : \"%s\",\r\n" +
                        " \"password\" : \"%s\"\r\n" +
                        "}", NAME, SELF_PHONE, PASSWORD);
                Log.d("JSON", json);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(API_URL + API_AUTH_PATH).post(body).build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("AUTH", Objects.requireNonNull(response.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public ActionResponse put_request(final String number) {

        class ActionRequest implements Runnable {
            private volatile String resp;

            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();

                String json = String.format("{\r\n" +
                        " \"client_id\" : %s,\r\n" +
                        " \"action_type\" : \"call\",\r\n" +
                        " \"action_phone\" : \"%s\"\r\n" +
                        "}", SELF_ID, number);
                Log.d("JSON", json);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(API_URL + API_PUT_ACTION_PATH).put(body).build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseString = Objects.requireNonNull(response.body()).string();
                    Log.d("ACTION", responseString);
                    resp = responseString;
                } catch (IOException e) {
                    e.printStackTrace();
                    resp = "null";
                }
            }

            public String getResp() {
                return resp;
            }
        }
        ActionRequest request = new ActionRequest();
        Thread thread = new Thread(request);
        thread.start();
        try {
            thread.join();
            return parseResponseBody(request.getResp());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ActionResponse parseResponseBody(String response) {
        try {
            JSONObject json = new JSONObject(response);
            boolean isService = json.getBoolean("is_service");
            boolean resp = json.getBoolean("resp");

            if (isService && resp) return new ActionResponse(ActionResponse.STATUS_EXIST_ORGANIZATION, "Доверенная организация");
            if (isService) return new ActionResponse(ActionResponse.STATUS_EXIST_ORGANIZATION, "Возможный мошенник");
            return new ActionResponse(ActionResponse.STATUS_UNKNOWN_ORGANIZATION, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new ActionResponse(ActionResponse.STATUS_UNKNOWN_ORGANIZATION, "");
        }
    }
}
