package com.tim.guangjiegou.pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop extends VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8503837870035853946L;

	private static final String PIC_PREFIX = "http://logo.taobao.com/shop-logo"; 
	
	private String bulletin;
	private String cid;
	private String created;
	private String nick;
	private String pic_path;
	private String sid;
	private String title;
	
	public void initWithJson(JSONObject jObject) {
		if(jObject != null) {
			try {
				bulletin = jObject.getString("bulletin");
				cid = jObject.getString("cid");
				created = jObject.getString("created");
				nick = jObject.getString("nick");
				pic_path = jObject.getString("pic_path");
				sid = jObject.getString("sid");
				title = jObject.getString("title");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getBulletin() {
		return bulletin;
	}

	public void setBulletin(String bulletin) {
		this.bulletin = bulletin;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getShopLogo() {
		return PIC_PREFIX + this.pic_path;
	}

	@Override
	public boolean equals(Object object) {
		boolean same = false;
		if(object != null && object instanceof Shop && this.sid != null) {
			Shop shop = (Shop) object;
			String sid = shop.sid;
			if(this.sid.equals(sid)) {
				same = true;
			}
		}
		return same;
	}
}
