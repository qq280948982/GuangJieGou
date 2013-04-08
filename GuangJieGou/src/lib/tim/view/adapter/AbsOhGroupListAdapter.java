package lib.tim.view.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.widget.ExpandableListAdapter;


public abstract class AbsOhGroupListAdapter implements ExpandableListAdapter {
	
	private final DataSetObservable mDataSetObservable = new DataSetObservable();

	/**
	 * 在group懸掛時會調用此方法，需要在此方法中更新懸掛group view為當前group的信息，如更新group的text
	 * @param hangGroudItemView	當前懸掛的group view.
	 * @param groupPos	當前的group位置
	 */
	public abstract void updateHangGroup(View hangGroudItemView, int groupPos);
	
	/**
	 * 請在此方法中釋放所有的成員變量，以防被外部引用而導致無法垃圾回收.
	 */
	public void release() {
		mDataSetObservable.unregisterAll();
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return getGroupCount() == 0;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {

	}

	@Override
	public void onGroupExpanded(int groupPosition) {

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	
	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}
	
	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}
	
	public boolean isGroupOutOfBounds(int groupPosition) {
		return groupPosition < 0 || groupPosition >= getGroupCount();
	}
	
	public boolean isChildOutOfBounds(int groupPosition, int childPosition) {
		boolean flag = isGroupOutOfBounds(groupPosition);
		if(!flag) {
			flag = childPosition < 0 || childPosition >= getChildrenCount(groupPosition);
		}
		return flag;
	}
	
}
