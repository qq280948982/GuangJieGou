package lib.tim.tab;


public class TabInfo {

	private String tabId;
	private int tabName;
	private int tabDrawableRes;
	private int tabbarIndex = -99;
	private boolean isSelected;
	private boolean isShowInGridView = true;
	private int gridIndex = -88;
	
	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	public int getTabName() {
		return tabName;
	}

	public void setTabName(int tabName) {
		this.tabName = tabName;
	}

	public int getTabDrawableRes() {
		return tabDrawableRes;
	}

	public void setTabDrawableRes(int tabDrawableRes) {
		this.tabDrawableRes = tabDrawableRes;
	}

	public int getTabbarIndex() {
		return tabbarIndex;
	}

	/**
	 * 設置Tabbar的位置，從0開始，如果沒有設置，則按TabConfig.sTabInfoList的順序排序
	 * @param tabbarIndex
	 */
	public void setTabbarIndex(int tabbarIndex) {
		this.tabbarIndex = tabbarIndex;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isShowInGridView() {
		return isShowInGridView;
	}

	public void setShowInGridView(boolean isShowInGridView) {
		this.isShowInGridView = isShowInGridView;
	}
	
	/**
	 * 這個是在GridView中的位置，按照提供的列表順序設置，不允許外部設置，故設為default的訪問權限.
	 * @param index
	 */
	void setGridIndex(int index) {
		this.gridIndex = index;
	}
	
	/**
	 * 這個是在GridView中的位置，按照提供的列表順序設置，不允許外部設置，故設為default的訪問權限.
	 * @param index
	 */
	int getGridIndex() {
		return gridIndex;
	}
	
/*	Parcelable方法有問題，還是把結果存在TabConfig.sResultTabInfoList吧.
 * 
	@Override
	public int describeContents() {
		return tabbarIndex;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(tabId);
		dest.writeInt(tabName);
		dest.writeInt(tabDrawableRes);
		dest.writeInt(tabbarIndex);
		dest.writeInt(isSelected ? 1 : 0);
		dest.writeInt(isShowInGridView ? 1 : 0);
		dest.writeInt(gridIndex);
	}
	
	public static final Parcelable.Creator<TabInfo> CREATOR = new Parcelable.Creator<TabInfo>() {

		@Override
		public TabInfo createFromParcel(Parcel source) {
			TabInfo tabInfo = new TabInfo();
			tabInfo.gridIndex = source.readInt();
			tabInfo.isShowInGridView = source.readInt() == 1;
			tabInfo.isSelected = source.readInt() == 1;
			tabInfo.tabbarIndex = source.readInt();
			tabInfo.tabDrawableRes = source.readInt();
			tabInfo.tabName = source.readInt();
			tabInfo.tabId = source.readString();
			return tabInfo;
		}

		@Override
		public TabInfo[] newArray(int size) {
			return null;
		}
		
	};
*/
}
