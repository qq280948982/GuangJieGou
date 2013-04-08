package lib.tim.tab;

import lib.tim.view.OhTextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabItemView extends LinearLayout {
	
	private static final String TAG = "TabItemView";
	
	protected ImageView mIcon;
	protected TextView mText;
	
	private int index;
	protected int mTextRes;
	protected int mDrawableRes;
	private boolean isHighlight;
	
	protected TabInfo mTabInfo;
	
	private static Bitmap sHighlightBitmap;
	
	private int mSrcTextSize;
	
	public TabItemView(Context context) {
		super(context);
		initComponent();
		setWillNotDraw(false);
	}
	
	private void initComponent() {
		removeAllViews();
		final Context context = getContext();
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mIcon = new ImageView(context);
		mText = new OhTextView(context);
		
		int iconHeight = TabUtils.getPxValue(context, TabConfig.sTabItemDrawbleHeight, 28);
		mIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, iconHeight));
		
		LayoutParams textLP = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		textLP.gravity = Gravity.CENTER_HORIZONTAL;
		mText.setLayoutParams(textLP);
		mText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		mText.setSingleLine(true);
		
		mSrcTextSize = TabUtils.getValue(TabConfig.sTabItemTextSize, 10);
		
		mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSrcTextSize);
		
		addView(mIcon);
		addView(mText);
	}
	
	public void setTabInfo(TabInfo tabInfo) {
		mTabInfo = tabInfo;
		setText(tabInfo.getTabName());
		setDrawable(tabInfo.getTabDrawableRes());
		setSelected(tabInfo.isSelected());
		tabInfo.setTabbarIndex(index);
	}
	
	public TabInfo getTabInfo() {
		return mTabInfo;
	}
	
	public int getTextResId() {
		return mTextRes;
	}
	
	public void setText(int resId) {
		mTextRes = resId;
		if(resId > 0) {
			mText.setText(mTextRes);
			if(TabConfig.sAutoScaleTabItemTextSize) {
				if(TabUtils.getScreenWidth(getContext()) <= 480) {
					final String text = mText.getText().toString();
					if(text.length() >= 14) {
						final int scaleTextSize = mSrcTextSize - 2;
						mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaleTextSize);
					}
					else {
						mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSrcTextSize);
					}
				}
			}
		}
	}
	
	public int getDrawableResId() {
		return mDrawableRes;
	}
	
	public void setDrawable(int resId) {
		mDrawableRes = resId;
		mIcon.setImageResource(mDrawableRes);
	}
	
	@Override
	public void setSelected(boolean selected) {
		if(mIcon != null) {
			mIcon.setSelected(selected);
		}
		if(mText != null) {
			mText.setTextColor(selected ? TabConfig.sTabItemSelectedTextColor : TabConfig.sTabItemNormalTextColor);
		}
		setBackgroundResource(selected ? TabConfig.sTabItemSelectedBackgroundRes : 0);
	}
	
	@Override
	public boolean isSelected() {
		return mIcon != null && mIcon.isSelected();
	}

	@Override
	public void setPressed(boolean pressed) {
		if(mTabInfo != null && !mTabInfo.isShowInGridView()) {
			if(mIcon != null) {
				mIcon.setPressed(pressed);
			}
			setBackgroundColor(pressed ? 0x99000000: Color.TRANSPARENT);
		}
	}

	@Override
	public boolean isPressed() {
		return mIcon != null && mIcon.isPressed();
	}

	public void setHighlight(boolean highlight) {
		this.isHighlight = highlight;
		invalidate();
	}
	
	public void setTabIndex(int index) {
		this.index = index;
		if(mTabInfo != null) {
			mTabInfo.setTabbarIndex(index);
		}
	}
	
	public int getTabIndex() {
		return index;
	}
	
	public static void destoryHighlightBitmap() {
		if(sHighlightBitmap != null) {
			sHighlightBitmap.recycle();
			sHighlightBitmap = null;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(sHighlightBitmap == null && TabConfig.sTabItemHighlightRes > 0) {
			final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), TabConfig.sTabItemHighlightRes);
			final int width = getWidth() - TabUtils.dip2px(getContext(), 10);
			final int height = getHeight();
			sHighlightBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
			bitmap.recycle();
		}
		if(isHighlight && sHighlightBitmap != null && !sHighlightBitmap.isRecycled()) {
			canvas.drawBitmap(sHighlightBitmap, 0, 0, null);
		}
	}
}
