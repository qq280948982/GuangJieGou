package lib.tim.download;

public class DownloadTask {

	public static final int STATUS_NORMAL=0;//this is the same to pause.
//	public static final int STATUS_PAUSE=100;
	public static final int STATUS_PREPARE=20;
	public static final int STATUS_DOWNLOADING=100;
//	public static final int STATUS_QUEUING=110;
	public static final int STATUS_VERIFY_TEMP_FILE=200;
	public static final int STATUS_SUCCESS=300;
	public static final int STATUS_FAIL=400;
	public static final int STATUS_STOP=500;
	
	private int status=STATUS_NORMAL;
	private String taskName=null;
	private String sourceUrl=null;
	private String destPath=null;
	
	private float downloadedPercent=0.0f;
	private String destDirectory=null;//end with '/'
	private String destFileName=null;//No '/' but with suffix, example:  filename.txt
	
	private Object data=null;
	
	private static final char STR_GAN='/';
	
	private boolean isRemoveManuallyWhenSuccess=false;
	private boolean isRemoveManuallyWhenFail=false;
	
	//
	private int id;
	private static int baseOnId=0;
	
	public DownloadTask(String taskName,String sourceUrl,String destPath){
		this.id=generateId();
		this.init(taskName, sourceUrl, destPath);
	}
	
	private static int generateId(){
		baseOnId++;
		if(baseOnId>Integer.MAX_VALUE-10){
			baseOnId=0;
		}
		return baseOnId;
	}
	
	public void init(String taskName,String sourceUrl,String destPath){
		this.taskName=taskName;
		this.sourceUrl=sourceUrl;
		this.destPath=destPath;
		status=STATUS_NORMAL;
		downloadedPercent=0.0f;
		destDirectory=null;
		destFileName=null;
		this.cutDirectoryAndFileName();
	}
	
	private void cutDirectoryAndFileName(){
		int lastGanIndex=destPath.lastIndexOf(STR_GAN);
		if(lastGanIndex!=-1){
			destDirectory=this.destPath.substring(0, lastGanIndex+1);
			destFileName=this.destPath.substring(lastGanIndex+1);
		}
	}
	
	public boolean isStatusSuccess(){
		return status==STATUS_SUCCESS;
	}
	
	public boolean isStatusFail(){
		return status==STATUS_FAIL;
	}
	
	public int getId(){
		return id;
	}

	public int getStatus() {
		return status;
	}

	void setStatus(int status) {
		this.status = status;
	}
	
	public void resetStatus(){
		this.status=STATUS_NORMAL;
	}

	public float getDownloadedPercent() {
		return downloadedPercent;
	}

	void setDownloadedPercent(float downloadedPercent) {
		this.downloadedPercent = downloadedPercent;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getDestPath() {
		return destPath;
	}
	
	public String getDestDirectory(){
		return destDirectory;
	}
	
	public String getDestFileName(){
		return destFileName;
	}
	
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	

	public boolean isRemoveManuallyWhenSuccess() {
		return isRemoveManuallyWhenSuccess;
	}

	public void setRemoveManuallyWhenSuccess(boolean isRemoveManuallyWhenSuccess) {
		this.isRemoveManuallyWhenSuccess = isRemoveManuallyWhenSuccess;
	}

	public boolean isRemoveManuallyWhenFail() {
		return isRemoveManuallyWhenFail;
	}

	public void setRemoveManuallyWhenFail(boolean isRemoveManuallyWhenFail) {
		this.isRemoveManuallyWhenFail = isRemoveManuallyWhenFail;
	}

	public DownloadTask clone(){
		DownloadTask obj=new DownloadTask(taskName, sourceUrl, destPath);
		obj.isRemoveManuallyWhenSuccess=isRemoveManuallyWhenSuccess;
		obj.isRemoveManuallyWhenFail=isRemoveManuallyWhenFail;
		obj.data=data;
		return obj;
	}
	
}

