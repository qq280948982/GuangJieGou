package lib.tim.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

//TODO 未完整测试各种效果：粗斜体，斜体，粗体。。。
public class OhButton extends Button {

	private static Typeface customTypefaceNormal=null;
	private static Typeface customTypefaceBold=null;

	public OhButton(Context context) {
		super(context);
	}
	
	public OhButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public OhButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setTypeface(Typeface tf, int style) {
		Typeface paramTf=tf;
		if(style==Typeface.BOLD_ITALIC){
			paramTf=customTypefaceBold;
		}
		else if(style==Typeface.BOLD){
			paramTf=customTypefaceBold;
		}
		else if(style==Typeface.ITALIC){
			paramTf=customTypefaceNormal;
		}
		else{
			paramTf=customTypefaceNormal;
		}
		if(paramTf==null){
			paramTf=tf;
		}
		super.setTypeface(paramTf, style);
	}

	public static void setupCustomTypeface(Context context, String assetPathNormal,String assetPathBold){
		if(assetPathNormal!=null){
			customTypefaceNormal=Typeface.createFromAsset (context.getAssets(),assetPathNormal);
		}
		if(assetPathBold!=null){
			customTypefaceBold=Typeface.createFromAsset (context.getAssets(),assetPathBold);
		}
	}

}
