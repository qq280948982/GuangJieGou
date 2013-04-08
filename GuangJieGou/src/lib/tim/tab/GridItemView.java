package lib.tim.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.MotionEvent;

public class GridItemView extends TabItemView {
	
	private static final String TAG = "GridItemView";
	
	private boolean isPressing;
	
	private OnTouchListener mOnTouchListener;
	
	public GridItemView(Context context) {
		super(context);
		super.setWillNotDraw(false);	//Enable call onDraw(canvas).
		mText.setTextColor(TabConfig.sTabItemNormalTextColor);
	}
	
	public void setSelected(boolean selected) {
		
	}

	public void setPressed(boolean isPressing) {
		this.isPressing = isPressing;
		postInvalidate();
	}
	
	public boolean isPressing() {
		return isPressing;
	}
	
	public void setText(int resId) {
		mTextRes = resId;
		if(resId > 0) {
			mText.setText(mTextRes);
		}
	}
	
	@Override
	public void setTabInfo(TabInfo tabInfo) {
		mTabInfo = tabInfo;
		setText(tabInfo.getTabName());
		setDrawable(tabInfo.getTabDrawableRes());
		setSelected(tabInfo.isSelected());
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		mOnTouchListener = l;
//		super.setOnTouchListener(l);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(mOnTouchListener != null) {
				mOnTouchListener.onTouch(this, event);
			}
			setPressed(true);
		}
//		super.onTouchEvent(event);
		return false; 
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(isPressing) {
			canvas.drawColor(0x99000000);
		}
	}
}
