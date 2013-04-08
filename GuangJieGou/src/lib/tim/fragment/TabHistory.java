package lib.tim.fragment;

import java.util.ArrayList;
import java.util.List;

public class TabHistory {

	private String tabID=null;
	private int containerViewID=0;
	private List<ClipInfo> history=new ArrayList<ClipInfo>();
	
	public TabHistory(String tabID){
		this.tabID=tabID;
	}
	
	public String getTabID(){
		return tabID;
	}
	
	public boolean containsClipInfoByName(String name){
		return getClipInfoByName(name)!=null;
	}
	
	public boolean containsClipInfoByClipFragment(Class<? extends ClipFragment> cls){
		return getClipInfoByClipFragment(cls)!=null;
	}
	
	public boolean containsClipInfo(ClipInfo info){
		return indexOfClipInfo(info)>-1;
	}
	
	public ClipInfo getClipInfoByName(String name){
		for(ClipInfo info:history){
			if(info.getName().equals(name)){
				return info;
			}
		}
		return null;
	}
	
	public ClipInfo getClipInfoByClipFragment(Class<? extends ClipFragment> cls){
		for(ClipInfo info:history){
			if(info.getDestFragment()==cls){
				return info;
			}
		}
		return null;
	}
	
	public ClipInfo getClipInfoByIndex(int index){
		return history.get(index);
	}
	
	public void addClipInfoToHistory(ClipInfo info){
		history.add(info);
	}
	
	public int indexOfClipInfo(ClipInfo info){
		return history.indexOf(info);
	}
	
	public void clearHistory(){
		history.clear();
	}
	
	public List<ClipInfo> getHistory(){
		return history;
	}

	public int getContainerViewID() {
		return containerViewID;
	}

	public void setContainerViewID(int containerViewID) {
		this.containerViewID = containerViewID;
	}
	
	public boolean hasPreviousClip(){
		if(history.size()>1){
			return true;
		}
		return false;
	}
	
	public ClipInfo popupLastClip(){
		if(history.size()>0){
			return history.remove(history.size()-1);
		}
		return null;
	}
	
	public ClipInfo getLastClip(){
		if(history.size()>0){
			return history.get(history.size()-1);
		}
		return null;
	}
	
	public boolean removeClipInfo(ClipInfo info){
		if(history.size()>0){
			return history.remove(info);
		}
		return false;
	}
}
