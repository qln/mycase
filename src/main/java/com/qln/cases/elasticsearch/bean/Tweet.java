package com.qln.cases.elasticsearch.bean;

import java.util.Date;

public class Tweet {

    private Long id;
    private String user;
    private Date postDate;
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tweet [id=");
        builder.append(id);
        builder.append(", user=");
        builder.append(user);
        builder.append(", postDate=");
        builder.append(postDate);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }
}
