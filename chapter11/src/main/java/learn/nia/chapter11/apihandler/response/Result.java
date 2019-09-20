package learn.nia.chapter11.apihandler.response;

public class Result<T extends Info> {

    protected T info;

    public T getInfo() {
        return info;
    }

    public Result(T info) {
        this.info = info;
    }

}
