package com.location.query.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NumberInfoAction {
   private int phoneInfoCompressCount;
   private static int p_total;
   private ArrayList<String> cities;

   public NumberInfoAction(){
	   cities=new ArrayList<String>();
	}

   public String getCityNameByNumber(String outFileName, int number) throws IOException{
    	RandomAccessFile outFile=new RandomAccessFile(outFileName, "r");
    	
    	int phonesCount = 0;
    	phonesCount=outFile.readInt();
    	
    	System.out.println("wowo"+phonesCount+"  total cities"+p_total);
    	int left=0,right=phonesCount-1;
    	
    	outFile.seek(4+phonesCount*6);
    	char[] location = new char[ 14 ];
    	
		for ( int i = 0; i < location.length; i++ )
		   location[ i ] = outFile.readChar();
		System.out.println("nnnn"+new String(location).replace('\0', ' '));
		   
    	NumberInfoCompress infoMiddle=new NumberInfoCompress();
    	
    	while(left<=right){
    	   int middle = (left + right) / 2;
    	  
    	   outFile.seek(4+6*middle);
    	   
    	   infoMiddle.setM_before(outFile.readShort());
    	   infoMiddle.setM_after(outFile.readShort());
    	   infoMiddle.setM_cityIndex(outFile.readShort());
    	   
    	   if(number<infoMiddle.getBegin()){
    		   right=middle-1;
    	   }else if(number>(infoMiddle.getBegin()+infoMiddle.getSkip())){
    		   left=middle+1;
    	   }else{
    		   return doFindResultThing(outFile, phonesCount, infoMiddle);
    	   }
    	}
    	outFile.close();
    	return "未知号码";
    }
	
    private String doFindResultThing(RandomAccessFile outFile,int phonesCount,NumberInfoCompress infoMiddle) throws IOException{
    	long totalOffset=4+6*phonesCount+infoMiddle.getCityIndex()*28;
    	outFile.seek(totalOffset);
    	char[] location = new char[ 14 ];
		   for ( int i = 0; i < location.length; i++ )
		      location[ i ] = outFile.readChar();
		   // 将空字符取代为空格符并返回
		   outFile.close();
		   return new String( location ).replace( '\0', ' '  );
    }
    
	public boolean changeTxtToBinary(String inFileName,String outFileName){
		boolean flag=false;
		try {
			FileInputStream fis=new FileInputStream(inFileName);
			InputStreamReader fsr=new InputStreamReader(fis,"utf-8");
			BufferedReader inFile=new BufferedReader(fsr);
			
			RandomAccessFile outFile=new RandomAccessFile(outFileName, "rw");
			
		    phoneInfoCompressCount=0;
			outFile.writeInt(phoneInfoCompressCount);
			
			writeRecords(inFile,outFile);
			
			outFile.seek(0);
			outFile.writeInt(phoneInfoCompressCount);
			
			outFile.seek(4+phoneInfoCompressCount*6);
			
			writeCities(outFile);
			
			inFile.close();
			outFile.close();
			
			flag=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return flag;
		}
	}
	
	private void writeRecords(BufferedReader inFile,RandomAccessFile outFile) throws IOException{
		int number;
		String location;
		StringBuilder builder;
		short cityIndex;
		NumberInfoCompress pre;
		
        String line=inFile.readLine();
        if(line==null)return;
        String[] st=line.trim().split(",");
        number=Integer.parseInt(st[0].trim());
        
        builder=new StringBuilder(st[1]);
        builder.setLength(14);
        
        location=builder.toString();
        
        cityIndex=getCityIndexByCityName(location);
        
        pre=new NumberInfoCompress(number,(short)0,cityIndex);
        
        line=inFile.readLine();
        while(line!=null){
            st=line.trim().split(",");
            number=Integer.parseInt(st[0].trim());
            
            builder=new StringBuilder(st[1]);
            builder.setLength(14);
            
            location=builder.toString();
            
            cityIndex=getCityIndexByCityName(location);
            
            if(number-(pre.getBegin()+pre.getSkip())==1
            	&&cityIndex==pre.getCityIndex()){
            	pre.setSkip((short) (number-pre.getBegin()));
            }else{
            	++phoneInfoCompressCount;
            	outFile.writeShort(pre.getM_before());
            	outFile.writeShort(pre.getM_after());
            	outFile.writeShort(pre.getM_cityIndex());
            	
            	pre=new NumberInfoCompress(number,(short)0,cityIndex);
            }
            line=inFile.readLine();
        }
        
        outFile.writeShort(pre.getM_before());
    	outFile.writeShort(pre.getM_after());
    	outFile.writeShort(pre.getM_cityIndex());
    	
    	++phoneInfoCompressCount;
	}
	
	private void writeCities(RandomAccessFile outFile) throws IOException{
        for(String st:cities){
        	outFile.writeChars(st);
        }
	}
	
	private short getCityIndexByCityName(String location){
		short index=0;
		int k;
		if(cities.indexOf(location)==-1){
			//不包含该元素
			p_total++;
		    cities.add(location);
		    k=cities.indexOf(location);
		}else{
			//包含该元素
			k=cities.indexOf(location);
		}
		index=(short) k;
		return index;
	}
}
