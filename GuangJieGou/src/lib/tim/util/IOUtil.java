package lib.tim.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public final class IOUtil {

	private static final String TAG="IOUtil";
	
	private IOUtil() {
		
	}
	
	public static void copyFile(String from, String to,boolean isDeleteExistDestFile) throws IOException {
		copyFile(new File(from), new File(to),isDeleteExistDestFile);
	}
	
	public static void copyFile(File from, File to,boolean isDeleteExistDestFile) throws IOException {
		if(to.exists()){
			if(isDeleteExistDestFile){
				to.delete();
			}
		}
		final FileInputStream fis = new FileInputStream(from);
		final FileOutputStream fos = new FileOutputStream(to);
		writeFile(fis, fos);
		closeIOStream(fos);
		closeIOStream(fis);
	}
	
	public static void saveInputStreamAsFile(InputStream inputStream, File target) throws IOException {
		final FileOutputStream fos = new FileOutputStream(target);
		writeFile(inputStream, fos);
		closeIOStream(fos);
		closeIOStream(inputStream);
	}
	
	private static void writeFile(InputStream inputStream, OutputStream outputStream) throws IOException {
		final byte[] buf = new byte[10240];
		int len;
		while((len = inputStream.read(buf)) !=-1) {
			outputStream.write(buf, 0, len);
		}
		outputStream.flush();
		closeIOStream(outputStream);
		closeIOStream(inputStream);
	}
	
	public static void remove(String filePath) {
		try{
			File file=new File(filePath);
			remove(file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void remove(File file) {
		if(file != null) {
			if(!file.isDirectory()) {
				file.delete();
			}
			else {
				String[] children = file.list();
				int size = children.length;
				for(int i = 0; i < size; i++) {
					remove(new File(file, children[i]));
				}
				file.delete();
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param newFile Must contain full path.
	 * @param isDeleteExistDestFile
	 * @return
	 */
	public static boolean renameFile(File file,File newFile,boolean isDeleteExistDestFile){
		if(newFile.exists()){
			if(isDeleteExistDestFile){
				newFile.delete();
			}
		}
		return file.renameTo(newFile);
	}
	
	public static void closeIOStream(Closeable stream) {
		if(stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Create file or directory.
	 * 
	 * @param name
	 *            The name for directory or file.Directories are denoted with a
	 *            trailing slash "/" .
	 * @param autoBuildDir
	 *            boolean indicating whether super-folder should be create or
	 *            not.
	 * @param deleteExist
	 *            boolean indicating whether delete the exist file or not.
	 * @return true if the file is created successfully,false otherwise(Maybe
	 *         the file is already exist,or other problems).
	 */
	public static boolean createFile(String fcPath, boolean autoBuildDir,
			boolean deleteExist) {
		File file=new File(fcPath);
		try{
			if(deleteExist){
				if(file.exists()){
					file.delete();
					file=new File(fcPath);
				}
			}
			if(autoBuildDir){
				file.getParentFile().mkdirs();
			}
			if(fcPath.endsWith("/")){
				return file.mkdir();
			}
			return file.createNewFile();
		}
		catch(Exception e){
			Log.e(TAG, "createFile>>>e="+e.toString());
		}
		return false;
	}
	
	public static boolean isExistFile(String name) {
		File file=new File(name);
		return file.exists();
	}
	
	public static void readSomeDataFromFile(String path, byte[] data) {
		InputStream is = null;
		try {
			File file = new File(path);
			is=new FileInputStream(file);
			is.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] buildDataFromFile(String path) {
		return buildDataFromFile(path, 1024);
	}

	public static byte[] buildDataFromFile(String path, int bufferSize) {
		InputStream is = null;
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		try {
			is=new FileInputStream(file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ByteArrayOutputStream baos = null;
		if (is != null) {
			try {
				baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[bufferSize];
				int i = 0;
				while ((i = is.read(buffer)) != -1) {
					baos.write(buffer, 0, i);
				}
				baos.flush();
				return baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	public static boolean writeStringToFile(String text,String filePath){
		OutputStream os=null;
		try{
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			os=new FileOutputStream(file);
			byte[] data=text.getBytes("UTF-8");
			os.write(data);
			os.flush();
			os.close();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(os!=null){
				try{
					os.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static File[] listFilesByDirectory(String directoryPath,boolean isOnlyFile){
		File dir = new File(directoryPath);
		if(!dir.exists()){
			return null;
		}
		File[] files=null;
		if(isOnlyFile){
			files=dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.isFile()){
						return true;
					}
					return false;
				}
			});
		}
		else{
			files=dir.listFiles();
		}
		return files;
	}
	
	public static String readInputStream(InputStream inputStream) {
		StringBuffer retString = new StringBuffer();
		if(inputStream != null) {
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			try {
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);
				String line = reader.readLine();
				while(line != null) {
					retString.append(line);
					line = reader.readLine();
				}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				closeIOStream(reader);
				closeIOStream(inputStreamReader);
				closeIOStream(inputStream);
			}
		}
		return retString.toString();
	}
	
}
