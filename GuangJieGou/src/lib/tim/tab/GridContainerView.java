package lib.tim.tab;

import java.util.ArrayList;
import java.util.List;

import lib.tim.view.OhTextView;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;

public class GridContainerView extends FrameLayout {
	
	private GridView mGridView;
	private OhTextView mDescriptionTextView;	//Options.
	
	private List<TabInfo> mAllTabInfos = TabConfig.sTabInfoList;
	private List<TabInfo> mGridTabInfos;
	
	private OnTouchListener mOnTouchListener;

	public GridContainerView(Context context) {
		super(context);
		if(TabConfig.sGridViewBackgroundRes > 0) {
			setBackgroundResource(TabConfig.sGridViewBackgroundRes);
		}
		else {
			setBackgroundColor(TabConfig.sGridViewBackgroundColor);
		}
		initComponent();
	}
	
	private void initComponent() {
		final Context context = getContext();
		mGridView = new GridView(context);
		mGridView.setNumColumns(4);
		mGridView.setVerticalSpacing(TabUtils.dip2px(context, 20));
		mGridView.setGravity(Gravity.CENTER_HORIZONTAL);
		final int gridPadding = TabUtils.dip2px(context, 10);
		mGridView.setPadding(gridPadding, gridPadding, gridPadding, gridPadding);
		mGridView.setAdapter(new GridAdapter());
		mGridView.setEnabled(false);
		mGridView.setClickable(false);
		
		addView(mGridView);
		
		if(TabConfig.sDescriptionStringRes > 0) {
			mDescriptionTextView = new OhTextView(context);
			mDescriptionTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
			final int padding = TabUtils.dip2px(context, 20);
			mDescriptionTextView.setPadding(padding, padding, padding, padding);
			mDescriptionTextView.setText(TabConfig.sDescriptionStringRes);
			mDescriptionTextView.setTextColor(0xff909090);
			mDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			addView(mDescriptionTextView);
		}
	}
	
	@Override
	public void setOnTouchListener(OnTouchListener l) {
		mOnTouchListener = l;
	}
	
	public final void clearPressed() {
		if(mGridView != null) {
			final int count = mGridView.getChildCount();
			for(int i = 0; i < count; i++) {
				mGridView.getChildAt(i).setPressed(false);
			}
		}
	}
	
	public void release() {
		if(mAllTabInfos != null) {
			mAllTabInfos.clear();
			mAllTabInfos = null;
		}
		if(mGridTabInfos != null) {
			mGridTabInfos.clear();
			mGridTabInfos = null;
		}
		mOnTouchListener = null;
	}
	
//--- Adapter class.	
	private class GridAdapter extends BaseAdapter {
		
		GridAdapter() {
			mGridTabInfos = new ArrayList<TabInfo>();
			final int size = mAllTabInfos.size();
			for(int i = 0; i < size; i++) {
				if(mAllTabInfos.get(i).isShowInGridView()) {
					mGridTabInfos.add(mAllTabInfos.get(i));
				}
			}
		}

		@Override
		public int getCount() {
			return mGridTabInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return mGridTabInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public boolean areAllItemsEnabled() {	//Disable click.
			return false;
		}

		@Override
		public boolean isEnabled(int position) {	//Disable click.
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GridItemView itemView = null;
			if(convertView == null) {
				itemView = new GridItemView(getContext());
				if(mOnTouchListener != null) {
					itemView.setOnTouchListener(mOnTouchListener);
				}
			}
			else {
				itemView = (GridItemView) convertView;
			}
			itemView.setTabInfo(mGridTabInfos.get(position));
			return itemView;
		}
	}
}
