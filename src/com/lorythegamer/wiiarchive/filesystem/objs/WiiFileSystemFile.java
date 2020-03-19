package com.lorythegamer.wiiarchive.filesystem.objs;

import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObj;
import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObjType;

public class WiiFileSystemFile extends WiiFileSystemObj {

	public byte[] data;
	
	public WiiFileSystemFile(String name) {
		super(WiiFileSystemObjType.FILE, name);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
