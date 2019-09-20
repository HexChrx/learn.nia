package learn.nia.chapter11.apihandler;

import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApiHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    public static byte[] transfer(ChannelHandlerContext ctx, Object msg) {

        ApiProtocol apiProtocol = new ApiProtocol(ctx, msg);

        if (apiProtocol.getEndPoint() == null) {
            return encode(ErrorHandler.error(StatusCode.API_CAN_NOT_BE_NULL));
        }

        if (apiProtocol.getApi() == null) {
            return encode(ErrorHandler.error(StatusCode.API_NOT_FOUND));
        }

        Object result = invoke(apiProtocol.getApi(), apiProtocol);
        if (result == null) {
            return encode(ErrorHandler.error(StatusCode.UNKNOWN_ERROR));
        }
        return encode(result);
    }

    public static Object invoke(String apiName, ApiProtocol apiProtocol) {
        Class<?> className;
        Object classObject;
        Constructor constructor;
        Method method;
        Object result = null;

        Api api = ApiRoute.apiMap.get(apiName);
        if (api == null) {
            return ErrorHandler.error(StatusCode.API_NOT_FOUND);
        }

        if (apiProtocol.getBuild() < api.getBuild()) {
            return ErrorHandler.error(StatusCode.VERSION_IS_TOO_LOW);
        }

        if (api.getHttpMethod() != null && !api.getHttpMethod().contains(apiProtocol.getMethod().toString().toLowerCase()))  {
            return ErrorHandler.error(StatusCode.REQUEST_MODE_ERROR);
        }

        try {
            className = Class.forName(Config.getString("resource.package.name") + "." + api.getResource());
            constructor = className.getConstructor(ApiProtocol.class);
            classObject = constructor.newInstance(apiProtocol);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        } catch (InstantiationException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        }

        try {
            method = className.getMethod(apiProtocol.getMethod().toString().toLowerCase());
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            return ErrorHandler.error(StatusCode.API_SERVER_ERROR);
        }

        try {
            result = method.invoke(classObject);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }

        return result;

    }

    private static byte[] encode(Object object) {
        String data = new JSONObject(object).toString();
        return data.getBytes();
    }
}
