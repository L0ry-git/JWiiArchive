package com.lorythegamer.wiiarchive.util;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//Replicates python's struct unpack
public class UnpackUtil {
	
	public static ByteOrder byteOrder;
	public static ByteOrder nativeByteOrder;

	public static void init() {
		nativeByteOrder = java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.BIG_ENDIAN ? 
				ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
		byteOrder = nativeByteOrder;
	}

	private static int unpackLength(char[] formatChars) {
		int ret = 0;
		if(formatChars.length == 0) return -1;

		for(char c : formatChars) {
			ret += size(c);
		}

		return ret != 0 ? ret : -1;
	}

	private static int size(char c) {
		switch(c) {

		case 'i':
		case 'I':
			return 4;
		case 'h':
		case 'H':
			return 2;
		case 'b':
		case 'B':
			return 1;
		default: return 0;
		}
	}

	public static Long[] unpack(String format, byte[] data, boolean print) throws Exception {
		char[] formatChars = format.toCharArray();
		int formatLength = unpackLength(formatChars);

		if(formatLength != data.length || formatLength == -1) throw new IllegalArgumentException();

		char byteOrderIdentifier = format.charAt(0);

		List<Long> ret = new ArrayList<Long>(byteOrderIdentifier == '@' ||
				byteOrderIdentifier == '>' ||
				byteOrderIdentifier == '<' ||
				byteOrderIdentifier == '!'
				? format.length() - 1 : format.length());
		ByteArrayInputStream reader = new ByteArrayInputStream(data);

		byteOrder = byteOrderIdentifier == '>' ||
				byteOrderIdentifier == '!' ? ByteOrder.BIG_ENDIAN :
				byteOrderIdentifier == '<' ? ByteOrder.LITTLE_ENDIAN :
				nativeByteOrder;


		for(int i = 1; i < format.toCharArray().length; i++) {
			char c = format.charAt(i);
			Method m = UnpackUtil.class.getMethod("unpack" + size(c) * 8 + "b", 
					boolean.class, byte[].class);
			boolean isBigEndian = (byteOrder == ByteOrder.BIG_ENDIAN);

			byte[] toUnpack = new byte[size(c)];
			reader.read(toUnpack);

			long toAdd = (long)m.invoke(null, Character.isUpperCase(c), isBigEndian ? toUnpack : 
				ArrayUtil.reverseByteArray(toUnpack));
			
			if(print) {
				System.out.println("Unpack type: " + c);
				
				System.out.println("Method: " + "unpack" + size(c) * 8 + "b");
				System.out.println("Unsigned: " + Character.isUpperCase(c));
				System.out.println("Byte order: " + byteOrder);
				for(byte b : toUnpack) System.out.println("Array " + i + ", byte: " + b);

				System.out.println("Unpacked: " + toAdd);
				System.out.println("-------------------");
			}
			
			ret.add(new Long(toAdd));
		}

		return ret.toArray(new Long[ret.size()]);
	}

	//Lory's breakfast
	public static long unpack8b(boolean unsigned, byte[] val){
		//unpack byte value in a long variable
		long ret = (val[0] & 0xFF);

		//if it's negative and two's complement unsigned integer get signed value 
		//else have a coffee :>
		ret = !unsigned && ((ret >>> 3 & 1) == 1) ? (((ret ^ 0x7F) & 0x7F) + 1) * -1 : ret;

		return ret;
	}

	//Lory's breakfast 2
	public static long unpack16b(boolean unsigned, byte[] val) {
		//unpack bytes in a long variable
		long ret = (((long) (val[0]& 0xff)) << 8) +
				((long) (val[1] & 0xff));

		//if it's negative and two's complement unsigned integer get signed value 
		//else have a coffee :>
		ret = !unsigned && ((ret >>> 7 & 1) == 1) ? (((ret ^ 0x7FFF) & 0x7FFF) + 1) * -1 : ret;

		return ret;
	}

	//Lory's breakfast 3
	public static long unpack32b(boolean unsigned, byte[] val) {
		//unpack bytes in a long variable
		long ret = ((long)(val[0] & 0xff) << 24) +
				(((long) (val[1] & 0xff))<< 16) +
				(((long) (val[2]& 0xff)) << 8) +
				((long) (val[3] & 0xff));

		//if it's negative and two's complement unsigned integer get signed value
		//else have a coffee :>
		ret = !unsigned && ((ret >>> 15 & 1) == 1) ? (((ret ^ 0x7FFFFFFF) & 0x7FFFFFFF) + 1) * -1 : ret;

		return ret;
	}


}
