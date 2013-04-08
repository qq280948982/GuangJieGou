package lib.tim.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lib.tim.util.IOUtil;
import android.util.Log;

public class Downloader implements Runnable{

	private static final String TAG="Downloader";
	
	private DownloadTask downloadTask;
	
	private Thread thread=null;
	private boolean isRunning=false;
	
	private HttpURLConnection connection=null;
	private InputStream inputStream=null;
	private FileOutputStream fileOutputStream=null;
	
	private static final int TIMEOUT_CONNECT=30000;
	private static final int TIMEOUT_READ=30000;
	
	private static final String SUFFIX_TEMP=".temp";
	
	private DownloadManager downloadManager;
	
	//
	private int id;
	private static int baseOnId=0;
	
	public Downloader(DownloadManager downloadManager){
		this.id=generateId();
		this.downloadManager=downloadManager;
	}
	
	private static int generateId(){
		baseOnId++;
		if(baseOnId>Integer.MAX_VALUE-10){
			baseOnId=0;
		}
		return baseOnId;
	}
	
	public int getId(){
		return id;
	}
	
	/**
	 * Start to download
	 * @param downloadTask
	 */
	public void assignDownloadTask(DownloadTask downloadTask){
		this.downloadTask=downloadTask;
		this.start();
	}
	
	public DownloadTask getDownloadTask(){
		return downloadTask;
	}
	
	private void start(){
		if(thread==null){
			thread=new Thread(this);
			thread.start();
		}
	}
	
	public void stop(){
		isRunning=false;
	}
	
	public void forceStop(){
		isRunning=false;
		this.close();
	}
	
	
	
	@Override
	public void run() {
		if(downloadTask==null){
			return;
		}
		isRunning=true;
		
		try {
			
			fireDownloadStatusChanged(DownloadTask.STATUS_PREPARE);
			if(!isRunning){
				return;
			}
			
			URL url=new URL(downloadTask.getSourceUrl());
			connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(TIMEOUT_CONNECT);
			connection.setReadTimeout(TIMEOUT_READ);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			inputStream=connection.getInputStream();
			
			String tempPath=wrapTempFileName(downloadTask.getDestPath());
			File tempFile=new File(tempPath);
			Log.i(TAG, "run>>>tempPath="+tempPath);
			if(tempFile.exists()){
				tempFile.delete();
			}
			
			Log.i(TAG, "run>>>111  tempPath="+tempPath);
			if(!tempFile.exists()){
//				File directory=new File(tempFile.getParent()+"/");
//				Log.i(TAG, "run>>>22  directory.getAbsolutePath()="+directory.getAbsolutePath());
//				directory.mkdirs();
				tempFile.getParentFile().mkdirs();
				tempFile.createNewFile();
			}
			
			Log.i(TAG, "run>>>222  tempPath="+tempPath);
			fileOutputStream=new FileOutputStream(tempFile);
			
			fireDownloadStatusChanged(DownloadTask.STATUS_DOWNLOADING);
			if(!isRunning){
				return;
			}
			
			byte[] buffer=new byte[10240];
			int count=0;
			while((count=inputStream.read(buffer))!=-1){
				if(!isRunning){
					return;
				}
				fileOutputStream.write(buffer, 0, count);
			}
			fileOutputStream.flush();
			
			fileOutputStream.close();
			inputStream.close();
			connection.disconnect();
			
			fireDownloadStatusChanged(DownloadTask.STATUS_VERIFY_TEMP_FILE);
			if(!isRunning){
				return;
			}
			
			File newFile=new File(downloadTask.getDestPath());
			boolean isSuccess=IOUtil.renameFile(tempFile, newFile, true);
			if(!isSuccess){
				Log.e(TAG, "run>>>renameFile isSuccess="+isSuccess);
			}
			
			if(!isRunning){
				return;
			}
			
			fireDownloadStatusChanged(DownloadTask.STATUS_SUCCESS);
			
		} catch (Exception e) {
			Log.e(TAG, "run>>>"+getId()+">>>e="+e.toString());
			
			fireDownloadStatusChanged(DownloadTask.STATUS_FAIL);
			
			e.printStackTrace();
		} finally{
			this.close();
			if(!isRunning){
				fireDownloadStatusChanged(DownloadTask.STATUS_STOP);
			}
		}
		
	}
	
	private static String wrapTempFileName(String fileName){
		return fileName+=SUFFIX_TEMP;
	}
	
	public void close(){
		if(inputStream!=null){
			try{
				inputStream.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try{
				connection.disconnect();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(fileOutputStream!=null){
			try{
				fileOutputStream.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void fireDownloadStatusChanged(int newStatus){
		int oldStatus=downloadTask.getStatus();
		downloadTask.setStatus(newStatus);
		downloadManager.onDownloadStatusChanged(this,downloadTask, oldStatus,newStatus);
	}

}
