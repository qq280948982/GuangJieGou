package com.tim.guangjiegou.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.view.KeyEvent;

import com.tim.guangjiegou.Constants;
import com.tim.guangjiegou.R;

public final class DebugUtil {

	private static final String TAG = "DebugUtil";
	
	private static final int EXPORT_TYPE_DATABASE = 11;
	private static final int EXPORT_TYPE_ALL_SYSTEM_FILES = 22;
	
	private static int sExportType = EXPORT_TYPE_ALL_SYSTEM_FILES;

	/**
	public static void printNSDictionary(NSDictionary nDictionary) {
		if (!CommonUtil.isNull(nDictionary)) {
			for (String key : nDictionary.allKeys()) {
				final NSObject nObject = nDictionary.objectForKey(key);
				if (nObject instanceof NSArray) {
					printNSArray((NSArray) nObject);
				} else if (nObject instanceof NSDictionary) {
					printNSDictionary((NSDictionary) nObject);

				} else {
					Log.d(TAG, key + " = " + nDictionary.objectForKey(key));
				}
			}
		}
	}

	public static void printNSArray(NSArray nArray) {
		// Log.e(TAG, "print NSArray..");
		if (!CommonUtil.isNull(nArray)) {
			printArray(nArray.getArray());
		}
	}
*/
	public static void printArray(Object[] array) {
		// Log.e(TAG, "print array.." + array.length);
		if (!CommonUtil.isNull(array)) {
			for (Object o : array) {
				Log.d(TAG, o.toString());
			}
		}
	}

	public static boolean exportDatabaseByClickMenuKey(Context context, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if(sExportType == EXPORT_TYPE_DATABASE) {
//				exportDatabase(context);
			}
			else if(sExportType == EXPORT_TYPE_ALL_SYSTEM_FILES){
				exportSystemFolder(context);
			}
		}
		return false;
	}

	/*
	
	public static void exportDatabase(Context context) {
		if (!Constants.ENABLE_DEBUG) {
			return;
		}
		String packageName = R.class.getPackage().getName();
		File dbFile = new File(SQLiteUtil.DATABASE_PATH, SQLiteUtil.DB_NAME);
		if (!dbFile.exists()) {
			return;
		}
		File dstDir = new File("/sdcard/Debug_Databases/" + packageName);
		if (!dstDir.exists()) {
			dstDir.mkdirs();
		}
		copyFile(dbFile, new File(dstDir, SQLiteUtil.DB_NAME));
		Log.d(TAG, "Export database success.");
	}
	
	*/
	
	public static void exportSystemFolder(Context context) {
		if(!Constants.ENABLE_DEBUG) {
			return ;
		}
		String packageName = R.class.getPackage().getName(); 
		File systemFolder = new File("/data/data/" + packageName);
		if(!systemFolder.exists()) {
			return ;
		}
		try {
			copyFolder(systemFolder, new File("/sdcard/Debug_System_data_data/" + packageName));
			Log.d(TAG, "Export system file success.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "export system file fail.");
		}
	}

	private static void copyFile(File from, File to) {
		if (!from.exists()) {
			return;
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);

			byte bt[] = new byte[1024];
			int c;
			while ((c = in.read(bt)) > 0) {
				out.write(bt, 0, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private static void copyFolder(File fromFolder, File toFolder) throws IOException {
		if(!fromFolder.exists()) {
			return ;
		}
		if(toFolder.exists()) {
			deleteFileAndFolder(toFolder);
		}
		copyFolder(fromFolder.getAbsolutePath(), toFolder.getAbsolutePath());
	}
	
	public static void copyFolder(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyFolder(dir1, dir2);
            }
        }
    }
	
	private static void deleteFileAndFolder(File fileOrFolder) {
		if(fileOrFolder == null || !fileOrFolder.exists()) {
			return ;
		}
		if(fileOrFolder.isDirectory()) {
			File[] children = fileOrFolder.listFiles();
			if(children != null) {
				for(File childFile : children){
					deleteFileAndFolder(childFile);
				}
			}
		}
		else {
			fileOrFolder.delete();
		}
	}
	
	public static void printValuesFolder(Context context) {
		String valuesFolder = com.common.tools.util.CommonUtil.getValuesFolder(context);
        Log.e(TAG, "Values Folder -> " + valuesFolder);
	}
}
