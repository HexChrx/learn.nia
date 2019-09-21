package learn.nia.chapter11.service;

import learn.nia.chapter11.apihandler.ApiProtocol;
import learn.nia.chapter11.model.User;

public class UserService extends BaseService {
    public UserService(ApiProtocol apiProtocol) {
        super(apiProtocol);
    }

    public User get() {
        String uidStr = getParam("uid");
        int uid = Integer.parseInt(uidStr);
        return new User(uid, "HelloWorld", 20);
    }
}