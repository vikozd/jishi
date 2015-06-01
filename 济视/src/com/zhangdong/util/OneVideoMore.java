package com.zhangdong.util;

public class OneVideoMore {
	
	String IsFavorite;
	String CounterPlay;
	String CounterGood;
    
	public String getIsFavorite() {
		return IsFavorite;
	}
	public void setIsFavorite(String isFavorite) {
		IsFavorite = isFavorite;
	}
	public String getCounterPlay() {
		return CounterPlay;
	}
	public void setCounterPlay(String counterPlay) {
		CounterPlay = counterPlay;
	}
	public String getCounterGood() {
		return CounterGood;
	}
	public void setCounterGood(String counterGood) {
		CounterGood = counterGood;
	}
	@Override
	public String toString() {
		return "OneVideoMore [IsFavoritel=" + IsFavorite + ", CounterPlay="
				+ CounterPlay + ", CounterGood=" + CounterGood + "]";
	}
	
  
}
