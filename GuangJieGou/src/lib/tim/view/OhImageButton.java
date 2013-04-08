package lib.tim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tim.guangjiegou.R;

public class OhImageButton extends LinearLayout {
	
	private static final String TAG = "OhImageButton";
	
	private static Typeface customTypefaceNormal=null;
	private static Typeface customTypefaceBold=null;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_SELECTED = 1;
	public final static int STATE_OTHER = 2;

	private ImageView mImageView;
	private OhTextView mTextView;
	
	private int mDrawableRes;
	private int mDisableDrawableRes;
	private int mTextRes;
	private float mTextSize;
	private int mTextColor = Color.GRAY;
	private float mIconWidth;
	private float mIconHeight;
	private boolean isMultiState;
	private int mState;
	
	private OnClickListener mOnClickListener;
	
	private int mClickTimes;
	
	public OhImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OhImageButtonStyle);
		mDrawableRes = a.getResourceId(R.styleable.OhImageButtonStyle_iconDrawable, 0);
		mDisableDrawableRes = a.getResourceId(R.styleable.OhImageButtonStyle_disableIconDrawable, 0);
//		mText = a.getString(R.styleable.OhImageButtonStyle_text);
		mTextRes = a.getResourceId(R.styleable.OhImageButtonStyle_text, 0);
		mTextSize = a.getDimension(R.styleable.OhImageButtonStyle_textSize, 0);
		mTextColor = a.getColor(R.styleable.OhImageButtonStyle_textColor, Color.GRAY);
		mIconWidth = a.getDimension(R.styleable.OhImageButtonStyle_iconWidth, 0);
		mIconHeight = a.getDimension(R.styleable.OhImageButtonStyle_iconHeight, 0);
		isMultiState = a.getBoolean(R.styleable.OhImageButtonStyle_multiState, false);
		a.recycle();
		initComponent();
	}

	public OhImageButton(Context context, int drawableRes, String text) {
		super(context);
		mDrawableRes = drawableRes;
		initComponent();
		mTextView.setText(text);
	}
	
	public OhImageButton(Context context, int drawableRes, int text) {
		super(context);
		mDrawableRes = drawableRes;
		mTextRes = text;
		initComponent();
	}
	
	private void initComponent() {
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		mClickTimes = 0;
		mImageView = new ImageView(getContext());
		if(mDrawableRes > 0) {
			mImageView.setBackgroundResource(mDrawableRes);
		}
		mTextView = new OhTextView(getContext());
		if(mTextRes > 0) {
			mTextView.setTextRes(mTextRes);
		}
		mTextView.setTextColor(mTextColor);
		mTextView.setGravity(Gravity.CENTER);
		if(mTextSize > 0) {
			mTextView.setTextSize(mTextSize);
		}
		if(mIconWidth > 0 && mIconHeight > 0) {
			final LayoutParams lp = new LayoutParams((int) mIconWidth, (int) mIconHeight);
			mImageView.setLayoutParams(lp);
		}
		addView(mImageView);
		addView(mTextView);
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!isEnabled()) {
					return true;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mImageView.setPressed(true);
					break;

				case MotionEvent.ACTION_UP:
					mImageView.setPressed(false);
					if(isMultiState) {
						mClickTimes = ++mClickTimes > 2 ? 0 : mClickTimes;
						mState = mClickTimes == 0 ? STATE_NORMAL : mClickTimes == 1 ? STATE_SELECTED : STATE_OTHER;
						
//							mImageView.setEnabled(mClickTimes == 0);
//							mImageView.setSelected(mClickTimes == 1);
					}
					else {
						mState = mState == STATE_NORMAL ? STATE_SELECTED : STATE_NORMAL;
					}
//						Log.d("Log", "state:" + mState);
					loadState();
					if(mOnClickListener != null) {
						playSoundEffect(SoundEffectConstants.CLICK);
						mOnClickListener.onClick(OhImageButton.this);
					}
				}
				return true;
			}
		});
	}
	
	public OhTextView getTextView() {
		return mTextView;
	}
	
	public ImageView getImageView() {
		return mImageView;
	}
	
	public int getState() {
		return mState;
	}
	
	public void setState(int state) {
		mState = state;
		loadState();
	}
	
	private void loadState() {
//		Log.d("Log", "LoadState >> " + mState);
		if(mImageView != null) {
//			Log.d("Log", "mImageView not null");
			switch (mState) {
			case STATE_NORMAL:
				mImageView.setEnabled(true);
				mImageView.setSelected(false);
				break;

			case STATE_SELECTED:
				mImageView.setEnabled(isMultiState);
				mImageView.setSelected(true);
				break;
				
			case STATE_OTHER:
				mImageView.setEnabled(false);
				mImageView.setSelected(false);
				break;
			}
		}
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		mOnClickListener = l;
	}

	public void setMultiState(boolean isMultiState) {
		this.isMultiState = isMultiState;
	}
	
	public boolean isMultiState() {
		return isMultiState;
	}
	
	@Override
	public void setSelected(boolean selected) {
		if(mImageView != null) {
			mImageView.setSelected(selected);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(mImageView != null) {
			mImageView.setBackgroundResource(enabled ? mDrawableRes : mDisableDrawableRes);
		}
	}
	
//	@Override
//	public void setTypeface(Typeface tf, int style) {
//		Typeface paramTf=tf;
//		if(style==Typeface.BOLD_ITALIC){
//			paramTf=customTypefaceBold;
//		}
//		else if(style==Typeface.BOLD){
//			paramTf=customTypefaceBold;
//		}
//		else if(style==Typeface.ITALIC){
//			paramTf=customTypefaceNormal;
//		}
//		else{
//			paramTf=customTypefaceNormal;
//		}
//		if(paramTf==null){
//			paramTf=tf;
//		}
//		super.setTypeface(paramTf, style);
//	}

	public static void setupCustomTypeface(Context context, String assetPathNormal,String assetPathBold){
		if(assetPathNormal!=null){
			customTypefaceNormal=Typeface.createFromAsset (context.getAssets(),assetPathNormal);
		}
		if(assetPathBold!=null){
			customTypefaceBold=Typeface.createFromAsset (context.getAssets(),assetPathBold);
		}
	}
	
	public void updateText() {
		if(mTextView != null) {
			mTextView.updateText();
		}
	}
	
	public void release() {
		mOnClickListener = null;
	}
}
