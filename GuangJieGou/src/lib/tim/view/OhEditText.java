package lib.tim.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

//TODO 未完整测试各种效果：粗斜体，斜体，粗体。。。
public class OhEditText extends EditText {

	private static Typeface customTypefaceNormal=null;
	private static Typeface customTypefaceBold=null;
	
	private OnSoftKeyboardHideListener mOnSoftKeyboardHideListener;

	public OhEditText(Context context) {
		super(context);
	}
	
	public OhEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public OhEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setTypeface(Typeface tf, int style) {
		Typeface paramTf=tf;
		if(style==Typeface.BOLD_ITALIC){
			paramTf=customTypefaceBold;
		}
		else if(style==Typeface.BOLD){
			paramTf=customTypefaceBold;
		}
		else if(style==Typeface.ITALIC){
			paramTf=customTypefaceNormal;
		}
		else{
			paramTf=customTypefaceNormal;
		}
		if(paramTf==null){
			paramTf=tf;
		}
		super.setTypeface(paramTf, style);
	}

	public static void setupCustomTypeface(Context context, String assetPathNormal,String assetPathBold){
		if(assetPathNormal!=null){
			customTypefaceNormal=Typeface.createFromAsset (context.getAssets(),assetPathNormal);
		}
		if(assetPathBold!=null){
			customTypefaceBold=Typeface.createFromAsset (context.getAssets(),assetPathBold);
		}
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if(mOnSoftKeyboardHideListener != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			 KeyEvent.DispatcherState state = getKeyDispatcherState();
	            if (state != null) {
	                if (event.getAction() == KeyEvent.ACTION_DOWN
	                        && event.getRepeatCount() == 0) {
	                    state.startTracking(event, this);
	                    return true;
	                } else if (event.getAction() == KeyEvent.ACTION_UP
	                        && !event.isCanceled() && state.isTracking(event)) {
	                	mOnSoftKeyboardHideListener.onKeyboardHide();
	                    return true;
	                }
	            }
		}
		return super.dispatchKeyEventPreIme(event);
	}
	
	public void setOnSoftKeyboardHideListener(OnSoftKeyboardHideListener l) {
		mOnSoftKeyboardHideListener = l;
	}
	
	public static interface OnSoftKeyboardHideListener {
		void onKeyboardHide();
	}
}
