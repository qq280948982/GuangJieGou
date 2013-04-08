package com.tim.guangjiegou.pojo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DaPeiItem extends VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7920283446440368389L;
	
	private String title;
	private String description;
	private String front_cover_url;
	private String create_date;
	private List<DaPeiItem.ShopItem> items = new ArrayList<DaPeiItem.ShopItem>();
	
	public void initWithJson(JSONObject json) {
		if(json != null) {
			items.clear();
			try {
				title = json.getString("title");
				description = json.getString("description");
				front_cover_url = json.getString("front_cover_url");
				create_date = json.getString("create_date");
				JSONArray itemsArray = json.getJSONArray("items");
				if(itemsArray != null && itemsArray.length() > 0) {
					final int length = itemsArray.length();
					for(int i = 0; i < length; i++) {
						JSONObject itemJObject = itemsArray.getJSONObject(i);
						DaPeiItem.ShopItem shopItem = new DaPeiItem.ShopItem();
						shopItem.content_text = itemJObject.getString("content_text");
						shopItem.image_url = itemJObject.getString("image_url");
						shopItem.shop_iid = itemJObject.getString("shop_iid");
						shopItem.shop_iname = itemJObject.getString("shop_iname");
						items.add(shopItem);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ShopItem {
		private String content_text;
		private String image_url;
		private String shop_iid;
		private String shop_iname;
		
		public String getContent_text() {
			return content_text;
		}
		
		public void setContent_text(String content_text) {
			this.content_text = content_text;
		}
		
		public String getImage_url() {
			return image_url;
		}
		
		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}
		
		public String getShop_iid() {
			return shop_iid;
		}
		
		public void setShop_iid(String shop_iid) {
			this.shop_iid = shop_iid;
		}
		
		public String getShop_iname() {
			return shop_iname;
		}
		
		public void setShop_iname(String shop_iname) {
			this.shop_iname = shop_iname;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFront_cover_url() {
		return front_cover_url;
	}

	public void setFront_cover_url(String front_cover_url) {
		this.front_cover_url = front_cover_url;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public List<DaPeiItem.ShopItem> getItems() {
		return items;
	}

	public void setItems(List<DaPeiItem.ShopItem> items) {
		this.items = items;
	}
	
	@Override
	public boolean equals(Object object) {
		boolean theSame = false;
		if(object != null && object instanceof DaPeiItem && this.title != null && this.description != null) {
			DaPeiItem daPeiItem = (DaPeiItem) object;
			String title = daPeiItem.title;
			String descritpion = daPeiItem.description;
			if(this.title.equals(title) && this.description.equals(descritpion)) {
				theSame = true;
			}
		}
		return theSame;
	}
}
