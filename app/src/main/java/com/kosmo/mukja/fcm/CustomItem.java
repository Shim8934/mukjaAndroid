package com.kosmo.mukja.fcm;

//리스트뷰의 하나의 아이템에 뿌려질 데이타를 저장 하는
//자료구조]
public class CustomItem {


    //텍스트1 저장용]
    private String text1;
    //인자 생성자]
    public CustomItem(String text1) {
        this.text1 = text1;

    }////////////////
    public String getText() {
        return text1;
    }


}
