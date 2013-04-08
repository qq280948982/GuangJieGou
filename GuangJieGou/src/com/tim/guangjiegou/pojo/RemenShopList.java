package com.tim.guangjiegou.pojo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RemenShopList extends BaseAPI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8503837870035853946L;
	
	private List<Shop> mShopList = new ArrayList<Shop>();
	
	@Override
	public void initWithJson(String json) {
		super.initWithJson(json);
		if(json != null) {
			mShopList.clear();
			try {
				JSONArray shopArray = new JSONObject(json).getJSONArray("shops");
				if(shopArray != null && shopArray.length() > 0) {
					final int length = shopArray.length();
					for(int i = 0; i < length; i++) {
						Shop shop = new Shop();
						shop.initWithJson(shopArray.getJSONObject(i));
						mShopList.add(shop);
					}
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Shop> getShopList() {
		return mShopList;
	}

	public void appendList(RemenShopList otherList) {
		if(otherList != null && otherList.getShopList() != null) {
			List<Shop> otherShopList = otherList.getShopList();
			final int size = otherShopList.size();
			for(int i = 0; i < size; i++) {
				Shop shop = otherShopList.get(i);
				if(!mShopList.contains(shop)) {
					mShopList.add(shop);
				}
			}
		}
	}
	
	public Shop getShop(int index) {
		Shop shop = null;
		if(index >= 0 && index < mShopList.size()) {
			shop = mShopList.get(index);
		}
		return shop;
	}
}
