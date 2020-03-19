package com.lorythegamer.wiiarchive.filesystem;

import com.lorythegamer.wiiarchive.exception.WiiFSRootException;
import com.lorythegamer.wiiarchive.filesystem.objs.WiiFileSystemFolder;

public class WiiFileSystemObj {
	
	public WiiFileSystemFolder parent;	
	public WiiFileSystemObjType type;
	
	public String name = "<null>";
	
	public WiiFileSystemObj(WiiFileSystemObjType type, String name) {
		this.type = type;
		this.name = name;
	}

	public WiiFileSystemFolder getParent() throws WiiFSRootException {
		return parent;
	}

	public void setParent(WiiFileSystemFolder parent) throws WiiFSRootException {
		this.parent = parent;
	}
	
	public void unlinkFromParent() throws WiiFSRootException {
		this.parent.children.remove(this);
		this.parent = null;
	}

	public WiiFileSystemObjType getType() {
		return type;
	}
	
	public boolean isRoot() {
		return type == WiiFileSystemObjType.ROOT;
	}
	
	public boolean isFile() {
		return type == WiiFileSystemObjType.FILE;
	}
	
	public boolean isFolder() {
		return type == WiiFileSystemObjType.DIR;
	}
	
	

}
