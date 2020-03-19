package com.lorythegamer.wiiarchive.example;

import java.io.File;

import com.lorythegamer.wiiarchive.U8Archive;
import com.lorythegamer.wiiarchive.filesystem.WiiFileSystemObj;

public class Example1 {

	public static void main(String[] args) throws Exception {
		//Archive's file
		File f = new File("res//ExampleArchive.arc");
		
		//Create a new archive and read
		U8Archive arc = new U8Archive(f).read();
		
		//Print the name of each root child file
		for(WiiFileSystemObj file : arc.root.getChildren()) System.out.println(file.name);
		
	}
}
