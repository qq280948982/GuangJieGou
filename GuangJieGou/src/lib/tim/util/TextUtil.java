package lib.tim.util;

public class TextUtil {

	public static final String STR_TRUE = "true";

	public static boolean convertStringToBoolean(String str) {
		if (str == null) {
			return false;
		}
		str = str.trim().toLowerCase();
		if (str.equals(STR_TRUE)) {
			return true;
		}
		return false;
	}

	public static int convertStringToInt(String str) {
		return convertStringToInt(str, 0);
	}

	public static int convertStringToInt(String str, int failValue) {
		if (str == null) {
			return failValue;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failValue;
	}

	public static long convertStringToLong(String str, long failValue) {
		if (str == null) {
			return failValue;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failValue;
	}

	public static byte[] hexToByte(String input) {
		byte[] output = new byte[input.length() / 2];
		String input2 = input.toLowerCase();
		for (int i = 0; i < input2.length(); i += 2) {
			output[i / 2] = hexToByte(input2.charAt(i), input2.charAt(i + 1));
		}
		return output;
	}

	public static byte hexToByte(char char1, char char2) {
		// Returns hex String representation of byte b
		byte output = 0x00;
		if (char1 == '0') {
			output = 0x00;
		} else if (char1 == '1') {
			output = 0x10;
		} else if (char1 == '2') {
			output = 0x20;
		} else if (char1 == '3') {
			output = 0x30;
		} else if (char1 == '4') {
			output = 0x40;
		} else if (char1 == '5') {
			output = 0x50;
		} else if (char1 == '6') {
			output = 0x60;
		} else if (char1 == '7') {
			output = 0x70;
		} else if (char1 == '8') {
			output = (byte) 0x80;
		} else if (char1 == '9') {
			output = (byte) 0x90;
		} else if (char1 == 'a') {
			output = (byte) 0xa0;
		} else if (char1 == 'b') {
			output = (byte) 0xb0;
		} else if (char1 == 'c') {
			output = (byte) 0xc0;
		} else if (char1 == 'd') {
			output = (byte) 0xd0;
		} else if (char1 == 'e') {
			output = (byte) 0xe0;
		} else if (char1 == 'f') {
			output = (byte) 0xf0;
		}

		if (char2 == '0') {
			output = (byte) (output | (byte) 0x00);
		} else if (char2 == '1') {
			output = (byte) (output | (byte) 0x01);
		} else if (char2 == '2') {
			output = (byte) (output | (byte) 0x02);
		} else if (char2 == '3') {
			output = (byte) (output | (byte) 0x03);
		} else if (char2 == '4') {
			output = (byte) (output | (byte) 0x04);
		} else if (char2 == '5') {
			output = (byte) (output | (byte) 0x05);
		} else if (char2 == '6') {
			output = (byte) (output | (byte) 0x06);
		} else if (char2 == '7') {
			output = (byte) (output | (byte) 0x07);
		} else if (char2 == '8') {
			output = (byte) (output | (byte) 0x08);
		} else if (char2 == '9') {
			output = (byte) (output | (byte) 0x09);
		} else if (char2 == 'a') {
			output = (byte) (output | (byte) 0x0a);
		} else if (char2 == 'b') {
			output = (byte) (output | (byte) 0x0b);
		} else if (char2 == 'c') {
			output = (byte) (output | (byte) 0x0c);
		} else if (char2 == 'd') {
			output = (byte) (output | (byte) 0x0d);
		} else if (char2 == 'e') {
			output = (byte) (output | (byte) 0x0e);
		} else if (char2 == 'f') {
			output = (byte) (output | (byte) 0x0f);
		}
		return output;
	}
	
	public static String ellipsizeString(String string, int limit) {
		String retValue = "";
		if(string != null) {
			if(string.length() > limit) {
				retValue = string.substring(0, limit) + "...";
			}
			else {
				retValue = string;
			}
		}
		return retValue;
	}
	
	public static boolean isStringEmpty(String s){
		return s==null || s.trim().equals("");
	}
}
