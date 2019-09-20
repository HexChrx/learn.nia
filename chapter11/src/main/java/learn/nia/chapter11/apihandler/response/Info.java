package learn.nia.chapter11.apihandler.response;

public class Info {
    private int code = 200;
    private String codeMessage;

    public int getCode() {
        return code;
    }

    public Info setCode(int code) {
        this.code = code;
        return this;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public Info setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
        return this;
    }
}
