package lib.tim.tab;

import java.util.List;

import lib.tim.tab.NavigationBar.OnClickDoneListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;


public class EditPage extends RelativeLayout implements OnTouchListener {
	
	private static final String TAG = "EditPage";
	
	private NavigationBar mNavigationBar;
	private GridContainerView mGridContainer;
	private TabBar mTabBar;

	private int mFlowTabWidth;
	private int mFlowTabHeight;
	private int mFlowTabX;
	private int mFlowTabY;
	private Bitmap mFlowTabBitmap;
	private GridItemView mSelectGridView;
	private TabItemView mSelectTabView;
	
	public EditPage(Context context) {
		super(context);
		setWillNotDraw(false);	//Enable onDraw(canvas);
		initComponent();
	}
	
	private void initComponent() {
		final Context context = getContext();
		
		//Init navigation bar.
		mNavigationBar = new NavigationBar(context);
		mNavigationBar.setId(R.id.navigation_bar);
		LayoutParams navLP = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		navLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mNavigationBar.setLayoutParams(navLP);
		
		//Init grid container.
		mGridContainer = new GridContainerView(context);
		mGridContainer.setId(R.id.grid_view);
		LayoutParams gridLP = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		gridLP.addRule(RelativeLayout.BELOW, R.id.navigation_bar);
		gridLP.addRule(RelativeLayout.ABOVE, R.id.tab_bar);
		mGridContainer.setLayoutParams(gridLP);
		mGridContainer.setOnTouchListener(this);
		
		//Init tabbar.
		mTabBar = new TabBar(context);
		mTabBar.setId(R.id.tab_bar);
		LayoutParams tabbarLP = new LayoutParams(LayoutParams.FILL_PARENT, TabUtils.getPxValue(getContext(), TabConfig.sTabbarHeight, 45));
		tabbarLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mTabBar.setLayoutParams(tabbarLP);
		
		addView(mNavigationBar);
		addView(mGridContainer);
		addView(mTabBar);
	}
	
	/**
	 * EditPage自身的OnTouchEvent處理，交由統一的onTouch(v, event)來做.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return onTouch(this, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int rawX = (int) event.getRawX();
		final int rawY = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(v instanceof GridItemView) {
				mSelectGridView = (GridItemView) v;
				mFlowTabWidth = (int) (v.getWidth() * 1.5);
				mFlowTabHeight = (int) (v.getHeight() * 1.5);
				fixFlowTabOffset(rawX, rawY);
				buildFlowTabBitmap(mSelectGridView);
				invalidate();
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			fixFlowTabOffset(rawX, rawY);
			mTabBar.clearHighlight();
			mSelectTabView = null;
			invalidate();
			if(mSelectGridView != null && rawY > getHeight() - mTabBar.getHeight()) {	//如果手指位置移動到TabBar條上
				final TabItemView tabItemView = getTabItemViewByRawX(rawX);
				if(tabItemView != null && 	//判斷GridItem是否等於TabItem，若相等，則不操作
						mSelectGridView.getTabInfo() != tabItemView.getTabInfo()
						&& tabItemView.getTabInfo().isShowInGridView()) {	//Edit tab不處理
					mSelectTabView = tabItemView;
					mTabBar.clearHighlight();
					tabItemView.setHighlight(true);
				}
			}
			break;
			
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(mGridContainer != null) {
				mGridContainer.clearPressed();
			}
			if(mTabBar != null) {
				mTabBar.clearHighlight();
			}
			if(mSelectTabView != null && mSelectGridView != null) {
				doReplace(mSelectGridView, mSelectTabView);
			}
			mSelectTabView = null;
			mSelectGridView = null;
			if(mFlowTabBitmap != null) {
				mFlowTabBitmap.recycle();
			}
			invalidate();
			break;
		}
		return true;
	}
	
	private TabItemView getTabItemViewByRawX(int rawX) {
		//假設屏幕寬度為480px, 每個tabItem的寬度就為480 / 5 = 96px, 
		//假設當前手指X座標為150, 則可以知道現在所選的tab的index為：150 / 96 = 1, 即第二個tab
		TabItemView itemView = null;
		itemView = mTabBar.getTabItemByTouchX(rawX);
		return itemView;
	}
	
	private void fixFlowTabOffset(int rawX, int rawY) {
		mFlowTabX =  (rawX - mFlowTabWidth / 2);
		mFlowTabY = (rawY - mFlowTabHeight * 2) + 10;
//		mFlowTabX = (int) rawX;
//		mFlowTabY = (int) rawY;
	}
	
	private void buildFlowTabBitmap(GridItemView view) {
		view.setWillNotCacheDrawing(false);
		view.setDrawingCacheBackgroundColor(0);
		view.buildDrawingCache();
		mFlowTabBitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), mFlowTabWidth, mFlowTabHeight, false);
		view.destroyDrawingCache();
		invalidate();
	}
	
	private void doReplace(GridItemView gridItem, TabItemView tabItem) {
		final TabInfo gridInfo = gridItem.getTabInfo();
		final TabInfo tabInfo = tabItem.getTabInfo();
		final int gridInfoIndex = gridInfo.getTabbarIndex();
		final int tabInfoIndex = tabInfo.getTabbarIndex();
//		Log.e(TAG, "From: " + getString(gridInfo.getTabName()) + ", index: " + gridInfoIndex);
//		Log.e(TAG, "To: " + getString(tabInfo.getTabName()) + ", index: " + tabInfoIndex);
		if(gridInfo != tabInfo) {
			final TabItemView gridExistInTabItemView = mTabBar.getTabItemViewByTabInfoIfExist(gridInfo); 
			if(gridExistInTabItemView != null) {	//所選的girdItemView也在TabBar中, 則替換TabBar中兩者的位置
				gridExistInTabItemView.setTabInfo(tabInfo);
			}
			else {
				tabInfo.setTabbarIndex(-99);
			}
			tabItem.setTabInfo(gridInfo);
//			tabInfo.setTabbarIndex(gridInfoIndex);
		}
	}
	
	private String getString(int res) {
		return getResources().getString(res);
	}
	
	public void setOnClickDoneListener(OnClickDoneListener l) {
		if(mNavigationBar != null) {
			mNavigationBar.setOnClickDoneListener(l);
		}
	}
	
	public List<TabInfo> getResultTabInfos() {
		if(mTabBar != null) {
			return mTabBar.getResultTabInfos();
		}
		return null;
	}

	@Override
	public void draw(Canvas canvas) {
		if(mFlowTabBitmap != null && !mFlowTabBitmap.isRecycled()) {
			canvas.save();
			super.draw(canvas);
			canvas.drawBitmap(mFlowTabBitmap, mFlowTabX, mFlowTabY, null);
			canvas.restore();
		}
		else {
			super.draw(canvas);
		}
	}
	
	public void release() {
		if(mFlowTabBitmap != null) {
			mFlowTabBitmap.recycle();
			mFlowTabBitmap = null;
		}
		if(mGridContainer != null) {
			mGridContainer.release();
		}
		if(mNavigationBar != null) {
			mNavigationBar.release();
		}
		TabItemView.destoryHighlightBitmap();
		mSelectGridView = null;
		mSelectTabView = null;
	}
}
