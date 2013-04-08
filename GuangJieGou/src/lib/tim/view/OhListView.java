package lib.tim.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class OhListView extends ListView {

	public OhListView(Context context) {
		super(context);
		initComponent();
	}

	public OhListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initComponent();
	}
	
	public OhListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initComponent();
	}
	
	private void initComponent() {
		setCacheColorHint(Color.TRANSPARENT);
//		setBackgroundColor(getContext().getResources().getColor(R.color.black_deep_alpha));
		ColorDrawable drawable = new ColorDrawable(Color.WHITE);
		setDivider(drawable);
		setDividerHeight(1);
	}

}
