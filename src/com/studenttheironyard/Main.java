package com.studenttheironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static User user;
    static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
            HashMap content = new HashMap();
            if (user == null) {
                return new ModelAndView(content, "login.html");
            } else {
                content.put("name", user.name);
                content.put("users", userList);
                return new ModelAndView(content, "index.html");
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
