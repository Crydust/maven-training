package com.mycompany.example12.boilerplate;

public class BoilerplateException extends RuntimeException {

    public BoilerplateException() {
        super();
    }

    public BoilerplateException(String message) {
        super(message);
    }

    public BoilerplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoilerplateException(Throwable cause) {
        super(cause);
    }

}
