package lib.tim.tab;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TabBar extends ViewGroup {
	
	private static final String TAG = "TabBar";
	
	private TabItemView mTabItem0;
	private TabItemView mTabItem1;
	private TabItemView mTabItem2;
	private TabItemView mTabItem3;
	private TabItemView mTabItem4;
	
	private TabItemView mSelectedTabItem;
	
	private int mFromIndex;
	private int mEachTabWidth;
	
	private boolean isTouchingTabBar;

	public TabBar(Context context) {
		super(context);
		mEachTabWidth = TabUtils.getScreenWidth(context) / 5;
		initComponent();
	}
	
	private void initComponent() {
		mTabItem0 = constructTabItemView(R.id.tabItem0);
		mTabItem1 = constructTabItemView(R.id.tabItem1);
		mTabItem2 = constructTabItemView(R.id.tabItem2);
		mTabItem3 = constructTabItemView(R.id.tabItem3);
		mTabItem4 = constructTabItemView(R.id.tabItem4);
		
		setTabItemData();
		
		setBackgroundResource(TabConfig.sTabbarBackgroundRes);
		
		addView(mTabItem0);
		addView(mTabItem1);
		addView(mTabItem2);
		addView(mTabItem3);
		addView(mTabItem4);
	}
	
	private TabItemView constructTabItemView(int id) {
		final Context context = getContext();
		final TabItemView itemView = new TabItemView(context);
		itemView.setId(id);
		return itemView;
	}
	
	private void setTabItemData() {
		mTabItem0.setTabInfo(TabConfig.getTabInfoByIndex(0));
		mTabItem0.setTabIndex(0);
		mTabItem1.setTabInfo(TabConfig.getTabInfoByIndex(1));
		mTabItem1.setTabIndex(1);
		mTabItem2.setTabInfo(TabConfig.getTabInfoByIndex(2));
		mTabItem2.setTabIndex(2);
		mTabItem3.setTabInfo(TabConfig.getTabInfoByIndex(3));
		mTabItem3.setTabIndex(3);
		mTabItem4.setTabInfo(TabConfig.getTabInfoByIndex(4));
		mTabItem4.setTabIndex(4);
	}
	
	public TabItemView getTabItemViewByTabIndex(int index) {
		TabItemView itemView = null;
		final int childCount = getChildCount();
		for(int i = 0; i < childCount; i++) {
			TabItemView view = (TabItemView) getChildAt(i);
			if(view.getTabIndex() == index) {
				itemView = view;
				break;
			}
		}
		return itemView;
	}
	
	public TabItemView getTabItemViewByTabInfoIfExist(TabInfo tabInfo) {
		TabItemView itemView = null;
		if(mTabItem0.getTabInfo() == tabInfo) {
			itemView = mTabItem0;
		}
		else if(mTabItem1.getTabInfo() == tabInfo) {
			itemView = mTabItem1;
		}
		else if(mTabItem2.getTabInfo() == tabInfo) {
			itemView = mTabItem2;
		}
		else if(mTabItem3.getTabInfo() == tabInfo) {
			itemView = mTabItem3;
		}
		else if(mTabItem4.getTabInfo() == tabInfo) {
			itemView = mTabItem4;
		}
		return itemView;
	}

	public void clearHighlight() {
		mTabItem0.setHighlight(false);
		mTabItem1.setHighlight(false);
		mTabItem2.setHighlight(false);
		mTabItem3.setHighlight(false);
		mTabItem4.setHighlight(false);
	}
	
	public void clearPressed() {
		mTabItem0.setPressed(false);
		mTabItem1.setPressed(false);
		mTabItem2.setPressed(false);
		mTabItem3.setPressed(false);
		mTabItem4.setPressed(false);
	}
	
	public List<TabInfo> getResultTabInfos() {
		List<TabInfo> tabInfos = new ArrayList<TabInfo>(5);
		tabInfos.add(getTabItemViewByTabIndex(0).getTabInfo());
		tabInfos.add(getTabItemViewByTabIndex(1).getTabInfo());
		tabInfos.add(getTabItemViewByTabIndex(2).getTabInfo());
		tabInfos.add(getTabItemViewByTabIndex(3).getTabInfo());
		tabInfos.add(getTabItemViewByTabIndex(4).getTabInfo());
//		tabInfos.add(mTabItem0.getTabInfo());
//		tabInfos.add(mTabItem1.getTabInfo());
//		tabInfos.add(mTabItem2.getTabInfo());
//		tabInfos.add(mTabItem3.getTabInfo());
//		tabInfos.add(mTabItem4.getTabInfo());
		return tabInfos;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int rawX = (int) event.getRawX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouchingTabBar = true;
			mSelectedTabItem = getTabItemByTouchX(rawX);
			if(mSelectedTabItem != null) {
				mSelectedTabItem.setPressed(true);
				if(mSelectedTabItem.getTabInfo().isShowInGridView()) {
					mFromIndex = mSelectedTabItem.getTabIndex();
				}
				else {
					mSelectedTabItem = null;	//如果是Edit tab的話，取消選中.
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if(mSelectedTabItem != null) {
				mSelectedTabItem.layout(rawX, mSelectedTabItem.getTop(), rawX + mSelectedTabItem.getWidth(), mSelectedTabItem.getBottom());
				TabItemView target = getTabItemByTouchX(rawX);
				if(target != null && target != mSelectedTabItem && target.getTabInfo().isShowInGridView()) {
					final int left = mFromIndex * mEachTabWidth;
					target.layout(left, target.getTop(), left + target.getWidth(), target.getBottom());
					int targetIndex = target.getTabIndex();
					target.setTabIndex(mSelectedTabItem.getTabIndex());
					mSelectedTabItem.setTabIndex(targetIndex);
					mFromIndex = targetIndex;
				}
			}
			break;
			
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			clearPressed();
			isTouchingTabBar = false;
			if(mSelectedTabItem != null) {
				final int left = mFromIndex * mEachTabWidth;
				mSelectedTabItem.layout(left, mSelectedTabItem.getTop(), left + mSelectedTabItem.getWidth(), mSelectedTabItem.getBottom());
				mSelectedTabItem = null;
			}
			break;
		}
		return true;
	}
	
	public TabItemView getTabItemByTouchX(int rawX) {
		TabItemView view = null;
		final int tabIndex = rawX / mEachTabWidth;
		final int childCount = getChildCount();
		for(int i = 0; i < childCount; i++) {
			TabItemView itemView = (TabItemView) getChildAt(i);
			if(itemView.getTabIndex() == tabIndex) {
				view = itemView;
				break;
			}
		}
		return view;
	}
	
	public boolean isTouchingTabBar() {
		return isTouchingTabBar;
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int barHeight = TabUtils.getPxValue(getContext(), TabConfig.sTabbarHeight, 45);
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            child.measure(MeasureSpec.makeMeasureSpec(mEachTabWidth, MeasureSpec.EXACTLY), 
            		MeasureSpec.makeMeasureSpec(barHeight, MeasureSpec.EXACTLY));
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i = 0; i <  getChildCount(); i++) {
			TabItemView tabItemView = (TabItemView) getChildAt(i);
			final int left = tabItemView.getTabIndex() * mEachTabWidth;
			tabItemView.layout(left, 0, left + tabItemView.getMeasuredWidth(), tabItemView.getMeasuredHeight());
		}
	}
}
