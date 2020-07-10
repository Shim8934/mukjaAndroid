package com.kosmo.mukja.fcm;

public class Chat {

    int detachNo;
    int ercno;
    String message;
    String username;
    String nick;

    public Chat() {
    }

    public Chat(int detachNo, int ercno, String message, String username, String nick) {
        this.detachNo = detachNo;
        this.ercno = ercno;
        this.message = message;
        this.username = username;
        this.nick = nick;
    }

    public int getDetachNo() {
        return detachNo;
    }

    public void setDetachNo(int detachNo) {
        this.detachNo = detachNo;
    }

    public int getErcno() {
        return ercno;
    }

    public void setErcno(int ercno) {
        this.ercno = ercno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
