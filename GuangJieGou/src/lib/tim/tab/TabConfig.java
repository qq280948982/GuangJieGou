package lib.tim.tab;

import java.util.List;

public final class TabConfig {
	
	public static final int REQUEST_CODE = 0;
	public static final int RESULT_CODE_OK = 1;
	public static final int RESULT_CODE_CANCEL = -1;
	public static final String EXTRA_RESULT_TAB_INFO = "lib.gt.tab.result.infos";
	public static final String EXTRA_TO_ENTER_ANIM = "lib.gt.tab.to_enter_anim";
	public static final String EXTRA_TO_EXIT_ANIM = "lib.gt.tab.to_exit_anim";
	public static final String EXTRA_RETURN_ENTER_ANIM = "lib.gt.tab.return_enter_anim";
	public static final String EXTRA_RETURN_EXIT_ANIM = "lib.gt.tab.return_exit_anim";
	
	public static boolean sEnabledBackKey;
	
	/**
	 * 是否顯示NavigationBar，默認為顯示
	 */
	public static boolean sEnableNavigationBar = true;
	
	public static int sNavigationBarBackgroundRes;
	
	public static int sNavigationBarDoneButtonBackgroundRes;
	
	/**
	 * 如果sEnableNavigationBar為true，則必須設置此值.
	 */
	public static int sNavigationBarTitleTextRes;
	
	/**
	 * 如果sEnableNavigationBar為true，則必須設置此值.
	 */
	public static int sNavigationBarDoneTextRes;
	
	/**
	 * 可以不設置，默認22sp
	 * <br/><b>注：若設置的話請直接給單位為sp的值，即不需要轉換.<b>
	 */
	public static int sNavigationBarTitleTextSize;
	
	/**
	 * 可以不設置，默認14sp
	 * <br/><b>注：若設置的話請直接給單位為sp的值，即不需要轉換.<b>
	 */
	public static int sNavigationBarDoneButtonTextSize;
	
	/**
	 * 可以不設置，默認60dp
	 * <br/><b>注：若設置的話請轉化為單位為px的值.<b>
	 */
	public static int sNavigationBarDoneButtonWidth;
	
	/**
	 * 可以不設置，默認30dp
	 * <br/><b>注：若設置的話請轉化為單位為px的值.<b>
	 */
	public static int sNavigationBarDoneButtonHeight;
	
	/**
	 * 可以不設置，默認45dp
	 * <br/><b>注：若設置的話請轉化為單位為px的值.<b>
	 */
	public static int sNavigationBarHeight;
	
	/**
	 * 可以不設置，默認45dp
	 * <br/><b>注：若設置的話請轉化為單位為px的值.<b>
	 */
	public static int sTabbarHeight;
	
	public static int sTabbarBackgroundRes;
	
	/**
	 * 可以不設置，默認28dp
	 * <br/><b>注：若設置的話請轉化為單位為px的值.<b>
	 */
	public static int sTabItemDrawbleHeight;
	
	/**
	 * Grid item移到Tab bar時發光的drawable背景.
	 */
	public static int sTabItemHighlightRes;
	
	/**
	 * 可以不設置，默認10sp
	 * <br/><b>注：若設置的話請直接給單位為sp的值，即不需要轉換.<b>
	 */
	public static int sTabItemTextSize;
	
	public static boolean sAutoScaleTabItemTextSize;
	
	public static int sTabItemNormalTextColor;
	
	public static int sTabItemSelectedTextColor;
	
	public static int sTabItemSelectedBackgroundRes;
	
	public static int sGridViewBackgroundColor;
	
	public static int sGridViewBackgroundRes;
	
	/**
	 * 必須設置此值，傳入所有的TabInfo,並設置每個TabInfo的tabbarIndex來標明其在Tabbar的位置.
	 */
	public static List<TabInfo> sTabInfoList;
	
	public static List<TabInfo> sResultTabInfoList;
	
	public static TabInfo getTabInfoByIndex(int index) {
		TabInfo info = null;
		if(sTabInfoList != null && index >= 0 && index < sTabInfoList.size()) {
			final int size = sTabInfoList.size();
			for(int i = 0; i < size; i++) {
				TabInfo t = sTabInfoList.get(i);
				if(t.getTabbarIndex() == index) {
					info = t;
					break;
				}
			}
			if(info == null) {	//sTabInfoList裏的TabInfo沒有設置index的話，按list的順序取得.
				info = sTabInfoList.get(index);
			}
		}
		return info;
	}
	
	//Others
	public static int sDescriptionStringRes;
}
