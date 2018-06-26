package org.bitm.pencilbox.firebaseauthpb5;

/**
 * Created by Mobile App on 6/25/2018.
 */

public class MyTodoList {
    private String todo;
    private String date;
    private String id;

    public MyTodoList(String todo, String date, String id) {
        this.todo = todo;
        this.date = date;
        this.id = id;
    }

    public MyTodoList() {
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
