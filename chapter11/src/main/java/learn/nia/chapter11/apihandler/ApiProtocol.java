package learn.nia.chapter11.apihandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiProtocol {

    private static final Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    private int        build = 101;
    private String     version = "1.0";
    private String     channel = "learn.nia";
    private String     geo     = null;
    private String     clientIp = null;
    private String     serverIp = null;
    private String     api      = null;
    private String     endPoint = null;
    private String     auth     = null;
    private int        offset   = 0;
    private int        limit    = 10;
    private HttpMethod method = HttpMethod.GET;
    private String     postBody = null;
    private Map<String, List<String>> paramters = new HashMap<String, List<String>>();

    public int getBuild() {
        return build;
    }

    public String getVersion() {
        return version;
    }

    public String getChannel() {
        return channel;
    }

    public String getGeo() {
        return geo;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getApi() {
        return api;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getAuth() {
        return auth;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPostBody() {
        return postBody;
    }

    public Map<String, List<String>> getParamters() {
        return paramters;
    }

    private void addParamter(String key, String param) {
        List<String> params = new ArrayList<>();
        params.add(param);
        this.paramters.put(key, params);
    }

    public ApiProtocol(ChannelHandlerContext ctx, Object msg) {
        HttpRequest req = (HttpRequest) msg;

        String uri = req.uri();
        if (uri.length() <= 0) {
            return;
        }

        logger.info(uri);
        this.method = req.method();

    }

    private void parseEndpoint(String uri) {
        String endpoint = uri.split("\\?")[0];
        if (endpoint.endsWith("/")) {
            endpoint = endPoint.substring(0, endpoint.length());
        }
        this.endPoint = endpoint;

        Set<Map.Entry<String, Api>> set = ApiRoute.apiMap.entrySet();
        for (Map.Entry<String, Api> entry : set) {
            Api api = entry.getValue();
            Pattern pattern = Pattern.compile("^" + entry.getValue().getRegex() + "$");
            Matcher matcher = pattern.matcher(endpoint);
            if (matcher.find()) {
                this.api = api.getName();
                if (matcher.groupCount() > 0) {
                    for (int i = 0; i < matcher.groupCount(); ++i) {
                        addParamter(api.getParamterNames().get(i), matcher.group(i + 1));
                    }
                }
                break;
            }
        }
    }

    private void setIp(ChannelHandlerContext ctx, HttpRequest req) {
        String clinetIp = (String) req.headers().get("X-Forwarded-For");
        if (clinetIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            clinetIp = inetSocketAddress.getAddress().getHostAddress();
        }
        this.clientIp = clinetIp;

        InetSocketAddress serverSocket = (InetSocketAddress) ctx.channel().localAddress();
        this.serverIp = serverSocket.getAddress().getHostAddress();
    }

    private void setFields() {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();

            if (fieldName.equals("logger")
                    || fieldName.equals("method")
                    || fieldName.equals("paramters")
                    || fieldName.equals("postBody")) {
                continue;
            }
            if (!this.paramters.containsKey(fieldName)) {
                continue;
            }
            Class fieldType = field.getType();
            field.setAccessible(true);
            try {
                if (fieldType == int.class) {
                    field.setInt(this, Integer.parseInt(this.paramters.get(fieldName).get(0)));
                } else {
                    field.set(this, this.paramters.get(fieldName).get(0));
                }
            } catch (NumberFormatException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
            this.paramters.remove(fieldName);
        }
    }

    private void queryStringHandler(String uri) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        if (queryStringDecoder.parameters().size() > 0) {
            this.paramters.putAll(queryStringDecoder.parameters());
        }
    }

    private void requestParamtersHandler(HttpRequest req) {
        if (req.method().equals(HttpMethod.POST)) {
            HttpPostRequestDecoder httpPostRequestDecoder = new HttpPostRequestDecoder(req);
            try{
                List<InterfaceHttpData> postList = httpPostRequestDecoder.getBodyHttpDatas();
                for (InterfaceHttpData data : postList) {
                    List<String> values = new ArrayList<>();
                    MixedAttribute value = (MixedAttribute) data;
                    value.setCharset(CharsetUtil.UTF_8);
                    values.add(value.getValue());
                    this.paramters.put(data.getName(), values);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void requestBodyHandler(Object msg) {
        if(msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            this.postBody = content.toString(CharsetUtil.UTF_8);
        }
    }
}
