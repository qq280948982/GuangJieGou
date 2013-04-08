package lib.tim.util;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class DirectDBHandler {

	private static final String TAG="DirectDBUtil";
	
//	private static DirectDBHandler instance;
	
	private Context mContext;
	
	private String dbFolder =null;
	private String dbName=null;
	private int dbRawResourceId=-1;
	private int dbVersion=1;
	
	private SQLiteDatabase database;
	
	public DirectDBHandler(Context context){
		this.mContext=context;
		dbFolder = "/data/data/" + context.getPackageName() + "/databases/";
	}
	
//	public static void init(Context context){
//		if(instance==null){
//			instance=new DirectDBHandler(context);
//		}
//	}
//	
//	public static DirectDBHandler getInstance(){
//		return instance;
//	}
	
	public void setDbName(String dbName){
		this.dbName=dbName;
	}
	
	public String getDbName(){
		return this.dbName;
	}
	
	public void setDbRawResourceId(int rawResourceId){
		this.dbRawResourceId=rawResourceId;
	}

	public int getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(int dbVersion) {
		this.dbVersion = dbVersion;
	}

	/**
	 * 预先设置dbName与rawResourceId dbVersion,再调用该方法创建或更新DB.
	 */
	public void setupDatabase(){
		if(checkDatabaseExist()){
			int previousDbVersion=this.getDatabase().getVersion();
			if(previousDbVersion==this.dbVersion){
				return;
			}
			else{
				boolean isHandled=handleVersionNotMatch(previousDbVersion,this.dbVersion);
				if(isHandled){
					return;
				}
				//如果DB有更新，则删除旧DB。（默认的处理）
				this.deleteDatabase();
			}
		}
		this.createLocalDatabase();
	}
	
	/**
	 * 
	 * @param oldVersion
	 * @param newVersion
	 * @return 若为true，表示已被处理，不需要setupDatabase()做处理。若返回false,则执行默认处理:删除旧DB，再建立新DB。
	 */
	protected abstract boolean handleVersionNotMatch(int oldVersion,int newVersion);
	
	public boolean checkDatabaseExist() {
		File sqlFile = new File(dbFolder + dbName);
		return sqlFile.exists();
	}
	
	private void createLocalDatabase() {
		if(dbRawResourceId==-1){
			Log.e(TAG,"createLocalDatabase>>>No dbRawResourceId");
			return;
		}
		final File databasePath = new File(dbFolder);
		if(!databasePath.exists()) {
			databasePath.mkdirs();
		}
		final InputStream inputStream = mContext.getResources().openRawResource(dbRawResourceId);
		try {
			IOUtil.saveInputStreamAsFile(inputStream, new File(dbFolder + dbName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			IOUtil.closeIOStream(inputStream);
		}
		this.getDatabase().setVersion(dbVersion);
	}
	
	public SQLiteDatabase getDatabase() {
		if(database == null) {
			database = mContext.openOrCreateDatabase(dbName, Context.MODE_WORLD_WRITEABLE, null);
		}
		return database;
	}
	
	public void closeDatabase(){
		if(database != null) {
			database.close();
			database = null;
		}
	}
	
	public void deleteDatabase(){
		this.closeDatabase();
		mContext.deleteDatabase(dbName);
	}
}
