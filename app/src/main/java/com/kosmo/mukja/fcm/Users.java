package com.kosmo.mukja.fcm;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Users {

    private String username;
    private String password;
    private String u_nick;
    private java.sql.Date u_regidate;
    private String u_img;
    private String u_age;
    private String u_tend;
    private String u_addr;
    private int u_lat;
    private int u_lng;
    private String u_ph;
    private int enabled;
    private String authority;

    public Users() {

    }


}
