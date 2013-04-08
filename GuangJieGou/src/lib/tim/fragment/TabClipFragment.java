package lib.tim.fragment;

import android.util.Log;

public class TabClipFragment extends ClipFragment {

	private static final String TAG="TabFragment";
	private TabManager tabManager=null;
	
	
//	public TabManager getTabManager() {
//		return tabManager;
//	}
//
//	public void setTabManager(TabManager tabManager,int i__) {
//		this.tabManager = tabManager;
//	}

	@Override
	public void onDestroy() {
		tabManager=null;
		super.onDestroy();
	}
	
	public void removeSelfInHistory(){
		if(tabManager==null){
			Log.w(TAG, "removeSelfFragment>>>tabManager is null");
			return;
		}
		tabManager.removeInfoAndFragment(getInfo());
	}
	
//	public void removeSelfFragment(){
//		if(tabManager==null){
//			Log.w(TAG, "removeSelfFragment>>>tabManager is null");
//			return;
//		}
//		tabManager.removeFragment(this);
//	}
	
	
}
