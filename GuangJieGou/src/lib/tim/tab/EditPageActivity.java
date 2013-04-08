package lib.tim.tab;

import lib.tim.tab.NavigationBar.OnClickDoneListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class EditPageActivity extends Activity implements OnClickDoneListener {
	
	private EditPage mEditPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TabConfig.sResultTabInfoList = null;
		
		//Enable log
//		Log.loggagble(true);
		
		//TODO 首先先判斷TabConfig.TabInfoList是否為空並且size要大於5.
		if(TabConfig.sTabInfoList == null) {
			Toast.makeText(this, "需要設置TabConfig.sTabInfoList才可以使用EditPage.", Toast.LENGTH_LONG).show();
			setResult(TabConfig.RESULT_CODE_CANCEL);
			finish();
			return ;
		}
		if(TabConfig.sTabInfoList.size() < 5) {
			Toast.makeText(this, "TabConfig.sTabInfoList的size不能小於5個.", Toast.LENGTH_LONG).show();
			setResult(TabConfig.RESULT_CODE_CANCEL);
			finish();
			return ;
		}
		//初始化GridView的index
//		final int listSize = TabConfig.sTabInfoList.size();
//		for(int i = 0; i < listSize; i++) {
//			TabConfig.sTabInfoList.get(i).setGridIndex(i);
//		}
		mEditPage = new EditPage(this);
		mEditPage.setOnClickDoneListener(this);
		setContentView(mEditPage);
	}

	@Override
	public void onClickDone() {
		TabConfig.sResultTabInfoList = mEditPage.getResultTabInfos();
		setResult(TabConfig.RESULT_CODE_OK);
		finish();
	}
	
	@Override
	public void finish() {
		final Intent intent = getIntent();
		int enterAnim = intent.getIntExtra(TabConfig.EXTRA_RETURN_ENTER_ANIM, 0);
		int exitAnim = intent.getIntExtra(TabConfig.EXTRA_RETURN_EXIT_ANIM, 0);
		super.finish();
		if(enterAnim > 0 && exitAnim > 0) {
			overridePendingTransition(enterAnim, exitAnim);
		}
	}

	@Override
	protected void onDestroy() {
		if(mEditPage != null) {
			mEditPage.release();
			mEditPage = null;
		}
		if(TabConfig.sTabInfoList != null) {
			TabConfig.sTabInfoList.clear();
			TabConfig.sTabInfoList = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(!TabConfig.sEnabledBackKey) {
			return true;
		}
		setResult(TabConfig.RESULT_CODE_CANCEL);
		return super.onKeyUp(keyCode, event);
	}
}
