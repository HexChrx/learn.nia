package learn.nia.websocket.service;

import learn.nia.websocket.model.Client;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestService {

    public static Client clientRegister(String request) {
        String res = new String(Base64.decodeBase64(request));
        JSONObject json = new JSONObject(res);

        Client client = new Client();

        if (!json.has("rid")) {
            return client;
        }
        try {
            client.setRoomId(json.getInt("rid"));
        } catch (JSONException e) {
            e.printStackTrace();
            return client;
        }

        if (!json.has("id") || !json.has("token")) {
            return client;
        }

        long id;
        String token;

        try {
            id = json.getLong("id");
            token = json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return client;
        }

        if (!checkToken(id, token)) {
            return client;
        }
        client.setId(id);
        return client;
    }

    private static boolean checkToken(Long id, String token) {
        return true;
    }
}
