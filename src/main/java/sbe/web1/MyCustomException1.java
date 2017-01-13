package sbe.web1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MyCustomException1 extends Exception {
    private static final long serialVersionUID = 1L;

    public MyCustomException1(String msg) {
        super(msg);
    }
}
