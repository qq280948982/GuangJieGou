package lib.tim.tab;

import android.app.Activity;
import android.content.Intent;

public final class EditPageActivityLauncher {
	
	private EditPageActivityLauncher() {}
	
	public static void startEditPageActivity(Activity activity) {
		startEditPageActivity(activity, 0, 0, 0, 0);
	}
	
	public static void startEditPageActivity(Activity activity, int toEnterAnimRes, int toExitAnimRes, int returnEnterAnimRes, int returnAnimExitRes) {
		if(activity != null) {
			final Intent intent = new Intent(activity, EditPageActivity.class);
			intent.putExtra(TabConfig.EXTRA_TO_ENTER_ANIM, toEnterAnimRes);
			intent.putExtra(TabConfig.EXTRA_TO_EXIT_ANIM, toExitAnimRes);
			intent.putExtra(TabConfig.EXTRA_RETURN_ENTER_ANIM, returnEnterAnimRes);
			intent.putExtra(TabConfig.EXTRA_RETURN_EXIT_ANIM, returnAnimExitRes);
			activity.startActivityForResult(intent, TabConfig.REQUEST_CODE);
			if(toEnterAnimRes > 0 && toExitAnimRes > 0) {
				activity.overridePendingTransition(toEnterAnimRes, toExitAnimRes);
			}
		}
	}

}
