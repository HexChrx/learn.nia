package learn.nia.chapter11.service;

import learn.nia.chapter11.apihandler.ApiProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BaseService {
    protected ApiProtocol apiProtocol;
    protected Logger logger;

    public BaseService(ApiProtocol apiProtocol) {
        this.apiProtocol = apiProtocol;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public String getParam(String key) {
        List<String> values = apiProtocol.getParamters().get(key);
        if (values == null || values.size() == 0) {
            return "";
        }
        return values.get(0);
    }
}
