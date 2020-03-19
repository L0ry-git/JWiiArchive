package com.lorythegamer.wiiarchive.filesystem.objs;

import java.util.ArrayList;
import java.util.List;

import com.lorythegamer.wiiarchive.exception.WiiFSRootException;
import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObj;
import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObjType;

public class WiiFileSystemFolder extends WiiFileSystemObj {
	
	public List<WiiFileSystemObj> children;
	public boolean isRoot;
	
	public WiiFileSystemFolder(String name, boolean isRoot) {
		super(WiiFileSystemObjType.DIR, name);
		
		if(isRoot) this.type = WiiFileSystemObjType.ROOT;
		
		this.isRoot = isRoot;
		this.children = new ArrayList<WiiFileSystemObj>();
	}
	
	public WiiFileSystemFolder(String name) {
		this(name, false);
	}

	public List<WiiFileSystemObj> getChildren() {
		return children;
	}

	public void addChild(WiiFileSystemObj obj) {
		this.children.add(obj);
		
		for(WiiFileSystemObj child : children) {
			try {
				child.setParent(this);
			} catch (WiiFSRootException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
