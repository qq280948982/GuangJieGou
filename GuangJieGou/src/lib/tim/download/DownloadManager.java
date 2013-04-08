package lib.tim.download;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class DownloadManager implements Runnable{

	private static final String TAG="DownloadManager";
	
	private boolean isRunning=false;
	private Thread thread=null;
	private static final int TIME_WAIT=1000;
	private Object lock=new Object();
	
	private boolean isKeepSuccessTasks=false;
	private boolean isKeepFailTasks=false;
	
	private List<DownloadTask> listAllDownloadTasks=new ArrayList<DownloadTask>();
//	private List<DownloadTask> listCompleteTasks=new ArrayList<DownloadTask>();
//	private List<DownloadTask> listTrashTasks=new ArrayList<DownloadTask>();
	
	private int maxDownloaderNumber=1;
	private List<Downloader> listDownloaders=new ArrayList<Downloader>();
	private DownloadTask lastDownloadTask=null;
	
	private DownloadManagerListener listener;
	
	public DownloadManager(){
		start();
	}
	
	public void config(boolean isKeepSuccessTasks,boolean isKeepFailTasks){
		this.isKeepSuccessTasks=isKeepSuccessTasks;
		this.isKeepFailTasks=isKeepFailTasks;
	}
	
	private void start(){
		if(thread==null){
			thread=new Thread(this);
			thread.start();
		}
	}
	
	public DownloadTask findDownloadTask(String taskName){
		for(DownloadTask task:listAllDownloadTasks){
			if(task.getTaskName().equals(taskName)){
				return task;
			}
		}
		return null;
	}
	
	public void addDownloadTask(DownloadTask downloadTask){
		downloadTask.resetStatus();
		listAllDownloadTasks.add(downloadTask);
		synchronized(lock){
			lock.notify();
		}
	}
	
	public void deleteDownloadTask(DownloadTask downloadTask,boolean isStopDownload){
		listAllDownloadTasks.remove(downloadTask);
		if(isStopDownload){
			stopDownloadTask(downloadTask);
			synchronized(lock){
				lock.notify();
			}
		}
	}
	
	//TODO retryDownloadTask();
	//TODO startAllDownloadTasks();
	//TODO startAllStoppedDownloadTasks();
	//TODO moveDownloadTaskUp();
	//TODO moveDownloadTaskDown();
	//TODO moveDownloadTaskToTop();
	//TODO moveDownloadTaskToBottom();
	
	public void stopDownloadTask(DownloadTask downloadTask){
		for(Downloader downloader:listDownloaders){
			if(downloader.getDownloadTask()==downloadTask){
				downloader.stop();
				break;
			}
		}
	}
	
	public void clearAllDownloadTasks(boolean isStopDownload){
		listAllDownloadTasks.clear();
		if(isStopDownload){
			for(Downloader downloader:listDownloaders){
				downloader.stop();
			}
		}
	}

	public void shutdown(){
		this.isRunning=false;
		synchronized(lock){
			lock.notify();
		}
	}
	
	public void setDownloadListener(DownloadManagerListener listener){
		this.listener=listener;
	}
	
	public DownloadManagerListener getDownloadManagerListener(){
		return listener;
	}
	
	void onDownloadStatusChanged(Downloader downloader, DownloadTask downloadTask,int oldStatus,int newStatus){
		switch(newStatus){
		case DownloadTask.STATUS_SUCCESS:
			if(!isKeepSuccessTasks && !downloadTask.isRemoveManuallyWhenSuccess()){
				listAllDownloadTasks.remove(downloadTask);
			}
			break;
		case DownloadTask.STATUS_FAIL:
			if(!isKeepFailTasks && !downloadTask.isRemoveManuallyWhenFail()){
				listAllDownloadTasks.remove(downloadTask);
			}
			break;
		}
		if(listener!=null){
			listener.onDownloadStatusChanged(this, downloadTask, oldStatus, newStatus);
		}
		if(newStatus==DownloadTask.STATUS_SUCCESS
				|| newStatus==DownloadTask.STATUS_FAIL
				|| newStatus==DownloadTask.STATUS_STOP){
			
			listDownloaders.remove(downloader);
			synchronized(lock){
				lock.notify();
			}
		}
	}
	
	@Override
	public void run() {
		isRunning=true;
		while(isRunning){
			synchronized(lock){
				try {
					lock.wait(TIME_WAIT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try{
				if(!isRunning){
					break;
				}
				if(listDownloaders.size()>=maxDownloaderNumber){
					continue;
				}
				DownloadTask downloadTask=getNextDownloadableTask();
				if(downloadTask==null){
					continue;
				}
				if(downloadTask==lastDownloadTask){
					Log.w(TAG, "run>>>these two tasks are the same one");
				}
				lastDownloadTask=downloadTask;
				Downloader downloader=new Downloader(this);
				listDownloaders.add(downloader);
				downloader.assignDownloadTask(downloadTask);
			}
			catch(Exception e){
				Log.e(TAG,"run>>>e="+e.toString());
				e.printStackTrace();
			}
		}
		this.clearAllDownloadTasks(true);
	}
	
	public DownloadTask getNextDownloadableTask(){
		//Should be improved: record the last download task, 
		//find the next one from the index of last one.
		if(listAllDownloadTasks.size()==0){
			return null;
		}
		for(DownloadTask tempTask:listAllDownloadTasks){
			if(tempTask.getStatus()==DownloadTask.STATUS_NORMAL){
				return tempTask;
			}
		}
		return null;
	}
	
}
