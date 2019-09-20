package learn.nia.chapter11.apihandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Api {

    private String       name;
    private String       regex;
    private List<String> paramterNames;
    private Set<String>  httpMethod;
    private String       resource;
    private int          build;

    public Api() {
        this.paramterNames = new ArrayList<String>();
        this.httpMethod = new HashSet<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        String[] strings = name.split("/");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length() == 0) {
                continue;
            }
            stringBuilder.append("/");
            if (strings[i].startsWith(":")) {
                paramterNames.add(strings[i].substring(1));
                stringBuilder.append("([^/]+)");
            } else {
                stringBuilder.append(strings[i]);
            }
        }
        this.regex = stringBuilder.toString();
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<String> getParamterNames() {
        return paramterNames;
    }

    public void setParamterNames(List<String> paramterNames) {
        this.paramterNames = paramterNames;
    }

    public Set<String> getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(Set<String> httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void addHttpMethod(String httpMethod) {
        this.httpMethod.add(httpMethod);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }
}
