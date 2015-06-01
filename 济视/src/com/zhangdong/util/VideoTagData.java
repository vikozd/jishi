package com.zhangdong.util;

public class VideoTagData {
	String vtDisplayTime;
	String vtTagText;
	String vURL;
	String VideoID;
	public String getVtDisplayTime() {
		return vtDisplayTime;
	}
	public void setVtDisplayTime(String vtDisplayTime) {
		this.vtDisplayTime = vtDisplayTime;
	}
	public String getVtTagText() {
		return vtTagText;
	}
	public void setVtTagText(String vtTagText) {
		this.vtTagText = vtTagText;
	}
	public String getvURL() {
		return vURL;
	}
	public void setvURL(String vURL) {
		this.vURL = vURL;
	}
	public String getVideoID() {
		return VideoID;
	}
	public void setVideoID(String videoID) {
		VideoID = videoID;
	}
	@Override
	public String toString() {
		return "VideoTagData [vtDisplayTime=" + vtDisplayTime + ", vtTagText="
				+ vtTagText + ", vURL=" + vURL + ", VideoID=" + VideoID + "]";
	}
	
	

}
