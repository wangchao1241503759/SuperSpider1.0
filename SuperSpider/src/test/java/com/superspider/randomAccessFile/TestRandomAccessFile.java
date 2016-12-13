package com.superspider.randomAccessFile;

import java.io.File;
import java.io.RandomAccessFile;

public class TestRandomAccessFile {
	public static void main(String[] args)throws Exception {
		
		File file = new File("d://wangchao.dat");
		RandomAccessFile rf = new RandomAccessFile(file, "rw");
		for(int i = 0 ; i< 10 ; i++){
			rf.writeDouble(i * 1.414);
			System.out.println(rf.getFilePointer());
		}
		rf.close();
		rf = new RandomAccessFile(file, "rw");
		rf.seek(5 * 8);
		rf.writeDouble(47.0001);
		rf.close();
		rf = new RandomAccessFile(file, "rw");
		for(int i = 0 ; i < 10 ; i++){
			System.out.println("Value " + i + ": " + rf.readDouble());
		}
		rf.close();
	}
}
