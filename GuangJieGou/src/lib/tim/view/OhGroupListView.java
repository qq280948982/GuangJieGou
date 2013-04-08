package lib.tim.view;

import lib.tim.view.adapter.AbsOhGroupListAdapter;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;

public class OhGroupListView extends FrameLayout {
	private static final String TAG="ItemSpecialListView";
	protected ExpandableListView mExpandableListView;
//	protected ItemSTGroup topItemSTGroup;// 顶部group item view
//	protected ItemSTScrollBar itemSTScrollBar;
//	private int currentShowGroupId;
//	protected int topItemHeight = 0;// expandable list view top group item height
	
	private View mHangGroupItemView;
	
	public OhGroupListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupView();
	}

	public OhGroupListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView();
	}

	public OhGroupListView(Context context) {
		super(context);
		setupView();
	}
	
	private void setupView(){
//		topItemHeight = (int) getContext().getResources().getDimension(R.dimen.item_st_group_height);
//		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		li.inflate(R.layout.item_special_list_view, this);
//		topItemSTGroup = (ItemSTGroup) findViewById(R.id.item_special_list_view_top_st_group);
//		expandableListView = (ExpandableListView) findViewById(R.id.item_special_list_view_expandable_view);
		
		final Context context = getContext();
		mExpandableListView = new ExpandableListView(context);
		mExpandableListView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mExpandableListView.setCacheColorHint(Color.TRANSPARENT);
		mExpandableListView.setFadingEdgeLength(0);
		mExpandableListView.setGroupIndicator(null);
		mExpandableListView.setScrollbarFadingEnabled(true);
		
		addView(mExpandableListView);
		
		
		
//		setupListener();
		mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO 防止group可以点击
				return true;
			}
		});
	}

	private void setupOnScrollListener() {
		
		mExpandableListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(mHangGroupItemView == null) {
					return ;
				}
				if(mExpandableListView.getExpandableListAdapter() == null) {
					return ;
				}
				if(mExpandableListView.getExpandableListAdapter().getGroupCount() <= 0) {
					mHangGroupItemView.setVisibility(View.GONE);
					return ;
				}
				final int hangGroupItemHeight = mHangGroupItemView.getHeight();
				int ptp = view.pointToPosition(0, 0);
//				Log.d(TAG, "hangGroupItemHeight >> " + mHangGroupItemView.getWidth());
				if (ptp != AdapterView.INVALID_POSITION) {
					long pos = mExpandableListView.getExpandableListPosition(ptp);
					int groupPos = ExpandableListView.getPackedPositionGroup(pos);
					int childPos = ExpandableListView.getPackedPositionChild(pos);
					int childSize = mExpandableListView.getExpandableListAdapter().getChildrenCount(groupPos);
					// Log.i(TAG, "groupPos:" + groupPos + " childPos:" + childPos + " childSize:" + childSize);
					// Log.i(TAG,"childCount:"+elv.getCount()+" "+elv.getChildCount());

					String groupName = (String) mExpandableListView.getExpandableListAdapter().getGroup(groupPos);
					if (groupName.equals("*")) {
						mHangGroupItemView.setVisibility(View.GONE);
					} else {
						Rect r = new Rect();
						View topChildview = mExpandableListView.getChildAt(0);
						topChildview.getHitRect(r);
						if (childPos == (childSize - 1)) {
							if (r.bottom <= hangGroupItemHeight) {
//								 Log.d(TAG, "top item move up");
								mHangGroupItemView.scrollTo(0, hangGroupItemHeight - r.bottom);
							}
//							 Log.d(TAG, "Rect:" + r.toString() + " topItemHeight:" + hangGroupItemHeight);
						} else if (childPos == 0 || childPos == (childSize - 2) || (childPos == -1)) {

							mHangGroupItemView.scrollTo(0, 0);
						}
						mHangGroupItemView.setVisibility(View.VISIBLE);// 设置顶部group bar
//						topItemSTGroup.setData((String) expandableListView.getExpandableListAdapter().getGroup(groupPos));
						((AbsOhGroupListAdapter) mExpandableListView.getExpandableListAdapter()).updateHangGroup(mHangGroupItemView, groupPos);
					}
				}

			}
		});
	}

	public void setAdapter(AbsOhGroupListAdapter adapter) {
		if(adapter != null) {
			DataSetObserver observer = new DataSetObserver() {

				@Override
				public void onChanged() {
					super.onChanged();
					post(new Runnable() {
						
						@Override
						public void run() {
							setupOnScrollListener();
						}
					});
				}
				
			};
			adapter.registerDataSetObserver(observer);
			mExpandableListView.setAdapter(adapter);
			final int count = adapter.getGroupCount();
			for(int i = 0; i < count; i++) {
				mExpandableListView.expandGroup(i);
			}
			postDelayed(new Runnable() {
				
				@Override
				public void run() {
					setupOnScrollListener();
				}
			}, 100);
		}
	}
	
	public void setHangGroupItemView(View view) {
		if(mHangGroupItemView != null) {
			return ;
		}
		mHangGroupItemView = view;
		if(mHangGroupItemView != null) {
			addView(mHangGroupItemView);
			postDelayed(new Runnable() {
				
				@Override
				public void run() {
					setupOnScrollListener();
				}
			}, 100);
		}
	}
	
	public void release() {
		mHangGroupItemView = null;
		if(mExpandableListView != null) {
			mExpandableListView.setOnChildClickListener(null);
			AbsOhGroupListAdapter adapter = (AbsOhGroupListAdapter) mExpandableListView.getExpandableListAdapter();
			if(adapter != null) {
				adapter.release();
				adapter = null;
			}
		}
	}
	
	public void addFooterView(View view) {
		if(mExpandableListView != null) {
			mExpandableListView.addFooterView(view);
			
		}
	}
	
	public void addFooterView(View v, Object data, boolean isSelectable) {
		if(mExpandableListView != null) {
			mExpandableListView.addFooterView(v, data, isSelectable);
		}
	}
	
	public void addHeaderView(View v) {
		if(mExpandableListView != null) {
			mExpandableListView.addHeaderView(v);
		}
	}
	
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		if(mExpandableListView != null) {
			mExpandableListView.addHeaderView(v, data, isSelectable);
		}
	}
	
	public void setOnChildClickListener(OnChildClickListener l) {
		if(mExpandableListView != null) {
			mExpandableListView.setOnChildClickListener(l);
		}
	}
	
	public ExpandableListView getExpandableListView() {
		return mExpandableListView;
	}
}
