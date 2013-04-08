package com.tim.guangjiegou.pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class TaoKeShopItem extends ShopItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6688798868001963475L;
	
	private static int SearchResultCount;	
	private String taobaoShopItemLink;
	
	@Override
	public void initWithJson(JSONObject jObject) {
		super.initWithJson(jObject);
		if(jObject != null) {
			try {
				taobaoShopItemLink = jObject.getString("click_url");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getTaobaoShopItemLink() {
		return taobaoShopItemLink;
	}
	
	public static void setSearchResultCount(int count) {
		SearchResultCount = count;
	}
	
	public static int SearchResultCount() {
		return SearchResultCount;
	}

}
