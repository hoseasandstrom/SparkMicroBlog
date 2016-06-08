package com.studenttheironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {
    static User user;


    public static void main(String[] args) {
        HashMap<String, User> userMap = new HashMap<String, User>();
        Spark.staticFileLocation("public");
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        User user = userMap.get(username);
                        m.put("messages", user.messageList);
                        m.put("username", user.name);
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
                    if (name == null || password == null) {
                        throw new Exception("Name or Password not entered");
                    }
                    if (user == null) {
                        user = new User(name, password);
                        User user = new User(name, password);
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
                    int deleteid = Integer.valueOf(request.queryParams("deleteid"));

                    User user = userMap.get(username);
                    if (deleteid <= 0 || deleteid - 1 >= user.messageList.size()) {
                        throw new Exception("Invalid id");
                    }

                    user.messageList.remove(deleteid - 1);

                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    String editText = request.queryParams("newText");
                    User user = userMap.get(username);
                        if(username == null) {
                            throw new Exception("You must be logged in to do that");
                    }

                    int editid = Integer.valueOf(request.queryParams("editid"));

                    if (editid <= 0 || editid - 1 >= user.messageList.size()) {
                        throw new Exception("Invalid id");

                }
                    user.messageList.set(editid - 1,new Message(editText));

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
