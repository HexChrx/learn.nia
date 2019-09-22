package learn.nia.websocket.service;

import learn.nia.websocket.dto.Response;
import learn.nia.websocket.model.Client;

import javax.swing.*;

public class MessageService {

    public static Response sendMessage(Client client, String message) {
        Response res = new Response();
        res.getData().put("id", client.getId());
        res.getData().put("message", message);
        res.getData().put("ts", System.currentTimeMillis());
        return res;
    }
}
