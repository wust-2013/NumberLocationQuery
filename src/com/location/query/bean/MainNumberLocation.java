package com.location.query.bean;

import java.io.DataInputStream;
import java.io.IOException;

public class MainNumberLocation {
	
	static void searchNum(NumberInfoAction action,String fileName) throws IOException{
//		DataInputStream in=new DataInputStream(System.in);
//		while(true){
//			System.out.println("输入查询号码小于等于7位.如10086，0755，1367002");
//			int searchNum=0;
//			searchNum=in.readInt();
//			String location=action.getCityNameByNumber(fileName, searchNum);
//			System.out.println("号码所在城市：  "+location);
//		}
		
		System.out.println(action.getCityNameByNumber("res/phonenumloc.dat", 110));
		System.out.println(action.getCityNameByNumber("res/phonenumloc.dat", 120));
		System.out.println(action.getCityNameByNumber("res/phonenumloc.dat", 1527180));
		System.out.println(action.getCityNameByNumber("res/phonenumloc.dat", 1869617));
		System.out.println(action.getCityNameByNumber("res/phonenumloc.dat", 1828943));
	}
	
	static void convertFile(NumberInfoAction action) throws IOException{
		String inFileName="res/phonenumloc.txt";
		String outFileName="res/phonenumloc.dat";
		
		boolean result=action.changeTxtToBinary(inFileName, outFileName);
		
		if(result){
			System.out.println("转换结束.");
			searchNum(action,outFileName);
		}else{
			System.out.println("转换失败");
		}
	}
	
    public static void main(String[] args){
    	NumberInfoAction action=new NumberInfoAction();
    	int isConvertFile=1;
    	System.out.println("是否要把txt文件转换成2进制文件,(y/n)");
    	
    	
    	try {
//			isConvertFile=in.readInt();
			System.out.println(isConvertFile);
			if(isConvertFile==1){
				convertFile(action);
			}else{
				searchNum(action, "res/phonenumloc.dat");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
