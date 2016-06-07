package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static ArrayList<Message> messageList = new ArrayList<>();

    public static void main(String[] args) {
        HashMap<String, User> userMap = new HashMap<String, User>();
        Spark.staticFileLocation("public");

        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("messages", messageList);
                        m.put("name", user.name);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                    new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("userpassword");
                    if (!userMap.containsKey(username)) {
                        user = new User(username, password);
                        userMap.put(username, user);
                        response.redirect("/");
                        return "";
                    }
                    else if (password.equals(userMap.get(username).password)){
                        user = userMap.get(username);
                        response.redirect("/");
                    }
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    String text = request.queryParams("usermessage");
                    Message message = new Message(text);
                    messageList.add(message);
                    response.redirect("/");
                    return "";
                }
        );
    }
}
