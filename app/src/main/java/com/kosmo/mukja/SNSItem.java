package com.kosmo.mukja;

import android.graphics.drawable.Drawable;

public class SNSItem {

    private String snsProfile;
    private String snsID;
    private String snsContent;
    private String snsRe;

    public SNSItem() {
    }

    public SNSItem(String snsProfile, String snsID, String snsContent, String snsRe) {
        this.snsProfile = snsProfile;
        this.snsID = snsID;
        this.snsContent = snsContent;
        this.snsRe = snsRe;
    }

    public String getSnsProfile() {
        return snsProfile;
    }

    public void setSnsProfile(String snsProfile) {
        this.snsProfile = snsProfile;
    }

    public String getSnsID() {
        return snsID;
    }

    public void setSnsID(String snsID) {
        this.snsID = snsID;
    }

    public String getSnsContent() {
        return snsContent;
    }

    public void setSnsContent(String snsContent) {
        this.snsContent = snsContent;
    }

    public String getSnsRe() {
        return snsRe;
    }

    public void setSnsRe(String snsRe) {
        this.snsRe = snsRe;
    }
}
