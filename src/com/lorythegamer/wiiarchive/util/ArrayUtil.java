package com.lorythegamer.wiiarchive.util;

public class ArrayUtil {
	
	public static byte[] extract(byte[] arr, int from, int to) {
		byte[] ret = new byte[to - from];
		for(int i = from; i < to; i++) {
			ret[i - from] = arr[i];
		}
		return ret;
	}
	
	public static byte[] reverseByteArray(byte[] bytes){
		for(int i = 0; i<(bytes.length / 2); i++){
			bytes[i] += bytes[bytes.length - i - 1];
			bytes[bytes.length - i - 1] = (byte) (bytes[i] - bytes[bytes.length - i - 1]);
			bytes[i] -= bytes[bytes.length-i-1];
		}

		return bytes;
	}

}
