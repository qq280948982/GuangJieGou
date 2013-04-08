package com.tim.guangjiegou.pojo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DaPeiList extends BaseAPI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7681123093773163752L;
	
	private List<DaPeiItem> daPeiList = new ArrayList<DaPeiItem>();
	
	@Override
	public void initWithJson(String json) {
		super.initWithJson(json);
		if(json != null) {
			daPeiList.clear();
			try {
				JSONArray itemArray = new JSONObject(json).getJSONArray("subjects");
				if(itemArray != null && itemArray.length() > 0) {
					final int length = itemArray.length();
					for(int i = 0; i < length; i++) {
						DaPeiItem item = new DaPeiItem();
						item.initWithJson(itemArray.getJSONObject(i));
						daPeiList.add(item);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public List<DaPeiItem> getDaPeiList() {
		return daPeiList;
	}
	
	public void appendList(DaPeiList otherList) {
		if(otherList != null && otherList.getDaPeiList() != null) {
			List<DaPeiItem> otherDaPeiList = otherList.getDaPeiList();
			final int size = otherDaPeiList.size();
			for(int i = 0; i < size; i++) {
				DaPeiItem otherAItem = otherDaPeiList.get(i);
				if(!daPeiList.contains(otherAItem)) {
					daPeiList.add(otherAItem);
				}
			}
		}
	}
	
	public DaPeiItem getItem(int index) {
		DaPeiItem item = null;
		if(index >= 0 && index < daPeiList.size()) {
			item = daPeiList.get(index);
		}
		return item;
	}
}
