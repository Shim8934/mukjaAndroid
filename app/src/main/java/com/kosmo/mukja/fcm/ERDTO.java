package com.kosmo.mukja.fcm;

import java.io.Serializable;

public class ERDTO implements Serializable {
    private int er_no;
    private int erc_no;
	private String store_name;
    private String er_master;
    private String username;
    private String er_title;
    private String er_content;
    private String er_time;
    private String er_tend;
    private String er_max;
    private String er_postdate;
    private String password;
    private String master_nick;
	private String master_img;
    private String u_nick;
    private String u_regidate;
    private String u_img;
    private String u_age;
    private String u_tend;
    private String u_lat;
    private String u_lng;
    private String u_ph;
    private String enabled;
    private String authority;
    private String er_curr;
    private String store_id;

	public ERDTO() {
	}

	public String getMaster_nick() {
		return master_nick;
	}

	public void setMaster_nick(String master_nick) {
		this.master_nick = master_nick;
	}

	public String getMaster_img() {
		return master_img;
	}

	public void setMaster_img(String master_img) {
		this.master_img = master_img;
	}

	public int getEr_no() {
		return er_no;
	}

	public void setEr_no(int er_no) {
		this.er_no = er_no;
	}

	public int getErc_no() {
		return erc_no;
	}

	public void setErc_no(int erc_no) {
		this.erc_no = erc_no;
	}

	public String getEr_master() {
		return er_master;
	}

	public void setEr_master(String er_master) {
		this.er_master = er_master;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEr_title() {
		return er_title;
	}

	public void setEr_title(String er_title) {
		this.er_title = er_title;
	}

	public String getEr_content() {
		return er_content;
	}

	public void setEr_content(String er_content) {
		this.er_content = er_content;
	}

	public String getEr_time() {
		return er_time;
	}

	public void setEr_time(String er_time) {
		this.er_time = er_time;
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

	public String getEr_postdate() {
		return er_postdate;
	}

	public void setEr_postdate(String er_postdate) {
		this.er_postdate = er_postdate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getU_nick() {
		return u_nick;
	}

	public void setU_nick(String u_nick) {
		this.u_nick = u_nick;
	}

	public String getU_regidate() {
		return u_regidate;
	}

	public void setU_regidate(String u_regidate) {
		this.u_regidate = u_regidate;
	}

	public String getU_img() {
		return u_img;
	}

	public void setU_img(String u_img) {
		this.u_img = u_img;
	}

	public String getU_age() {
		return u_age;
	}

	public void setU_age(String u_age) {
		this.u_age = u_age;
	}

	public String getU_tend() {
		return u_tend;
	}

	public void setU_tend(String u_tend) {
		this.u_tend = u_tend;
	}

	public String getU_lat() {
		return u_lat;
	}

	public void setU_lat(String u_lat) {
		this.u_lat = u_lat;
	}

	public String getU_lng() {
		return u_lng;
	}

	public void setU_lng(String u_lng) {
		this.u_lng = u_lng;
	}

	public String getU_ph() {
		return u_ph;
	}

	public void setU_ph(String u_ph) {
		this.u_ph = u_ph;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEr_curr() {
		return er_curr;
	}

	public void setEr_curr(String er_curr) {
		this.er_curr = er_curr;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
}
