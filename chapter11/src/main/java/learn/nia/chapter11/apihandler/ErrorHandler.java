package learn.nia.chapter11.apihandler;

import learn.nia.chapter11.apihandler.response.Info;
import learn.nia.chapter11.apihandler.response.Result;

public class ErrorHandler {

    public static Result error(int errcode) {
        Result result = new Result<>(new Info());
        result.getInfo().setCode(errcode).setCodeMessage(StatusCode.codeMap.get(errcode));
        return result;
    }

    public static Result error(int errcode, Object paramter) {
        Result result = new Result<>(new Info());
        result.getInfo()
                .setCode(errcode)
                .setCodeMessage(String.format(StatusCode.codeMap.get(errcode), paramter));
        return result;
    }
}
