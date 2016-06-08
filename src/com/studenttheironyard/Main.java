package com.studenttheironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    static HashMap<String, User> userMap = new HashMap<>();


    public static void main(String[] args) {
        Spark.staticFileLocation("public");
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    User user = userMap.get(username);

                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        int id = 1;
                        for (Message msg : user.messageList) {
                            msg.id = id;
                            id++;
                        }
                        m.put("messages", user.messageList);
                        return new ModelAndView(m, "messages.html");
                    }
                },
                    new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                (request, response) -> {
                    String name = request.queryParams("username");
                    String password = request.queryParams("userpassword");

                    User user = userMap.get(name);
                    if (name == null || password == null) {
                        throw new Exception("Name or Password not entered");
                    }
                    if (user == null) {
                        user = new User(name, password);
                        userMap.put(name, user);
                    }
                    else if (!password.equals(user.password)){
                        throw new Exception("Invalid password");
                    }
                    Session session = request.session();
                    session.attribute("username", name);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/create-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = userMap.get(username);

                    if (username == null) {
                        throw new Exception("Not logged in");
                    }

                    String text = request.queryParams("usermessage");
                    user.messageList.add(new Message(text));

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/delete-message",
                (request, response) -> {
                 Session session = request.session();
                 String username = session.attribute("username");
                    if(username == null) {
                        throw new Exception("You must be logged in to do that");
                    }
                    int id = Integer.valueOf(request.queryParams("deleteid"));

                    User user = userMap.get(username);
                    if (id <= 0 || id - 1 >= user.messageList.size()) {
                        throw new Exception("Invalid id");
                    }

                    user.messageList.remove(id - 1);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = userMap.get(username);
                        if(username == null) {
                            throw new Exception("You must be logged in to do that");
                    }
                    int id  = Integer.valueOf(request.queryParams("id"));
                    String text = request.queryParams("text");

                    if (id <= 0 || id - 1 >= user.messageList.size()) {
                        throw new Exception("Invalid id");

                }
                    user.messageList.set(id - 1,new Message(text));

                    response.redirect("/");
                    return"";

                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "/";
                }
        );
    }
}
