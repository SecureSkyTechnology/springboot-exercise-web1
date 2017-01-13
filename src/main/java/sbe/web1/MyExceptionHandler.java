package sbe.web1;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * more better:
 * 
 * @see https://speakerdeck.com/sinsengumi/spring-boot-application-infrastructure
 * @see http://qiita.com/yakumo/items/026fc4274ac2692e4947
 */
@ControllerAdvice
public class MyExceptionHandler {
    static final Logger LOG = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public String handleIOException(IOException e) {
        LOG.warn("MyExceptionHandler : IOException", e);
        return "error_io";
    }

    @ExceptionHandler(MyCustomException1.class)
    public String handleMyCustomException1(MyCustomException1 e) {
        LOG.warn("MyExceptionHandler : MyCustomException1", e);
        return "error_custom1";
    }
    //
    // @ExceptionHandler
    // public String handleAnyException(Exception e) {
    // LOG.warn("MyExceptionHandler : Exception", e);
    // return "error_any";
    // }
}
