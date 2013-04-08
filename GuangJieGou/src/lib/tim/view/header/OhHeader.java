package lib.tim.view.header;

import lib.tim.fragment.TabContainerFragment;
import android.app.Instrumentation;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public abstract class OhHeader extends LinearLayout implements View.OnClickListener {

	private static final String TAG="OhHeader";
	
//	private List<Integer> leftButtonIds=new ArrayList<Integer>();
//	private List<Integer> rightButtonIds=new ArrayList<Integer>();
	private View backButton=null;
	private int resIdBackButton=0;
	private boolean isBackButtonOnLeftSide=true;
	private View lastDisplayLeftButton=null;
	private View lastDisplayRightButton=null;
	
//	private Instrumentation instrumentation;
//	private onClickHeaderButtonListener listener;
	
	private TabContainerFragment containerFragment;
	KeyEvent keyEventBack=new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
	
	public OhHeader(Context context) {
		super(context);
		init();
	}

	public OhHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		int resLayout=getResLayout();
		super.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resLayout, this);
		
//		instrumentation=new Instrumentation();
	}
	
	public void setBelongToContainerFragment(TabContainerFragment fragment){
		containerFragment=fragment;
	}
	
	protected abstract int getResLayout();
	
	private void addButtonId(int resId,boolean isLeftOrRight){
		View view=super.findViewById(resId);
		if(view!=null){
			view.setVisibility(View.INVISIBLE);
//			if(isLeftOrRight){
//				leftButtonIds.add(resId);
//			}
//			else{
//				rightButtonIds.add(resId);
//			}
			view.setOnClickListener(this);
		}
	}
	public void addLeftButtonId(int resId){
		addButtonId(resId,true);
	}
	public void addRightButtonId(int resId){
		addButtonId(resId,false);
	}
	public void addLeftButtonIds(int[] resIds){
		for(int resId:resIds){
			addButtonId(resId,true);
		}
	}
	public void addRightButtonIds(int[] resIds){
		for(int resId:resIds){
			addButtonId(resId,false);
		}
	}

	public void setBackButtonId(int resId){
		setBackButtonId(resId, true);
	}
	public void setBackButtonId(int resId,boolean isOnLeftSide){
		resIdBackButton=resId;
		isBackButtonOnLeftSide=isOnLeftSide;
		backButton=super.findViewById(resIdBackButton);
//		if(resIdBackButton>0){
//			boolean isFoundOnLeftSide=false;
//			for(int ri:leftButtonIds){
//				if(ri==resIdBackButton){
//					isFoundOnLeftSide=true;
//					break;
//				}
//			}
//			isBackButtonOnLeftSide=isFoundOnLeftSide;
//		}
	}
	
	public void showBackButton(){
		showButton(backButton,isBackButtonOnLeftSide);
	}
	
	public void hideBackButton(){
		hideButton(backButton,isBackButtonOnLeftSide);
	}
	
	public void showButton(int resId,boolean isLeftOrRight){
		showButton(super.findViewById(resId),isLeftOrRight);
	}
	
	public void showButton(View view,boolean isLeftOrRight){
		if(view==null){
			return;
		}
		if(isLeftOrRight){
			if(view==lastDisplayLeftButton){
				return;
			}
			if(lastDisplayLeftButton!=null){
				lastDisplayLeftButton.setVisibility(View.INVISIBLE);
			}
			view.setVisibility(View.VISIBLE);
			lastDisplayLeftButton=view;
		}
		else{
			if(view==lastDisplayRightButton){
				return;
			}
			if(lastDisplayRightButton!=null){
				lastDisplayRightButton.setVisibility(View.INVISIBLE);
			}
			view.setVisibility(View.VISIBLE);
			lastDisplayRightButton=view;
		}
	}
	
	public void hideButton(View view,boolean isLeftOrRight){
		if(view==null){
			return;
		}
		if(isLeftOrRight){
			view.setVisibility(View.INVISIBLE);
			if(view==lastDisplayLeftButton){
				lastDisplayLeftButton=null;
			}
		}
		else{
			view.setVisibility(View.INVISIBLE);
			if(view==lastDisplayRightButton){
				lastDisplayRightButton=null;
			}
		}
	}
	
	public boolean isBackButton(int resId){
		return resId==resIdBackButton;
	}

	@Override
	public synchronized void onClick(View view) {
//		if(listener!=null){
//			listener.onClickHeaderButton(view);
//		}
		if(view.getId()==resIdBackButton && containerFragment!=null){
			
			containerFragment.onKeyUp(keyEventBack.getKeyCode(), keyEventBack);
//			new Thread(){
//				public void run(){
//					instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//				}
//			}.start();
//			instrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, (KeyEvent.KEYCODE_BACK)));
		}
		else{
			onClickHeaderButton(view);
		}
	}
	
//	public onClickHeaderButtonListener getListener() {
//		return listener;
//	}
//
//	public void setListener(onClickHeaderButtonListener listener) {
//		this.listener = listener;
//	}

	public interface onClickHeaderButtonListener{
		public void onClickHeaderButton(View buttonView);
	}
	
	protected abstract void onClickHeaderButton(View buttonView);

}
