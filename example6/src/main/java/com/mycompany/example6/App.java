package com.mycompany.example6;

import org.json.JSONStringer;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        System.out.println(new JSONStringer()
                .object()
                .key("JSON")
                .value("Hello, World!")
                .endObject()
                .toString());
    }
}
