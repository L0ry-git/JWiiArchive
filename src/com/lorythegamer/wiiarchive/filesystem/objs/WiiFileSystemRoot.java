package com.lorythegamer.wiiarchive.filesystem.objs;

import com.lorythegamer.wiiarchive.exception.WiiFSRootException;

public class WiiFileSystemRoot extends WiiFileSystemFolder {

	public WiiFileSystemRoot() {
		super("root", true);
		
		this.parent = null;
	}
	
	@Override
	public void setParent(WiiFileSystemFolder parent) throws WiiFSRootException {
		throw new WiiFSRootException("The root MUST be the root. It cannot have a parent!");
	}
	
	@Override
	public void unlinkFromParent() throws WiiFSRootException {
		throw new WiiFSRootException("The root MUST be the root. It cannot have a parent!");
	}
	
	@Override
	public WiiFileSystemFolder getParent() throws WiiFSRootException {
		throw new WiiFSRootException("The root MUST be the root. It cannot have a parent!");
	}

}
