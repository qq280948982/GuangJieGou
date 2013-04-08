package lib.tim.download;

public interface DownloadManagerListener {

//	public String getName();//方便删除同名的listener
//	public boolean isSingle();//可以根据这个值，决定是否删除同名的旧listener.
	
	public void onDownloadStatusChanged(DownloadManager downloadManager,DownloadTask downloadTask,int oldStatus,int newStatus);
}
