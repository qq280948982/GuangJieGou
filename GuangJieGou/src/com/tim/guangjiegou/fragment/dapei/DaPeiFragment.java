package com.tim.guangjiegou.fragment.dapei;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tim.guangjiegou.R;
import com.tim.guangjiegou.fragment.TabContentFragment;

public class DaPeiFragment extends TabContentFragment {
	
	private ListView mDaPeiListView;
	private DaPeiListAdapter mAdapter;

	@Override
	protected int getModuleTitleRes() {
		return R.string.tab_tapei;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.page_dapei, container, false);
	}

	@Override
	protected void findViews() {
		mDaPeiListView = (ListView) findViewById(R.id.page_dapei_list);
		mAdapter = new DaPeiListAdapter(getTabActivity());
		mDaPeiListView.setAdapter(mAdapter);
		
		mAdapter.fetchData();
	}

	@Override
	protected void setupListeners() {
		
	}
	
	@Override
	public void onFragmentDidAppear() {
		super.onFragmentDidAppear();
		if(mAdapter != null) {
			mAdapter.fetchData();
		}
				
	}

}
