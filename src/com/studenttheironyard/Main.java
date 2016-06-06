package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static Message content;
    static ArrayList<User> userList = new ArrayList<>();
    static ArrayList<Message> messageContent = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
            HashMap stuff = new HashMap();
            if (user == null) {
                return new ModelAndView(stuff, "login.html");
            } else {
                stuff.put("name", user.name);
                stuff.put("users", userList);
            }
             if(content == null) {
                 return new ModelAndView(stuff, "messages.html");
             } else {
                 stuff.put("content", content.content);
                 stuff.put("contents", messageContent);
                 return new ModelAndView(stuff, "index.html");
             }
        },
            new MustacheTemplateEngine()
    );
    Spark.post(
            "/login",
            (request, response) -> {
            String username = request.queryParams("username");
            user = new User(username);
            userList.add(user);
            response.redirect("/messages");
            return "";
            }
        );
        Spark.post(
                "/messages",
                (request, response) -> {
                    String userMessage = request.queryParams("usermessage");
                    content = new Message(userMessage);
                    messageContent.add(content);
                    response.redirect("/");
                    return "";
                }
        );
    Spark.post (
            "/logout",
            (request, response) -> {
            user = null;
            response.redirect("/");
            return "";
            }
        );
    }
}
