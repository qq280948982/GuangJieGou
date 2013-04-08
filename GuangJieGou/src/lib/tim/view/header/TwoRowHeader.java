package lib.tim.view.header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class TwoRowHeader extends BaseHeader {

	

	public TwoRowHeader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public TwoRowHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	private void init(){
		super.setOrientation(LinearLayout.VERTICAL);
		
		RelativeLayout rl=new RelativeLayout(getContext());
		RelativeLayout.LayoutParams rllp=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		
		super.findViews();
	}

	@Override
	protected TextView getTitleView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FrameLayout getFrameLayoutForLeftButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FrameLayout getFrameLayoutForRightButtons() {
		// TODO Auto-generated method stub
		return null;
	}

}
