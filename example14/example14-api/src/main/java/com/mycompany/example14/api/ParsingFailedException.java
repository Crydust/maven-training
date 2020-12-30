package com.mycompany.example14.api;

public class ParsingFailedException extends Exception {
    public ParsingFailedException() {
        super();
    }

    public ParsingFailedException(String message) {
        super(message);
    }

    public ParsingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingFailedException(Throwable cause) {
        super(cause);
    }
}
