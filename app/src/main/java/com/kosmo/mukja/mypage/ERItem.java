package com.kosmo.mukja.mypage;

public class ERItem {

   private String store_name;
   private String store_id;
   private String er_title;
   private String er_postdate;
   private String user_id;
   private String erjoin_num;
   private String u_nick;
   private String u_tend;
   private String u_age;
    private String u_img;
   public ERItem() {
   }

    public ERItem(String store_name, String store_id, String er_title, String er_postdate, String user_id, String erjoin_num, String u_nick, String u_tend, String u_age,String u_img) {
        this.store_name = store_name;
        this.store_id = store_id;
        this.er_title = er_title;
        this.er_postdate = er_postdate;
        this.user_id = user_id;
        this.erjoin_num = erjoin_num;
        this.u_nick = u_nick;
        this.u_tend = u_tend;
        this.u_age = u_age;
        this.u_img = u_img;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getEr_title() {
        return er_title;
    }

    public void setEr_title(String er_title) {
        this.er_title = er_title;
    }

    public String getEr_postdate() {
        return er_postdate;
    }

    public void setEr_postdate(String er_postdate) {
        this.er_postdate = er_postdate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getErjoin_num() {
        return erjoin_num;
    }

    public void setErjoin_num(String erjoin_num) {
        this.erjoin_num = erjoin_num;
    }

    public String getU_nick() {
        return u_nick;
    }

    public void setU_nick(String u_nick) {
        this.u_nick = u_nick;
    }

    public String getU_tend() {
        return u_tend;
    }

    public void setU_tend(String u_tend) {
        this.u_tend = u_tend;
    }

    public String getU_age() {
        return u_age;
    }

    public void setU_age(String u_age) {
        this.u_age = u_age;
    }
}
