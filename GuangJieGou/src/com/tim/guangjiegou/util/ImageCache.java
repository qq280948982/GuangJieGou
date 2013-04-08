package com.tim.guangjiegou.util;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.common.tools.http.CommonHttpClient;
import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;

public class ImageCache {
	
	public static final boolean ENABLE_DEFAULT_BITMAP = true;

	private static final String TAG = "ImageCache";
	private static ImageCache imageCache;
	private Context context;
	// 开辟8M硬缓存空间
	private final int hardCachedSize = 10 * 1024 * 1024;
	// 软引用
	private final int MAX_DOWNLOAD_THREAD = 3;// 同时下载图片的线程数量
	private static final int SOFT_CACHE_CAPACITY = 1;
	private final File imageDir;
	private final LinkedList<Task> downloadQueue;
	private BitmapFactory.Options factory;

	private static int downloadCount = 0;
	private int taskSize;
	private Bitmap defaultBitmap = null;
	private ImageHandler IMAGE_HANDLER;

	private ImageCache(Context context) {
		super();
		this.context = context;
		downloadQueue = new LinkedList<Task>();
		imageDir = new File(Constants.PATH_LOCAL_IMAGE_LIBRARY);
		factory = new BitmapFactory.Options();
		factory.inJustDecodeBounds = true;// 当为true时 允许查询图片不为 图片像素分配内存
		defaultBitmap = ENABLE_DEFAULT_BITMAP ? BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_non_image_bg) : null;
		IMAGE_HANDLER=new ImageHandler();
	}

//	public static ImageCache getInstance(Context context) {
//		if (imageCache == null) {
//			imageCache = new ImageCache(context);
//		}
//		return imageCache;
//	}
	
	public static void init(Context context) {
		if(imageCache == null) {
			imageCache = new ImageCache(context);
		}
	}
	
	public static ImageCache getInstance() {
		return imageCache;
	}
	
	public static void destory() {
		if(imageCache != null) {
			imageCache.release();
			imageCache = null;
		}
	}

	// hard cache
	private final LruCache<String, BitmapCache> sHardBitmapCache = new LruCache<String, BitmapCache>(hardCachedSize) {
		@Override
		public int sizeOf(String key, BitmapCache value) {
			return value.bitmap.getRowBytes() * value.bitmap.getHeight();
			// return value.getRowBytes() * value.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key, BitmapCache oldValue, BitmapCache newValue) {
			// Log.w(TAG, "硬引用缓存达到上限 , 推一个不经常使用的放到软缓存:" + key);
			// 硬引用缓存区满，将一个最不经常使用的oldvalue推入到软引用缓存区
			remove(key);
			// if(oldValue!=null){
			// oldValue.bitmap.recycle();
			// }
			sSoftBitmapCache.put(key, new SoftReference<ImageCache.BitmapCache>(oldValue));
		}
	};

	private final static LinkedHashMap<String, SoftReference<BitmapCache>> sSoftBitmapCache = new LinkedHashMap<String, SoftReference<BitmapCache>>(
			SOFT_CACHE_CAPACITY, 0.75f, true) {
		private static final long serialVersionUID = 4780156896636613685L;

		@Override
		protected boolean removeEldestEntry(Entry<String, SoftReference<BitmapCache>> eldest) {
			if (size() > SOFT_CACHE_CAPACITY) {
				// Log.e(TAG, "软引用缓存达到上限 , purge one");
				if (eldest != null) {
					sSoftBitmapCache.remove(eldest.getKey());
				}
				return true;
			}
			return false;
		}

	};

	// 缓存bitmap
	private boolean putBitmap(String key, BitmapCache bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(key, bitmap);
			}
			return true;
		}
		return false;
	}

	/**
	 * 从缓存中获取bitmap
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmap(String key, Options options) {
		synchronized (sHardBitmapCache) {
			final BitmapCache bitmap = sHardBitmapCache.get(key);
			if (bitmap != null && !bitmap.bitmap.isRecycled() && bitmap.options.equals(options)) {
				return bitmap.bitmap;
			} else if (bitmap != null && !bitmap.bitmap.isRecycled()) {

				sHardBitmapCache.remove(key);
			} else {
				sHardBitmapCache.remove(key);
			}
		}
		// 硬引用缓存区间中读取失败，从软引用缓存区间读取
		synchronized (sSoftBitmapCache) {
			SoftReference<BitmapCache> bitmapReference = sSoftBitmapCache.get(key);
			if (bitmapReference != null) {
				final BitmapCache bitmap2 = bitmapReference.get();
				if (bitmap2 != null && !bitmap2.bitmap.isRecycled() && bitmap2.options.equals(options))

					return bitmap2.bitmap;
				else {
					Log.v(TAG, "soft reference 已经被回收");
					sSoftBitmapCache.remove(key);
				}
			}
		}

		return null;
	}

	public void removeBitmap(String key) {
		sHardBitmapCache.remove(key);
	}

	/**
	 * 下载文件以createImageFileName(...)命名
	 * 
	 * @param fileName
	 * @param sizeType
	 * @param options
	 * @return
	 */
	private boolean downloadImage(String url, String fileName, int sizeType, Options options) {
		// if (options == null) {
		// options = Options.createNormalOptions();
		// }
		boolean result = false;
		if(url == null || (!url.startsWith("http://") && !url.startsWith("https://"))) {
			url = Constants.URL_IMAGE_PREFIX + fileName;
		}
		File file = null;
		if(fileName.startsWith("/mnt") || fileName.startsWith("/sdcard")) {	//fileName is full path.
			file = new File(createImageFileName(fileName, options));
		}
		else {
			file = new File(imageDir.getAbsolutePath() + "/" + createImageFileName(fileName, options));
		}
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		Log.i(TAG, "To be download image: " + url);
		final CommonHttpClient client = new CommonHttpClient(url, context);
		InputStream inputStream = null;
		try {
			inputStream = client.get();
			if (inputStream != null) {
				IOUtil.saveInputStreamAsFile(inputStream, file);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			IOUtil.closeIOStream(inputStream);
		}
		return result;
	}
	
	private boolean isImageExist(String fileName) {
		if(fileName != null) {
			final File file = new File(fileName);
			return file.exists();
		}
		return false;
	}
	
	private String getImagePath(String fileName) {
		String path = null;
//		final File file = new File(imageDir.getAbsoluteFile() + "/" + fileName);
//		if(file.exists()) {
//			path = file.getAbsolutePath();
//		}
//		else {
//			final File file2 = new File(fileName);
//			if(file2.exists()) {
//				path = file2.getAbsolutePath();
//			}
//		}
		if(fileName.startsWith("/mnt") || fileName.startsWith("/sdcard")) {
			path = fileName;
		}
		else {
			path = imageDir.getAbsolutePath() + "/" + fileName;
		}
		return path;
	}

	/**
	 * 加载图片,如果加载失败则用默认图片
	 * 
	 * @param fileName
	 * @param listener
	 * @return
	 */
	public Bitmap loadImageWithDefault(String url, String fileName, ImageCacheListener listener) {
		Bitmap result = loadImage(url, fileName, 1, listener, null);

		return result != null ? result : defaultBitmap;
	}

	/**
	 * 加载图片,如果加载失败则用默认图片
	 * 
	 * @param fileName
	 * @param listener
	 * @return
	 */
	public Bitmap loadImageWithDefault(String url, String fileName, Options options, ImageCacheListener listener) {
		Bitmap result = loadImage(url, fileName, 1, listener, options);

		return result != null ? result : defaultBitmap;
	}
	
	public Bitmap loadImage(String url, String fileName, ImageCacheListener listener) {
		return loadImage(url, fileName, 1, listener, null);
	}

	public Bitmap loadImage(String url, String fileName, Options options, ImageCacheListener listener) {
		return loadImage(url, fileName, 1, listener, options);
	}

	/**
	 * 直接从缓存加载图片
	 * 
	 * @param fileName
	 * @return
	 */
	public Bitmap loadImageFromCache(String fileName, Options options) {
		if (fileName == null) {
			return null;
		}
		String mergeFileName = createImageFileName(fileName, options);// update 20120820 load image issue
		return getBitmap(mergeFileName, options);

	}

	/**
	 * 直接从缓存加载图片
	 * 
	 * @param fileName
	 * @return
	 */
	public Bitmap loadImageFromCacheWithDefault(String fileName, Options options) {
		Bitmap bitmap = null;
		if (fileName == null) {
			return defaultBitmap;
		}

		String mergeFileName = createImageFileName(fileName, options);// update 20120820 load image issue
		bitmap = getBitmap(mergeFileName, options);
		if (bitmap != null) {
			return bitmap;
		} else {
			return defaultBitmap;
		}
	}

	/**
	 * 加载Image文件名以createImageFileName为准
	 * 
	 * @param fileName
	 * @param sizeType
	 * @param listener
	 * @param options
	 *            如果是特殊图片请有Options.create***.<br>
	 *            如果想对原图缩放,先实例化Options然后设置对应的宽高,一定要把options.enable=false.<br>
	 *            如果加载原图options传null
	 * @return
	 */
	public synchronized Bitmap loadImage(String url, String fileName, int sizeType, ImageCacheListener listener, Options options) {
		Bitmap bitmap = null;
		BitmapCache bitmapCache = null;
		if (fileName == null) {
			return null;
		}
		String mergeFileName = createImageFileName(fileName, options);// update 20120820 load image issue
		bitmap = getBitmap(mergeFileName, options);
		if (bitmap != null) {
//			Log.v(TAG, "get bitmap from bitmap cache!");
			return bitmap;
		} else {
			String path = getImagePath(mergeFileName);
//			if (isImageExist(mergeFileName)) {
			if(isImageExist(path)) {
				Log.v(TAG, "Load image from file. " + mergeFileName);
				Log.v(TAG, "image path >> " + path);
//				String path = imageDir.getAbsolutePath() + "/" + mergeFileName;
				int hRatio = 0;
				int wRatio = 0;
				factory.inSampleSize = 1;
				factory.inJustDecodeBounds = true;
				synchronized (factory) {
					if (options != null && !options.enable) {
						bitmap = BitmapFactory.decodeFile(path, factory);
//						Log.d(TAG, options.toString());

						if (options.height > 0) {
							hRatio = (int) Math.ceil(factory.outHeight / options.height); // 图片是高度的几倍
						}
						if (options.width > 0) {
							wRatio = (int) Math.ceil(factory.outWidth / options.width); // 图片是宽度的几倍
						}

						if (hRatio > 1 || wRatio > 1) {
							if (hRatio > wRatio) {
								factory.inSampleSize = hRatio;

							} else
								factory.inSampleSize = wRatio;
						}

					}

					factory.inJustDecodeBounds = false;

					bitmap = BitmapFactory.decodeFile(path, factory);

					if (bitmap != null) {
						bitmapCache = new BitmapCache();
						bitmapCache.bitmap = bitmap;
						bitmapCache.options = options;
						bitmapCache.fileName = mergeFileName;
						putBitmap(mergeFileName, bitmapCache);
					} else {
						// bitmap not invalid , deleted local file and retry
						// download again bitmap
						if (factory.outHeight < 1 && factory.outWidth < 1) {
//							Log.i(TAG, "delete file:" + mergeFileName);
//							File file = new File(imageDir.getAbsolutePath() + "/" + mergeFileName);
							File file = new File(path);
							file.delete();
							if (!compareTask(fileName)) {
								addTask(url, fileName, sizeType, listener, options);
							}
						} else {
//							Log.d(TAG, "bitmap ==null && outHeight>1 && out width >1");
						}
					}
				}

			} else {
				Log.d(TAG, "Can not find the image file, download from internet.");
				if (!compareTask(fileName)) {
					addTask(url, fileName, sizeType, listener, options);
				}
			}

		}

		return bitmap;
	}

	public interface ImageCacheListener {
		public void onDownLoadSuccess(String fileName, Options options);
	}

	public class DownLoadThread extends Thread {
		private Task task;
		private TaskFinishListener taskFinishListener;

		public DownLoadThread(Task task) {
			super();
			this.task = task;
			downloadCount++;
		}

		@Override
		public void run() {
			task.isSuccess = downloadImage(task.url, task.fileName, task.sizeType, task.options);
			taskFinishListener.onFinish(task);
			super.run();
		}

		public Task getTask() {
			return task;
		}

		public DownLoadThread setTaskFinishListener(TaskFinishListener taskFinishListener) {
			this.taskFinishListener = taskFinishListener;
			return this;
		}

	}

	public interface TaskFinishListener {
		public void onFinish(Task task);
	}

	public void addTask(String name, int sizeType, ImageCacheListener listener, Options options) {
		addTask(null, name, sizeType, listener, options);
	}
	
	public void addTask(String url, String name, int sizeType, ImageCacheListener listener, Options options) {
		Task task = new Task();
		task.url = url;
		task.isSuccess = false;
		task.fileName = name;
		task.sizeType = sizeType;
		task.listener = listener;
		task.options = options;
		downloadQueue.add(task);
		startDownloadMSG();
	}

	public void removeTask(Task task) {
		downloadQueue.remove(task);
	}

	/**
	 * 比较task中有无下载这个name的资源
	 * 
	 * @param name
	 * @return
	 */
	public boolean compareTask(String name) {
		try{//TODO 20120827 Jigar: 有时出bug,不敢syn。有线程访问隐患，现在直接try.
			for (int i = 0; i < downloadQueue.size(); i++) {
				if (downloadQueue.get(i).fileName.equals(name)) {
					return true;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}

	private class Task {
		public String url;
		public int sizeType = 1;
		public String fileName;
		public boolean isSuccess = false;
		public ImageCacheListener listener;
		public Options options;
	}

	public void clearAll() {
		Log.i(TAG, "clearAll");
		clearDownloadMSG();
		downloadQueue.clear();
		sHardBitmapCache.evictAll();
		sSoftBitmapCache.clear();
		System.gc();
		Runtime.getRuntime().gc();
	}
	
	public void release() {
		this.clearAll();
		defaultBitmap = null;
		this.context = null;
	}

	/**
	 * decode bitmap size
	 * 
	 * @author JianFeng
	 * 
	 */
	public static class Options {
		public int width = -1;// pixel
		public int height = -1;// pixel
		public boolean enable = true;// 如果为true,下载图片加size参数,保存加载名字通过createImageFileName生成

		public Options() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Options(boolean enable) {
			super();
			this.enable = enable;
		}

		public Options(int width, int height) {
			super();
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {

			return "Options --> witdh:" + width + " height:" + height;
		}

		public static Options createThumbnailOptions(boolean isPad) {
			Options options = new Options();
			if (isPad) {

			} else {
				options.width = 144;
				options.height = 134;
			}
			options.enable = true;
			return options;
		}

		public static Options createNormalOptions() {
			Options options = new Options();
			options.width = 460;
			options.height = 305;
			options.enable = false;
			return options;
		}

	}

	private class BitmapCache {
		public Options options;
		public Bitmap bitmap;
		public String fileName;

		public boolean equals(Options options) {
			if (options == null && this.options == null) {
				return true;
			} else if (options != null && this.options != null && options.height == this.options.height
					&& options.width == this.options.width) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Bitmap getDefaultBitmap() {
		return defaultBitmap;
	}

	/**
	 * 生成Image文件名,如果options!=null并且options.enable=true,生成的文件名以
	 * filename+"_"+options.width+"x"+options.height+"file suffix"
	 * 
	 * @param fileName
	 * @param options
	 * @return
	 */
	public String createImageFileName(String fileName, Options options) {
		if (fileName == null) {
			return null;
		}
		if (options == null || !options.enable) {
			// Log.v(TAG, "createImageFileName:" + fileName);
			return fileName;
		}
//		int index = fileName.lastIndexOf(".");
		String tmpFileName = fileName + "_" + options.width + "x" + options.height;
		// Log.v(TAG, "createImageFileName:" + tmpFileName);
		return tmpFileName;
	}

	private void startDownloadMSG() {
		clearDownloadMSG();
		IMAGE_HANDLER.sendEmptyMessage(ImageHandler.IMAGE_HANDLER_START_LOOP);
	}

	private void clearDownloadMSG() {
		IMAGE_HANDLER.removeMessages(ImageHandler.IMAGE_HANDLER_START_LOOP);
	}
	
	class ImageHandler extends Handler{

		public static final int IMAGE_HANDLER_START_LOOP = 0x000000001;
		Handler taskCallBack=new Handler(context.getMainLooper());
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case IMAGE_HANDLER_START_LOOP:
				Task task = null;
				synchronized (downloadQueue) {
					taskSize = downloadQueue.size();
					if (taskSize > 0 && downloadCount < MAX_DOWNLOAD_THREAD) {
						task = downloadQueue.removeLast();
					}
				}
				if (task != null) {
					new DownLoadThread(task).setTaskFinishListener(new TaskFinishListener() {

						@Override
						public void onFinish(final Task task) {
							downloadCount--;
							taskCallBack.post(new Runnable() {

								@Override
								public void run() {
									if (task != null && task.isSuccess && task.listener != null) {
										task.listener.onDownLoadSuccess(task.fileName, task.options);
									}
								}
							});
							Log.d(TAG, "on download success file: " + createImageFileName(task.fileName, task.options)
									+ " onFinish isSuccess:" + task.isSuccess);

						}
					}).start();
				}
				if (taskSize <= 0) {
					clearDownloadMSG();
				} else {
					Log.d(TAG, "download image queue count>>>" + taskSize);
					sendEmptyMessageDelayed(IMAGE_HANDLER_START_LOOP, 1000);
				}
				break;
			}
		}
	}

}
