package com.tim.guangjiegou.pojo;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopItem extends BaseAPI implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7307460859211267490L;
	
	private String num_iid;
	private String title;
	private String shopOwner;
	private String shopOwnerImage;
	private String shopItemImage;
	private String likes;
	private String prices;
	private String recentSaled;
	private String description;
	private String stock;
	
	@Override
	public void initWithJson(String json) {
		super.initWithJson(json);
		if(json != null) {
			try {
				JSONObject jObject = new JSONObject(json);
				initWithJson(jObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initWithJson(JSONObject jObject) {
		if(jObject != null) {
			try {
				this.num_iid = jObject.getString("num_iid");
				this.title = jObject.getString("title");
				this.shopOwner = jObject.getString("nick");
//				this.shopOwnerImage = jObject.getString("");
				this.shopItemImage = jObject.getString("pic_url");
				setPrices(jObject.getString("price"));
				this.stock = jObject.getString("num");
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getNum_iid() {
		return num_iid;
	}

	public void setNum_iid(String num_iid) {
		this.num_iid = num_iid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShopOwner() {
		return shopOwner;
	}

	public void setShopOwner(String shopOwner) {
		this.shopOwner = shopOwner;
	}

	public String getShopOwnerImage() {
		return shopOwnerImage;
	}

	public void setShopOwnerImage(String shopOwnerImage) {
		this.shopOwnerImage = shopOwnerImage;
	}

	public String getShopItemImage() {
		return shopItemImage;
	}

	public void setShopItemImage(String shopItemImage) {
		this.shopItemImage = shopItemImage;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getPrices() {
		return prices;
	}

	public void setPrices(String prices) {
		if(prices != null) {
			this.prices = prices;
			if(!prices.startsWith("￥")) {
				this.prices = "￥" + prices;
			}
		}
	}

	public String getRecentSaled() {
		return recentSaled;
	}

	public void setRecentSaled(String recentSaled) {
		this.recentSaled = recentSaled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
}
