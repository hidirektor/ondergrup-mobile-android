package me.t3sl4.ondergrup.Model.Message;

public class Message {
    private String userID;
    private String userName;
    private String nameSurname;
    private long commentDate;
    private String comment;
    private String type;

    public Message(String userID, String userName, String nameSurname, long commentDate, String comment, String type) {
        this.userID = userID;
        this.userName = userName;
        this.nameSurname = nameSurname;
        this.commentDate = commentDate;
        this.comment = comment;
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(long commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}