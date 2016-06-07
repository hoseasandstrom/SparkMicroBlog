package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static Message currentmessage;
    static ArrayList<Message> messageList;

    public static void main(String[] args) {
        HashMap<String, User> userMap = new HashMap<String, User>();
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    if (user == null) {
                        return new ModelAndView(userMap, "index.html");
                    } else {
                        return new ModelAndView(userMap, "messages.html");
                    }
                },
                    new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("userpassword");
                    if (userMap.containsKey(username) && password.equals(password)) {
                        response.redirect("/");
                        return "";
                    } else if (userMap.containsKey(username) && password != password){
                        response.redirect("/create-user");
                    }
                    else {
                        user = new User(password, username);
                        userMap.put("username", user);
                    }
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("usermessage");
                    currentmessage = new Message(message);
                    messageList.add(currentmessage);
                    response.redirect("/");
                    return "";
                }
        );
    }
}
