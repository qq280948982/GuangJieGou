package com.tim.guangjiegou.pojo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.database.Cursor;

import com.tim.guangjiegou.util.CommonUtil;

public abstract class VO implements Serializable{
	
	public VO() {
		
	}
	
	@Override
	public String toString() {
		final StringBuffer toString = new StringBuffer();
		Class<?> clazz = getClass();
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (Field f : fields) {
				final String fieldName = f.getName();
				final String methodNameSufix = String.valueOf(fieldName.charAt(0))
						.toUpperCase() + fieldName.substring(1);
				final Method method = getMethod(clazz, methodNameSufix);
				if (method == null) {
					continue;
				}
				Object value = null;
				try {
					value = method.invoke(this);
				} catch (Exception e) {
					value = null;
				}
				toString.append(f.getName() + " = " + value + "\n");
			}
		}
		return toString.toString();
	}

	private Method getMethod(Class<?> clazz, String methodNameSufix) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod("get" + methodNameSufix);
		} catch (Exception e) {
			try {
				method = clazz.getDeclaredMethod("is" + methodNameSufix);
			} catch (Exception e2) {
				try {
					method = clazz.getDeclaredMethod("has" + methodNameSufix);
				} catch (Exception e3) {
					method = null;
				}
			}
		}
		return method;
	}
	
	protected boolean booleanOfString(String string) {
		boolean flag = false;
		if(!CommonUtil.isNull(string)) {
			string = string.trim();
			flag = string.equalsIgnoreCase("Y") || string.equalsIgnoreCase("YES") ||
					string.equalsIgnoreCase("TRUE") || string.equalsIgnoreCase("T");
		}
		return flag;
	}
	
	protected double doubleOfString(String string) {
		double retValue = 0;
		if(!CommonUtil.isNull(string)) {
			string = string.trim();
			try {
				retValue = Double.parseDouble(string);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return retValue;
	}
	
	protected String getStringByColumnName(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
}
