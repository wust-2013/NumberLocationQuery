package com.location.query.bean;

/**
 * 
 * 封装号码归属地的数据bean类
 * 
 * @author wust-2013
 *
 */
public class NumberInfoCompress {
	
	private	short m_before;//store the 5 digit of the number,example: it store 136700 of 1367002
		
	private	short m_after;//store skip and last two digit of the number,
	                        //example:102,means 1 is the skip,02 is the last two digit of the number(1367002)
	private	short  m_cityIndex;
	
	public NumberInfoCompress(){
	   m_before=0;
	   m_after=0;
	   m_cityIndex=0;
	}
	
	public NumberInfoCompress(int begin,short skip,short cityIndex){
	   setBegin(begin);
	   setSkip(skip);
	   m_cityIndex = cityIndex;
	}
		
	public short getM_before() {
		return m_before;
	}

	public short getM_after() {
		return m_after;
	}

	public short getM_cityIndex() {
		return m_cityIndex;
	}

	public void setM_before(short mBefore) {
		m_before = mBefore;
	}

	public void setM_after(short mAfter) {
		m_after = mAfter;
	}

	public void setM_cityIndex(short mCityIndex) {
		m_cityIndex = mCityIndex;
	}

	public int getBegin(){
	   int lastTwoNumInAfter = m_after - getNumExceptLastTwo() * 100;
	   return m_before * 100 + lastTwoNumInAfter; 
	}
	
	public short getNumExceptLastTwo(){return (short) (m_after * 0.01);}
	
    public short getSkip(){ return getNumExceptLastTwo(); }
    
    public short getCityIndex(){ return m_cityIndex; }
    
	public short getLastTwoNum(int number){
	   int exceptLastTwoNum = (int) (number * 0.01);
	   return (short) (number - exceptLastTwoNum * 100);
	}
	
	public void setBegin(int number){
	   int lastTwoNum = getLastTwoNum(number);
	   m_before = (short) (number * 0.01);
	   m_after = (short) (getSkip() * 100 + lastTwoNum);
	}
	
	public void setSkip(short skip){
	   m_after =(short) (skip * 100 + getLastTwoNum(m_after));
	}
	public void setCityIndex(short city){m_cityIndex = city;}
	
}
