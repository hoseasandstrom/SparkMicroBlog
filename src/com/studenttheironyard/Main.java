package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static String password;
    static Message message;
    static ArrayList<User> userList;
    static ArrayList<String> passwordList;
    static ArrayList<Message> messageList;

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap stuff = new HashMap();
                    if (user == null) {
                        return new ModelAndView(stuff, "messages.html");
                    } else {
                        stuff.put("name", user.name);
                        stuff.put("users", userList);
                        stuff.put("message", message.message);
                        stuff.put("messages", messageList);
                        return new ModelAndView(stuff, "index.html");
                    }
                },
                    new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-user",
                (request, response) -> {
                    String username = request.queryParams("username");
                    user = new User(username);
                    userList.add(user);
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-password",
                (request, response) -> {
                    String userpassword = request.queryParams("userpassword");
                    password = new String(userpassword);
                    passwordList.add(password);
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    String usermessage = request.queryParams("usermessage");
                    message = new Message(usermessage);
                    messageList.add(message);
                    response.redirect("/");
                    return "";
                }
        );
    }
}
