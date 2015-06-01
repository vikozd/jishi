package com.zhangdong.JiShi.Bean;

public class MyHistory {
	private String UHID;
	private String vTitle;
//	private String vPreviewImageURL;
	private String vURL;
	private String VID;

	public String getUHID() {
		return UHID;
	}

	public void setUHID(String UHID) {
		this.UHID = UHID;
	}

	public MyHistory(String vTitle, String vURL,
			String VID,String UHID) {
		super();
//		this.vPreviewImageURL = vPreviewImageURL;
		this.vTitle = vTitle;
		this.vURL = vURL;
		this.VID = VID;
		this.UHID = UHID;
		
	}


	public String getvTitle() {
		return vTitle;
	}


	public void setvTitle(String vTitle) {
		this.vTitle = vTitle;
	}


//	public String getvPreviewImageURL() {
//		return vPreviewImageURL;
//	}
//
//
//	public void setvPreviewImageURL(String vPreviewImageURL) {
//		this.vPreviewImageURL = vPreviewImageURL;
//	}


	public String getvURL() {
		return vURL;
	}


	public void setvURL(String vURL) {
		this.vURL = vURL;
	}


	public String getVID() {
		return VID;
	}


	public void setVID(String vID) {
		VID = vID;
	}


	public MyHistory() {
		super();
	}

}
