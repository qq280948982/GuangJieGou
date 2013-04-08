package lib.tim.view.header;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseHeader extends LinearLayout implements View.OnClickListener {
	
	private static final String TAG="BaseHeader";
	
	private TextView titleView=null;
	private FrameLayout frameLayoutLeftButtons=null;
	private FrameLayout frameLayoutRightButtons=null;
	
	private Hashtable<String,View> headerButtons=new Hashtable<String,View>();
	private String keyForBackButton=null;
//	private View viewBackButton=null;
	private static final int VIEW_KEY=1000;
	
	private onClickHeaderButtonListener listener;
	private View lastDisplayLeftButton=null;
	private View lastDisplayRightButton=null;

	public BaseHeader(Context context) {
		super(context);
		
	}

	public BaseHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	protected void findViews(){
		titleView=getTitleView();
		frameLayoutLeftButtons=getFrameLayoutForLeftButtons();
		frameLayoutRightButtons=getFrameLayoutForRightButtons();
	}

	protected abstract TextView getTitleView();
	protected abstract FrameLayout getFrameLayoutForLeftButtons();
	protected abstract FrameLayout getFrameLayoutForRightButtons();
	
	public void setKeyForBackButton(String viewKey){
		keyForBackButton=viewKey;
//		viewBackButton=headerButtons.get(viewKey);
	}
	
	public String getKeyForBackButton(){
		return keyForBackButton;
	}
	
	public void addLeftButton(int resIdDrawable,String viewKey){
		ImageView imageView=new ImageView(getContext());
		imageView.setImageResource(resIdDrawable);
		addLeftButton(imageView, viewKey);
	}
	
	public void addRightButton(int resIdDrawable,String viewKey){
		ImageView imageView=new ImageView(getContext());
		imageView.setImageResource(resIdDrawable);
		addRightButton(imageView, viewKey);
	}
	
	public void addLeftButton(View view,String viewKey){
		if(headerButtons.containsKey(viewKey)){
			Log.w(TAG, "addLeftButton>>>key is already existing: "+viewKey);
			return;
		}
		frameLayoutLeftButtons.addView(view);
		view.setTag(VIEW_KEY, viewKey);
		view.setVisibility(View.INVISIBLE);
		headerButtons.put(viewKey, view);
		view.setOnClickListener(this);
	}
	
	public void addRightButton(View view,String viewKey){
		if(headerButtons.containsKey(viewKey)){
			Log.w(TAG, "addRightButton>>>key is already existing: "+viewKey);
			return;
		}
		frameLayoutRightButtons.addView(view);
		view.setTag(VIEW_KEY, viewKey);
		view.setVisibility(View.INVISIBLE);
		headerButtons.put(viewKey, view);
		view.setOnClickListener(this);
	}
	
	public synchronized void showLeftButton(String viewKey){
		View view=headerButtons.get(viewKey);
		if(view==lastDisplayLeftButton){
//			view.setVisibility(View.VISIBLE);
			return;
		}
		if(lastDisplayLeftButton!=null){
			lastDisplayLeftButton.setVisibility(View.INVISIBLE);
		}
		view.setVisibility(View.VISIBLE);
		lastDisplayLeftButton=view;
	}
	
	public synchronized void showRightButton(String viewKey){
		View view=headerButtons.get(viewKey);
		if(view==lastDisplayRightButton){
//			view.setVisibility(View.VISIBLE);
			return;
		}
		if(lastDisplayRightButton!=null){
			lastDisplayRightButton.setVisibility(View.INVISIBLE);
		}
		view.setVisibility(View.VISIBLE);
		lastDisplayRightButton=view;
	}
	
	public void setTitle(int resIdText){
		if(titleView!=null){
			titleView.setText(resIdText);
		}
	}
	public void setTitle(String text){
		if(titleView!=null){
			titleView.setText(text);
		}
	}
	
	public void showBackButton(){
//		if(keyForBackButton==null){
//			Log.w(TAG, "showBackButton>>>keyForBackButton is null");
//			return;
//		}
		showLeftButton(keyForBackButton);
	}

	public onClickHeaderButtonListener getListener() {
		return listener;
	}

	public void setListener(onClickHeaderButtonListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View view) {
//		Enumeration<View> values=headerButtons.elements();
//		while(values.hasMoreElements()){
//			if(values.nextElement()==view){
//				break;
//			}
//		}
		
		if(listener!=null){
			String viewKey=(String)view.getTag(VIEW_KEY);
			listener.onClickHeaderButton(viewKey, view);
		}
	}
	
	public interface onClickHeaderButtonListener{
		public void onClickHeaderButton(String viewKey,View buttonView);
	}
	
}
