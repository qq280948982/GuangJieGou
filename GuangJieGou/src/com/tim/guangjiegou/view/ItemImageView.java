package com.tim.guangjiegou.view;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.util.CommonUtil;
import com.tim.guangjiegou.util.IOUtil;
import com.tim.guangjiegou.util.ImageCache;
import com.tim.guangjiegou.util.ImageCache.ImageCacheListener;
import com.tim.guangjiegou.util.ImageCache.Options;

public class ItemImageView extends ImageView implements ImageCacheListener {
	private final static String TAG = "ItemImageView";
	private String fileName = "";
	private Options options;
	private int delay;
	private AsyncTask<Integer, Void, Bitmap> loadBitmapAsyn;
	
	private String url = "";
	
	private int defaultImageRes;
	
	private int validImageSize = -1;
	
	private boolean mTopCrop;

	public ItemImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ItemImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ItemImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setFileName(String url, String fileName) {
		setFileName(url, fileName, 0);
	}
	
	public void setFileName(String url, String fileName, int delay) {
		this.url = CommonUtil.replaceBlank(url);
		setFileName(fileName, (Options) null, delay);
	}
	
	public void setFileName(String url, String fileName, Options options) {
		this.url = CommonUtil.replaceBlank(url);
		setFileName(fileName, options, delay);
	}
	
	private void setFileName(String fileName, Options options, int delay) {
		if (fileName != null && this.fileName != null && loadBitmapAsyn != null && !this.fileName.equals(fileName)) {
			if (loadBitmapAsyn.getStatus().equals(AsyncTask.Status.RUNNING)) {
				loadBitmapAsyn.cancel(true);
			}
		}
		this.delay = delay;
		this.options = options;
		this.fileName = CommonUtil.replaceBlank(fileName);
		
//		Log.e(TAG, "Complete url: " + this.url);
//		Log.e(TAG, "FileName: " + fileName);
	}

	public void setImageFromCache() {
		final ImageCache imageCache = ImageCache.getInstance();
		if(imageCache == null) {
			return ;
		}
		Bitmap bitmap = imageCache.loadImageFromCacheWithDefault(fileName, options);
		if(bitmap != null) {
			setBitmapImage(bitmap);
		}
	}

	/**
	 * 使用默认的延迟时间加载图片,options ==null ,ImageCacheListener == this
	 */
	public void loadImageFromFileNameNoDelay() {
		final ImageCache imageCache = ImageCache.getInstance();
		if(imageCache == null) {
			return ;
		}
		Bitmap result = imageCache.loadImageWithDefault(url, fileName, options, this);
		if(result != null) {
			setBitmapImage(result);
		}
	}

	/**
	 * 使用默认的延迟时间加载图片,options ==null ,ImageCacheListener == this
	 */
	public void loadImageFromFileName() {
		loadImageFromFileNameWithDelay(this);
	}
	
	/**
	 * 有延迟加载图片,先判断hard cache 有无这个bitmap的缓存,如果有返回bitmap不延迟加载,否则会加载一个默认图片,当过了delay
	 * 时间后会设置对应加载的bitmap,<b>注意一定先设置好fileName</b>
	 * 
	 * @param options
	 * @param delay
	 * @param listener
	 * 
	 * 
	 */
	public void loadImageFromFileNameWithDelay(final ImageCacheListener listener) {
		// Log.v(TAG, "loadImageDelay:" + fileName + " this.fileName:"
		// + ItemImageView.this.fileName);
		// Log.d(TAG, "loadImageDelay who:" + ItemImageView.this);
		// Log.i(TAG, "loadImageDelay title:" + title + " options:" + options);// TODO
		// log
		final ImageCache imageCache = ImageCache.getInstance();
		if(imageCache == null) {
			return ;
		}
		Bitmap bitmap = imageCache.loadImageFromCache(fileName, options);
		if (bitmap == null) {
//			Log.v(TAG, "cache not find bitmap");
			if(!IOUtil.isExistFile(fileName)) {
				loadDefaultDrawable();
			}
			loadBitmapAsyn = new AsyncTask<Integer, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Integer... params) {
					int delayTime = delay;
					// Log.i(TAG, "delay Time:" + delay);
					Bitmap result = null;
					if (delayTime < Constants.LOADING_IMAGE_MIN_DELAY) {
						delayTime = Constants.LOADING_IMAGE_MIN_DELAY;// TODO
					}
					SystemClock.sleep(delayTime);
					result = imageCache.loadImageWithDefault(url, fileName, options, listener);
					return result;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					if(result != null) {
						setBitmapImage(result);
					}
					super.onPostExecute(result);
				}

			}.execute((Integer) null);
			bitmap = imageCache.getDefaultBitmap();
		}
		else {
			setBitmapImage(bitmap);
		}

	}

	@Override
	public void onDownLoadSuccess(String fileName, Options options) {
		final ImageCache imageCache = ImageCache.getInstance();
		if(imageCache == null) {
			return ;
		}
		Bitmap bitmap = imageCache.loadImageWithDefault(url, ItemImageView.this.fileName, options,
				null);
		if(bitmap != null) {
			setBitmapImage(bitmap);
		}

	}
	
	private boolean isValidImage(String fullFilePath) {
		if(validImageSize < 0) {
			return true;
		}
		boolean retValue = false;
		if(fullFilePath != null) {
			final File file = new File(fullFilePath);
			if(file.exists()) {
				retValue = file.length() > validImageSize;
			}
		}
		return retValue;
	}

//	public void setDefalutBitmap() {
//		if (loadBitmapAsyn != null && loadBitmapAsyn.getStatus().equals(AsyncTask.Status.RUNNING)) {
//			loadBitmapAsyn.cancel(true);
//		}
//		setImageBitmap(ImageCache.getInstance(getContext()).getDefaultBitmap());
//		ImageCache.getInstance(getContext()).removeBitmap(fileName);
//	}
	
	public void setDefaultDrawableRes(int res) {
		defaultImageRes = res;
	}
	
	public void loadDefaultDrawable() {
		if (loadBitmapAsyn != null && loadBitmapAsyn.getStatus().equals(AsyncTask.Status.RUNNING)) {
			loadBitmapAsyn.cancel(true);
		}
		if(defaultImageRes > 0) {
			setImageResource(defaultImageRes);
		}
		final ImageCache imageCache = ImageCache.getInstance();
		if(imageCache == null) {
			return ;
		}
		imageCache.removeBitmap(fileName);
	}
	
	public void setValidateImageSize(int byteSize) {
		validImageSize = byteSize;
	}
	
	public void setBitmapImage(Bitmap bitmap) {
		if(bitmap != null && isValidImage(fileName)) {
			super.setImageBitmap(bitmap);
		}
		else {
			loadDefaultDrawable();
		}
	}

	public void setScaleTypeAsTopCrop(boolean topCrop) {
		mTopCrop = topCrop;
		if(topCrop) {
			setScaleType(ScaleType.MATRIX);
		}
		else {
			setScaleType(ScaleType.CENTER);
		}
	}
	
	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		if(mTopCrop && getDrawable() != null) {
			Matrix matrix = getImageMatrix(); 
	        float scaleFactor = getWidth() / (float)getDrawable().getIntrinsicWidth();    
	        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
	        setImageMatrix(matrix);
		}
		return super.setFrame(l, t, r, b);
	}
	
	public interface ItemImageViewInterface {
		public ItemImageView getItemImageView();
	}
}
