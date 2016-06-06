package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static User password;
    static Message userContent;
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
            }
            else {
                stuff.put("name", user.name);
                stuff.put("users", userList);
                stuff.put("usermesage", userContent.content);
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
                String userPassword = request.queryParams("password");
                password = new User(userPassword);
                response.redirect("/");
                return "";
                }
        );
        Spark.post(
                "/messages",
                (request, response) -> {
                    String userMessage = request.queryParams("usermessage");
                    userContent = new Message(userMessage);
                    messageContent.add(userContent);
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
