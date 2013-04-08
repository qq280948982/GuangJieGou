package lib.tim.view;

import java.util.ArrayList;

public class GroupListItem<E> extends ArrayList<E> {

	private static final long serialVersionUID = 7023176166112796262L;
	
	private String mGroupName;
	
	public GroupListItem() {
		
	}
	
	public GroupListItem(String groupName) {
		mGroupName = groupName;
	}
	
	public void setGroupName(String groupName) {
		mGroupName = groupName;
	}
	
	public String getGroupName() {
		return mGroupName;
	}

}
