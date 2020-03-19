package com.lorythegamer.wiiarchive;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.lorythegamer.wiiarchive.exception.UnknownARCFileSystemObjException;
import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObj;
import com.lorythegamer.wiiarchive.filesystem.objs.WiiFileSystemFile;
import com.lorythegamer.wiiarchive.filesystem.objs.WiiFileSystemFolder;
import com.lorythegamer.wiiarchive.filesystem.objs.WiiFileSystemRoot;
import com.lorythegamer.wiiarchive.util.ArrayUtil;
import com.lorythegamer.wiiarchive.util.UnpackUtil;

public class U8Archive {

	boolean print;
	byte[] arcContent;
	
	public WiiFileSystemRoot root;
	final byte[] arcMagicDefault = new byte[] {85, -86, 56, 45};
	
	class ReadInfo {
		public int startPos;
		public String stringTable = "";
		public int currentNode;
	}
	
	public U8Archive(File arcFile) throws Exception {
		this(arcFile, false);
	}
	
	public U8Archive(File arcFile, boolean print) throws Exception {
		this.print = print;
		this.arcContent = Files.readAllBytes(Paths.get(arcFile.toURI()));
	}
	
	public U8Archive read() {
		UnpackUtil.init();
		
		ReadInfo info = new ReadInfo();
		info.startPos = 0;
		
		root = new WiiFileSystemRoot();	
		
		try {			
			//Read initial data: fstStart, fstSize and dataOffset
			Long[] initialData = UnpackUtil.unpack(">III", ArrayUtil.extract(arcContent, 4, 16), this.print);
			int fstStart = initialData[0].intValue();
			int fstSize = initialData[1].intValue();
			//int dataOffset = (int) initialData[2];			
			
			/*System.out.println("FST Start: " + fstStart);
			System.out.println("FST Size: " + fstSize);
			System.out.println("Data Offset: " + dataOffset);*/
			
			//Read FST and root node
			int pointer = info.startPos + fstStart + 8;
			int savePos = pointer + 4;
			int rootNodeLastChild = UnpackUtil.unpack(">I", ArrayUtil.extract(arcContent, pointer, savePos), this.print)[0].intValue();
			
			//System.out.println("Root Node Last Child: " + rootNodeLastChild);
			
			//Read the String Table
			int strTablePointer = savePos + ((rootNodeLastChild - 1) * 12);
			int strTableLength = fstSize - (rootNodeLastChild * 12);
			byte[] strTableArr = ArrayUtil.extract(arcContent, strTablePointer, strTablePointer + strTableLength);
			info.stringTable = new String(strTableArr);
			
			//System.out.println("String Table:" + info.stringTable);
			
			//Read the root node
			info.currentNode = 1;
			readDir(arcContent, root, savePos, rootNodeLastChild, info);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public WiiFileSystemRoot getArchiveRoot() {
		return root;
	}	
	
	private void readDir(byte[] data, WiiFileSystemFolder dir, int dataPointer, int lastChild, ReadInfo info) throws Exception {		
		while(info.currentNode < lastChild) {
			info.currentNode += 1;
			
			//Read dir info
			Long[] currentInfo = UnpackUtil.unpack(">III", ArrayUtil.extract(data, dataPointer, dataPointer + 12), this.print);
			
			int value = currentInfo[0].intValue();
			int dataOffset = currentInfo[1].intValue();
			int size = currentInfo[2].intValue();
			int nameOffset = value & 0xFFFFFF;
			int objType = value >> 24;
		
			//Checks the type. If 0, create a File. If 1, create a Directory. Else, throw an exception.
			WiiFileSystemObj newObj = null;
			
			int nameEnd = info.stringTable.indexOf('\0', nameOffset);
			String newObjName = info.stringTable.substring(nameOffset, nameEnd);
			
			if(objType == 0) newObj = new WiiFileSystemFile(newObjName);
			else if(objType == 1) newObj = new WiiFileSystemFolder(newObjName);
			else throw new UnknownARCFileSystemObjException("Cannot decode the object type ID " + objType + "as a known Wii Filesystem object");
		
			dir.addChild(newObj);		
			//System.out.println("Loaded decompressed file: " + newObjName);
			
			if(newObj.isFile()) {
				int readPointer = info.startPos + dataOffset;
				((WiiFileSystemFile) newObj).data = ArrayUtil.extract(data, readPointer, readPointer + size);
				dataPointer += 12;
			}			
			else if (newObj.isFolder()) readDir(data, (WiiFileSystemFolder)newObj, dataPointer, size, info);			
			else throw new UnknownARCFileSystemObjException("Cannot decode the object type ID " + objType + "as a known Wii Filesystem object");
		}
	}
	
}