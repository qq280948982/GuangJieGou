package lib.tim.fragment;

import java.util.Hashtable;

public class ClipInfo {

	public static final int ENTERTYPE_NEXT=0x00000001;
	public static final int ENTERTYPE_BACK=0x00000010;
	public static final int ENTERTYPE_FROM_HISTORY=0x00000100;
//	public static final int ENTERTYPE_SHOW_IF_NOT_EMPTY=0x00001000;
	
	private String name;
	private Class<? extends ClipFragment> destFragment;
	private Hashtable<String,Object> requestParameter=null;
	private Hashtable<String,Object> savedData=null;
	
//	private static int defaultReplaceContainerViewID=0;
	private int replaceContainerViewID=0;
	
	private int enterFlag=ENTERTYPE_FROM_HISTORY;
	
	private int enterAnimationResId=0;
	private int exitAnimationResId=0;
	
	private int backEnterAnimationResId=0;
	private int backExitAnimationResId=0;
	
	
	//
	private String tag="0";//this is unique.
	private static int globalId=0;
	
	public ClipInfo(){
		tag=String.valueOf(increaseId());
	}
	
	private static synchronized int increaseId(){
		globalId++;
		if(globalId>Integer.MAX_VALUE-100){
			globalId=0;
		}
		return globalId;
	}
	
	public static synchronized void reset() {
		globalId = 0;
	}
	
//	public static void setDefaultReplaceContainerViewID(int replaceContainerViewID){
//		defaultReplaceContainerViewID=replaceContainerViewID;
//	}
//	public static int getDefaultReplaceContainerViewID(){
//		return defaultReplaceContainerViewID;
//	}
	
	public String getTag(){
		return tag;
	}
	
	public Hashtable<String,Object> initRequestParameter(){
		if(requestParameter==null){
			requestParameter=new Hashtable<String,Object>();
		}
		return requestParameter;
	}
	
	public Hashtable<String,Object> initSavedData(){
		if(savedData==null){
			savedData=new Hashtable<String,Object>();
		}
		return savedData;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<? extends ClipFragment> getDestFragment() {
		return destFragment;
	}
	public void setDestFragment(Class<? extends ClipFragment> destFragment) {
		this.destFragment = destFragment;
	}
	public Hashtable<String, Object> getRequestParameter() {
		return requestParameter;
	}
	public void setRequestParameter(Hashtable<String, Object> requestParameter) {
		this.requestParameter = requestParameter;
	}
	public Hashtable<String, Object> getSavedData() {
		return savedData;
	}
	public void setSavedData(Hashtable<String, Object> savedData) {
		this.savedData = savedData;
	}
	public int getReplaceContainerViewID() {
		return replaceContainerViewID;
	}
	public void setReplaceContainerViewID(int replaceContainerViewID) {
		this.replaceContainerViewID = replaceContainerViewID;
	}

	public int getEnterFlag() {
		return enterFlag;
	}

	public void setEnterFlag(int enterFlag) {
		this.enterFlag = enterFlag;
	}
	
	public void clearData() {
		if(requestParameter != null) {
			requestParameter.clear();
		}
		if(savedData != null) {
			savedData.clear();
		}
	}
	
	public int getEnterAnimationResId() {
		return enterAnimationResId;
	}

	public void setEnterAnimationResId(int enterAnimationResId) {
		this.enterAnimationResId = enterAnimationResId;
	}

	public int getExitAnimationResId() {
		return exitAnimationResId;
	}

	public void setExitAnimationResId(int exitAnimationResId) {
		this.exitAnimationResId = exitAnimationResId;
	}

	public int getBackEnterAnimationResId() {
		return backEnterAnimationResId;
	}

	public void setBackEnterAnimationResId(int backEnterAnimationResId) {
		this.backEnterAnimationResId = backEnterAnimationResId;
	}

	public int getBackExitAnimationResId() {
		return backExitAnimationResId;
	}

	public void setBackExitAnimationResId(int backExitAnimationResId) {
		this.backExitAnimationResId = backExitAnimationResId;
	}
	
	
}
