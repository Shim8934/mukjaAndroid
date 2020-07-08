package com.kosmo.mukja.mypage;

public class FallowItem {

   private String postdate;
   private String store_id;
   private String user_id;
    private String img;
    public FallowItem() {
    }

    public FallowItem(String postdate, String store_id, String user_id,String img) {
        this.postdate = postdate;
        this.store_id = store_id;
        this.user_id = user_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
