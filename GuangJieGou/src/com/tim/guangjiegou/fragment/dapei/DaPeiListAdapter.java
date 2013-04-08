package com.tim.guangjiegou.fragment.dapei;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tim.guangjiegou.pojo.DaPeiItem;
import com.tim.guangjiegou.pojo.DaPeiList;
import com.tim.guangjiegou.service.APIManager;
import com.tim.guangjiegou.view.ItemDaPeiListItem;

public class DaPeiListAdapter extends BaseAdapter {
	
	private Activity mActivity;
	
	private DaPeiList mDaPeiList;
	
	public DaPeiListAdapter(Activity context) {
		mActivity = context;
		mDaPeiList = new DaPeiList();
	}

	@Override
	public int getCount() {
		return mDaPeiList == null ? 0 : mDaPeiList.getDaPeiList().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemDaPeiListItem itemView = null;
		if(convertView == null) {
			itemView = new ItemDaPeiListItem(mActivity);
		}
		else {
			itemView = (ItemDaPeiListItem) convertView;
		}
		DaPeiItem aItem = mDaPeiList.getItem(position);
		if(aItem != null) {
			itemView.setData(aItem);
		}
		return itemView;
	}
	
	public void fetchData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				DaPeiList list = APIManager.getInstance().listDaPeiList();
				if(list != null) {
					if(mDaPeiList != null) {
						mDaPeiList.appendList(list);
					}
					if(mActivity != null) {
						mActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								notifyDataSetChanged();
							}
						});
					}
				}
			}
		}).start();
	}
	
	public void release() {
		mActivity = null;
	}

}
