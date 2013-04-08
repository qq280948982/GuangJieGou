package lib.tim.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static boolean loggable = false;
	private static boolean writeFile = false;
	
	public static String SDCARD = "/sdcard/CommonTools";
	
	public static void d(String tag, String msg){
		if(loggable){
			android.util.Log.d(tag, msg);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void i(String tag, String msg){
		if(loggable){
			android.util.Log.i(tag, msg);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void e(String tag, String msg){
		if(loggable){
			android.util.Log.e(tag, msg);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Exception e){
		if(loggable){
			android.util.Log.e(tag, msg, e);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void w(String tag, String msg){
		if(loggable){
			android.util.Log.w(tag, msg);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void v(String tag, String msg) {
		if(loggable) {
			android.util.Log.v(tag, msg);
			toFiletoFile(tag, msg);
		}
	}
	
	public static void toFiletoFile(String tag,String msg){
		String fileName = "log.txt";
		toFiletoFile(tag, fileName, msg);
	}
	
	public static void toFiletoFile(String tag,String fileName,String msg){
		try {
			if(loggable && writeFile){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				msg = "[" + sdf.format(new Date()) + " - " + tag + "]" +  msg + "\n";
				
				writeFileEnd(SDCARD + "/" + fileName, msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writeFileEnd(String filepath, String text) {
		OutputStreamWriter wr = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			wr = new OutputStreamWriter(new FileOutputStream(filepath, true),
					"UTF8");
			wr.write(text);
			wr.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void loggagble(boolean loggable) {
		Log.loggable = loggable;
	}
	
	public static void writeable(boolean writeable) {
		Log.writeFile = writeable;
	}
}
