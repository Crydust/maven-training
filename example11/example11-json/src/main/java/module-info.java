module com.mycompany.example11.json {
    requires json;
    requires com.mycompany.example11.api;
    requires com.mycompany.example11.core;
    provides com.mycompany.example11.api.ExpressionParser
            with com.mycompany.example11.json.JsonExpressionParser;
}
