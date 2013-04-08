package lib.tim.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

public class OhLabelRightTextView extends LinearLayout {

	private static final String TAG="OhLabelRightTextView";
	protected OhTextView labelView=null;
	protected OhTextView textView=null;
	
	public OhLabelRightTextView(Context context) {
		super(context);
		init();
	}
	
	public OhLabelRightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		labelView=new OhTextView(getContext());
		LinearLayout.LayoutParams lpLabel=new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpLabel.gravity=Gravity.CENTER_VERTICAL;
		super.addView(labelView, lpLabel);
		
		textView=new OhTextView(getContext());
		LinearLayout.LayoutParams lpText=new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		lpText.leftMargin=12;
		lpText.gravity=Gravity.CENTER_VERTICAL;
		textView.setGravity(Gravity.RIGHT);
		super.addView(textView, lpText);
		
		textView.setSingleLine(true);
		textView.setEllipsize(TruncateAt.END);
	}

	public OhTextView getLabelView() {
		return labelView;
	}

	public OhTextView getTextView() {
		return textView;
	}
	
	public void setLabel(int resIdString){
		labelView.setTextRes(resIdString);
	}
	
	public void setText(int resIdString){
		textView.setTextRes(resIdString);
	}
	
	public String getText(){
		return textView.getText().toString();
	}
	
	public void setText(String text){
		textView.setText(text);
	}
	
	public String getLabel(){
		return labelView.getText().toString();
	}
	
	public void setLabel(String text){
		labelView.setText(text);
	}
	
	public void updateText(){
		labelView.updateText();
		textView.updateText();
	}

}
