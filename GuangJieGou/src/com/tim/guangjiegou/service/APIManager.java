package com.tim.guangjiegou.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.TopApiListener;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.GuangJieGouApplication;
import com.tim.guangjiegou.pojo.DaPeiList;
import com.tim.guangjiegou.pojo.RemenShopList;
import com.tim.guangjiegou.pojo.ShopItem;
import com.tim.guangjiegou.pojo.TaoKeShopItem;
import com.tim.guangjiegou.util.IOUtil;
import com.tim.guangjiegou.util.Log;

public class APIManager {
	
	private final String TAG = "APIManager";
	
	private static APIManager instance = null;
	
	private GuangJieGouApplication mApplication;
	
	private APIManager(GuangJieGouApplication application) {
		mApplication = application;
	}
	
	public static void init(GuangJieGouApplication application) {
		if(instance == null) {
			instance = new APIManager(application);
		}
	}
	
	public static APIManager getInstance() {
		return instance;
	}
	
	private final String[] img_urls = new String[] {
			"http://img01.taobaocdn.com/imgextra/i1/297744054/T2.UCfXglXXXXXXXXX_%21%21297744054.jpg",
			"http://img02.taobaocdn.com/imgextra/i2/297744054/T2lEOfXdhXXXXXXXXX_%21%21297744054.jpg",
			"http://img02.taobaocdn.com/imgextra/i2/297744054/T215WeXl0XXXXXXXXX_%21%21297744054.jpg",
			"http://img01.taobaocdn.com/imgextra/i1/297744054/T2l62kXbhaXXXXXXXX_!!297744054.jpg"
	};
	private final int PAGE_SIZE = 30;
	public List<ShopItem> listRemenShopItems(int page) {
		int startIndex = page * PAGE_SIZE;
		int endIndex = startIndex + PAGE_SIZE;
		List<ShopItem> itemList = new ArrayList<ShopItem>();
		for(int i = startIndex; i < endIndex; i++) {
			ShopItem item = new ShopItem();
			String imageUrl = img_urls[i % 4];
			
			item.setTitle("[清新实拍] 世界上另一个我" + i);
			item.setShopOwner("Tim" + i);
			item.setPrices("78");
			item.setRecentSaled("26");
			item.setDescription("2013新款欧美大码宽松蝙蝠中裤" + i);
//			item.setShopItemImage("http://img03.taobaocdn.com/imgextra/i3/297744054/T2OQ2jXX4aXXXXXXXX_!!297744054.jpg");
			item.setShopItemImage(imageUrl);
			itemList.add(item);
		}
		return itemList;
	}
	
	public List<ShopItem> listRemenShopItems() {
		List<ShopItem> itemList = new ArrayList<ShopItem>();
		String json = null;
		
		//TODO Get json by call server.
		//...
		
		//Test json data
		try {
			InputStream inSteam = mApplication.getAssets().open("json/remen_items.json");
			json = IOUtil.readInputStream(inSteam);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(json != null) {
			try {
				JSONObject jObject = new JSONObject(json);
				JSONArray items = jObject.getJSONObject("items_list_get_response").getJSONObject("items").getJSONArray("item");
				final int count = items.length();
				for(int i = 0; i < count; i++) {
					ShopItem item = new ShopItem();
					JSONObject itemJObject = items.getJSONObject(i);
					item.initWithJson(itemJObject);
					itemList.add(item);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return itemList;
	}
	
	public RemenShopList listRemenShopList() {
		RemenShopList shopListRequest = new RemenShopList();
		String json = null;
		
		//TODO Get json by call server.
		//...
		
		//Test json data
		try {
			InputStream inSteam = mApplication.getAssets().open("json/remen_shops.json");
			json = IOUtil.readInputStream(inSteam);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(json != null) {
			shopListRequest.initWithJson(json);
		}
		
		return shopListRequest;
	}
	
	public DaPeiList listDaPeiList() {
		DaPeiList daPeiList = new DaPeiList();
		String json = null;
		
		//TODO Get json by call server.
		//...
		
		//Test json data
		try {
			InputStream inSteam = mApplication.getAssets().open("json/dapei.json");
			json = IOUtil.readInputStream(inSteam);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(json != null) {
			daPeiList.initWithJson(json);
		}
		
		return daPeiList;
	}
	
	/**
	 * Call this method in other thread.
	 * @param keyword
	 * @return
	 */
	public List<TaoKeShopItem> searchTaoKeShopItems(String keyword, int pageNo) {
		final List<TaoKeShopItem> retList = new ArrayList<TaoKeShopItem>();
		TopAndroidClient client = TopAndroidClient.getAndroidClientByAppKey(Constants.SDK_TAOBAO_APP_KEY);
		if(client != null) {
			TopParameters params = new TopParameters();
			params.setMethod("taobao.taobaoke.items.get");
			params.addFields("num_iid, title, nick, pic_url, price, click_url, shop_click_url");
			params.addParam("pid", Constants.TAOKE_PID);
			params.addParam("keyword", keyword);
			params.addParam("is_mobile", "true");
			params.addParam("page_size", "40");
			params.addParam("page_no", String.valueOf(pageNo));
			
			client.api(params, SettingsManager.getInstance().getTaobaoUidLong(), new TopApiListener() {
				
				@Override
				public void onException(Exception e) {
					
				}
				
				@Override
				public void onError(ApiError error) {
					
				}
				
				@Override
				public void onComplete(JSONObject json) {
					if(json != null) {
						try {
							int totalResults = json.getJSONObject("taobaoke_items_get_response").getInt("total_results");
							TaoKeShopItem.setSearchResultCount(totalResults);
							JSONArray itemsArray = json.getJSONObject("taobaoke_items_get_response")
									.getJSONObject("taobaoke_items")
									.getJSONArray("taobaoke_item");
							if(itemsArray != null && itemsArray.length() > 0) {
								final int length = itemsArray.length();
								for(int i = 0; i < length; i++) {
									TaoKeShopItem shopItem = new TaoKeShopItem();
									shopItem.initWithJson(itemsArray.getJSONObject(i));
									retList.add(shopItem);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}, false);
		}
		return retList;
	}
}
