package com.tim.guangjiegou.fragment.guangjie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;
import com.tim.guangjiegou.activity.SearchResultActivity;
import com.tim.guangjiegou.fragment.TabContentFragment;
import com.tim.guangjiegou.util.CommonUtil;

public class GuangJieFragment extends TabContentFragment implements OnClickListener {
	
	private AutoCompleteTextView mSearchInput;
	private View mSearchButton;
	private GridView mGridView;
	
	@Override
	protected int getModuleTitleRes() {
		return R.string.tab_guangjie;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.page_guangjie, container, false);
	}

	@Override
	protected void findViews() {
		mSearchInput = (AutoCompleteTextView) findViewById(R.id.page_guangjie_search_keyword);
		mSearchButton = findViewById(R.id.page_guangjie_search_btn);
		mGridView = (GridView) findViewById(R.id.page_guangjie_grid);
	}

	@Override
	protected void setupListeners() {
		mSearchButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final String keyword = mSearchInput.getText().toString().trim();
		if(CommonUtil.isNull(keyword)) {
			Toast.makeText(getTabActivity(), R.string.tips_search_keyword_cannot_be_null, Toast.LENGTH_SHORT).show();
		}
		else {
			toSearch(keyword);
		}
	}

	private void toSearch(String keyword) {
		Intent intent = new Intent(getTabActivity(), SearchResultActivity.class);
		intent.putExtra(Constants.EXTRAS_SEARCH_KEYWORD, keyword);
		startActivity(intent);
	}
}
