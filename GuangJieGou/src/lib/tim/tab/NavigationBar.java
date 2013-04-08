package lib.tim.tab;

import lib.tim.view.OhTextView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class NavigationBar extends RelativeLayout implements OnClickListener {
	
	private RelativeLayout mRootLayout;
	private OhTextView mTitle;
	private OhTextView mDoneBtn;
	
	private OnClickDoneListener mOnClickDoneListener;

	public NavigationBar(Context context) {
		super(context);
		if(!TabConfig.sEnableNavigationBar) {
			return ;
		}
		initComponent();
	}
	
	private void initComponent() {
		removeAllViews();
		final Context context = getContext();
		
		mRootLayout = new RelativeLayout(context);
		int barHeight = TabUtils.getPxValue(context, TabConfig.sNavigationBarHeight, 45);
		mRootLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, barHeight));
		mRootLayout.setBackgroundResource(TabConfig.sNavigationBarBackgroundRes);
		
		mTitle = new OhTextView(context);
		LayoutParams titleLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		mTitle.setLayoutParams(titleLP);
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, TabUtils.getValue(TabConfig.sNavigationBarTitleTextSize, 22));
		mTitle.setText(TabConfig.sNavigationBarTitleTextRes);
		mTitle.setTypeface(null, Typeface.BOLD);
		mRootLayout.addView(mTitle);
		
		int doneBtnWidth = TabUtils.getPxValue(context, TabConfig.sNavigationBarDoneButtonWidth, 60);
		int doneBtnHeight = TabUtils.getPxValue(context, TabConfig.sNavigationBarDoneButtonHeight, 30);
		mDoneBtn = new OhTextView(context);
		LayoutParams buttonLP = new LayoutParams(doneBtnWidth, doneBtnHeight);
		buttonLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		buttonLP.addRule(RelativeLayout.CENTER_VERTICAL);
		buttonLP.rightMargin = TabUtils.dip2px(context, 10);
		mDoneBtn.setLayoutParams(buttonLP);
		mDoneBtn.setTextColor(Color.WHITE);
		mDoneBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, TabUtils.getValue(TabConfig.sNavigationBarDoneButtonTextSize, 14));
		mDoneBtn.setText(TabConfig.sNavigationBarDoneTextRes);
		mDoneBtn.setGravity(Gravity.CENTER);
		mDoneBtn.setBackgroundResource(TabConfig.sNavigationBarDoneButtonBackgroundRes);
		mRootLayout.addView(mDoneBtn);
		
		addView(mRootLayout);
		
		mDoneBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(mOnClickDoneListener != null) {
			mOnClickDoneListener.onClickDone();
		}
	}
	
	public void release() {
		mOnClickDoneListener = null;
	}
	
	public void setOnClickDoneListener(OnClickDoneListener l) {
		mOnClickDoneListener = l;
	}
	
	static interface OnClickDoneListener {
		void onClickDone();
	}
}
