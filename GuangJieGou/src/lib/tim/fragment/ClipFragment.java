package lib.tim.fragment;

import java.util.Hashtable;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ClipFragment extends Fragment {
	private static final String TAG="ClipFragment";
	
	private ClipInfo info;
	private boolean isAllowRemove=false;//若为false，则FragmentTransaction只可以用hide().
	
	private int enterType=ClipInfo.ENTERTYPE_NEXT;
	
	public ClipInfo getInfo() {
		return info;
	}

	public void setInfo(ClipInfo info) {
		this.info = info;
	}
	
	public boolean isAllowRemove() {
		return isAllowRemove;
	}

	public void setAllowRemove(boolean isAllowRemove) {
		this.isAllowRemove = isAllowRemove;
	}
	
	public int getEnterType(){
		return enterType;
	}
	
	void setEnterType(int enterType){
		this.enterType=enterType;
	}
	
	public boolean isEnterTypeNext(){
		return enterType==ClipInfo.ENTERTYPE_NEXT;
	}
	
	public boolean isEnterTypeBack(){
		return enterType==ClipInfo.ENTERTYPE_BACK;
	}
	
	public boolean isEnterTypeFromHistory(){
		return enterType==ClipInfo.ENTERTYPE_FROM_HISTORY;
	}
	
//	@Override
//	public void onHiddenChanged(boolean hidden) {
//		super.onHiddenChanged(hidden);
//		if(super.getView()!=null){
//			Log.i(TAG, "onHiddenChanged>>>has view,hidden="+hidden);
//			super.getView().setVisibility(hidden?View.INVISIBLE:View.VISIBLE);
//		}
//	}
	
	public void onFragmentResume(){
		
	}
	
	public void onFragmentDidAppear(){
		
	}

	public void onSaveData(Hashtable<String,Object> savedData){
		
	}
	
	public void onRestoreSavedData(Hashtable<String,Object> savedData){
		
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	

	//==================================================
	// ====The core series of lifecycle methods that are called to bring a fragment
	// up to resumed state (interacting with the user) are:

	// called once the fragment is associated with its activity.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// called to do initial creation of the fragment.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// creates and returns the view hierarchy associated with the fragment.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	// tells the fragment that its activity has completed its own
	// Activity.onCreaate.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// makes the fragment visible to the user (based on its containing activity
	// being started).
	@Override
	public void onStart() {
		super.onStart();
	}

	// makes the fragment interacting with the user (based on its containing
	// activity being resumed).
	@Override
	public void onResume() {
		super.onResume();
		if(super.isVisible()){
			onFragmentResume();
		}
	}

	// =====As a fragment is no longer being used, it goes through a reverse
	// series of callbacks:

	// fragment is no longer interacting with the user either because its
	// activity is being paused or a fragment operation is modifying it in the
	// activity.
	@Override
	public void onPause() {
		super.onPause();
	}

	// fragment is no longer visible to the user either because its activity is
	// being stopped or a fragment operation is modifying it in the activity.
	@Override
	public void onStop() {
		super.onStop();
	}

	// allows the fragment to clean up resources associated with its View.
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	// called to do final cleanup of the fragment's state.
	@Override
	public void onDestroy() {
		info = null;
		super.onDestroy();
	}

	// called immediately prior to the fragment no longer being associated with
	// its activity.
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
/**	Only For Debug
	@Override
	protected void finalize() throws Throwable {
		Log.e(TAG, getClass().getSimpleName() + " is finalize..");
		super.finalize();
	}
*/
}
