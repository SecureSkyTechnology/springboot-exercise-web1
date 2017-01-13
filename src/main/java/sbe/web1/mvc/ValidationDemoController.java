package sbe.web1.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import sbe.web1.mvc.form.UserForm;

@Controller
@RequestMapping("/validation-demo")
public class ValidationDemoController {
    static final Logger LOG = LoggerFactory.getLogger(ValidationDemoController.class);

    @RequestMapping("/")
    public String index(@ModelAttribute("form") UserForm userForm) {
        return "validation-demo/index";
    }

    @PostMapping("/")
    public String validateMvc(@ModelAttribute("form") @Validated UserForm userForm, BindingResult result, Model m) {
        for (FieldError fe : result.getFieldErrors()) {
            LOG.info("/validation-demo : field error[] = {}", fe);
        }
        return "validation-demo/index";
    }

    @PostMapping("/rest")
    @ResponseBody
    public UserForm echoJsonMappedModel(@RequestBody @Validated UserForm userForm) {
        return userForm;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidateErrorMessage>> handleValidateException(MethodArgumentNotValidException e, WebRequest request) {
        BindingResult bindResult = e.getBindingResult();
        return createErrorEntity(bindResult, request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ValidateErrorMessage>> handleBindException(BindException e, WebRequest request) {
        BindingResult bindResult = e.getBindingResult();
        return createErrorEntity(bindResult, request);
    }

    @Autowired
    MessageSource messageSource;

    ResponseEntity<List<ValidateErrorMessage>> createErrorEntity(BindingResult result, WebRequest request) {
        List<ValidateErrorMessage> errors = new ArrayList<>();
        for (FieldError fe : result.getFieldErrors()) {
            String field = fe.getField();
            String message = messageSource.getMessage(fe, request.getLocale());
            errors.add(new ValidateErrorMessage(field, message));
            LOG.info("/validation-demo : createErrorEntity() field error[] = {}", fe);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Collections.EMPTY_MAP);
    }
}

class ValidateErrorMessage {
    public final String field;
    public final String message;

    public ValidateErrorMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }
}