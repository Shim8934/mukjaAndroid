package com.kosmo.mukja.mypage;

public class MYERItem {

   private String er_no;
   private String erjoin_num;
   private String store_name;
   private String er_title;
   private String er_tend;
   private String er_max;
   private String er_time;
   private String u_nick;
    private String u_tend;
   private String u_age;
   private String u_img;

   public MYERItem() {
   }

   public MYERItem(String er_no, String erjoin_num, String store_name, String er_title,
                   String er_tend, String er_max, String er_time, String u_nick,
                   String u_tend, String u_age, String u_img) {
      this.er_no = er_no;
      this.erjoin_num = erjoin_num;
      this.store_name = store_name;
      this.er_title = er_title;
      this.er_tend = er_tend;
      this.er_max = er_max;
      this.er_time = er_time;
      this.u_nick = u_nick;
      this.u_tend = u_tend;
      this.u_age = u_age;
      this.u_img = u_img;
   }

   public String getEr_no() {
      return er_no;
   }

   public void setEr_no(String er_no) {
      this.er_no = er_no;
   }

   public String getErjoin_num() {
      return erjoin_num;
   }

   public void setErjoin_num(String erjoin_num) {
      this.erjoin_num = erjoin_num;
   }

   public String getStore_name() {
      return store_name;
   }

   public void setStore_name(String store_name) {
      this.store_name = store_name;
   }

   public String getEr_title() {
      return er_title;
   }

   public void setEr_title(String er_title) {
      this.er_title = er_title;
   }

   public String getEr_tend() {
      return er_tend;
   }

   public void setEr_tend(String er_tend) {
      this.er_tend = er_tend;
   }

   public String getEr_max() {
      return er_max;
   }

   public void setEr_max(String er_max) {
      this.er_max = er_max;
   }

   public String getEr_time() {
      return er_time;
   }

   public void setEr_time(String er_time) {
      this.er_time = er_time;
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

   public String getU_img() {
      return u_img;
   }

   public void setU_img(String u_img) {
      this.u_img = u_img;
   }
}
