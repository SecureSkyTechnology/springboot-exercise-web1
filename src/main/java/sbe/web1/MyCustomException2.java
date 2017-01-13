package sbe.web1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MyCustomException2 extends Exception {
    private static final long serialVersionUID = 1L;

    public MyCustomException2(String msg) {
        super(msg);
    }
}
