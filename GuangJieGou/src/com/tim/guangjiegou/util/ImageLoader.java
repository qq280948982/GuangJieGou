package com.tim.guangjiegou.util;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

/**
 * 此类不能用
 * @author Tim
 *
 */
public class ImageLoader
{
  public static final int MAX_QUEUE_SIZE = 20;
  private static ImageLoader ins = null;
  public ImageCache cache = null;
  public Handler handler = null;
  public ThreadPoolExecutor lowPirorTheadPool = null;
  public BlockingQueue<Runnable> lowqueue = null;
  public BlockingQueue<Runnable> queue = null;
  public ThreadPoolExecutor theadPool = null;

  public ImageLoader(Context paramContext)
  {
    this(paramContext, paramContext.getCacheDir(), 0, 10);
  }

  public ImageLoader(Context paramContext, File paramFile, int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0)
      paramInt1 = 1048576 * ((ActivityManager)paramContext.getSystemService("activity")).getMemoryClass() / 8;
//    this.cache = new ImageCache(paramFile, paramInt1);
    this.queue = new ArrayBlockingQueue(20);
    this.lowqueue = new ArrayBlockingQueue(400);
    this.theadPool = new ThreadPoolExecutor(paramInt2, paramInt2 + 5, 5L, TimeUnit.SECONDS, this.queue);
    this.lowPirorTheadPool = new ThreadPoolExecutor(3, 8, 5L, TimeUnit.SECONDS, this.lowqueue);
    this.handler = new Handler();
  }

  public void clear()
  {
    if (this.lowqueue.remainingCapacity() > this.queue.size())
      this.lowqueue.addAll(this.queue);
    this.queue.clear();
  }

  public void loadImage(String paramString, ImageView paramImageView)
  {
    loadImage(paramString, paramImageView, true);
  }

  public void loadImage(String paramString, ImageView paramImageView, int paramInt, boolean paramBoolean)
  {
    loadImage(paramString, paramImageView, paramInt, paramBoolean, 2130837595);
  }

  public void loadImage(String paramString, ImageView paramImageView, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    if ((paramString != null) && (paramString.trim().length() > 3))
    {
//      Bitmap localBitmap = this.cache.get(paramString, paramInt1, paramBoolean, false);
    	Bitmap localBitmap = null;
      if (localBitmap != null)
        paramImageView.setImageBitmap(localBitmap);
    }
//    do
//    {
//      return;
//      if (paramInt2 > 0)
//        paramImageView.setImageResource(paramInt2);
//      runTask(new LoadingImage(paramImageView, paramInt1, paramString));
//      return;
//    }
//    while (paramInt2 <= 0);
//    paramImageView.setImageResource(paramInt2);
  }

  public void loadImage(String paramString, ImageView paramImageView, boolean paramBoolean)
  {
    loadImage(paramString, paramImageView, Math.max(paramImageView.getMeasuredWidth(), 32), paramBoolean);
  }

  public void loadToCache(String paramString, int paramInt)
  {
    loadToCache(paramString, paramInt, false);
  }

  public void loadToCache(String paramString, int paramInt, boolean paramBoolean)
  {
//    if (paramBoolean)
//    {
//      this.lowPirorTheadPool.execute(new Runnable(paramString, paramInt)
//      {
//        public void run()
//        {
//          ImageLoader.this.cache.get(this.val$url, this.val$width, true);
//        }
//      });
//      return;
//    }
//    runTask(new Runnable(paramString, paramInt)
//    {
//      public void run()
//      {
//        ImageLoader.this.cache.get(this.val$url, this.val$width, true);
//      }
//    });
  }

  public void runTask(Runnable paramRunnable)
  {
    if (this.queue.size() >= 20)
      this.queue.poll();
    this.theadPool.execute(paramRunnable);
  }

  class LoadingImage
    implements Runnable
  {
    private ImageView img = null;
    private String url = null;
    private int width = 0;

    LoadingImage(ImageView paramInt, int paramString, String arg4)
    {
      this.img = paramInt;
      this.width = paramString;
      Object localObject;
//      this.url = localObject;
    }

    public void run()
    {
//      Bitmap localBitmap = ImageLoader.this.cache.get(this.url, this.width, true);
//      if (localBitmap == null)
//        return;
//      ImageLoader.this.handler.post(new Runnable(localBitmap)
//      {
//        public void run()
//        {
//          ImageLoader.LoadingImage.this.img.setImageBitmap(this.val$bm);
//        }
//      });
    }
  }
}