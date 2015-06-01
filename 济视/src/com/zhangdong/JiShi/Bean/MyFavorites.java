package com.zhangdong.JiShi.Bean;

public class MyFavorites {
	private String vTitle;
	private String vPreviewImageURL;
	private String vURL;
	private String VID;
	private String UFID;

	public String getUFID() {
		return UFID;
	}

	public void setUFID(String UFID) {
		this.UFID = UFID;
	}

	public MyFavorites(String vPreviewImageURL,String vTitle, String vURL,
			String VID,String UFID) {
		super();
		this.vPreviewImageURL = vPreviewImageURL;
		this.vTitle = vTitle;
		this.vURL = vURL;
		this.VID = VID;
		this.UFID = UFID;
	}


	public String getvTitle() {
		return vTitle;
	}


	public void setvTitle(String vTitle) {
		this.vTitle = vTitle;
	}


	public String getvPreviewImageURL() {
		return vPreviewImageURL;
	}


	public void setvPreviewImageURL(String vPreviewImageURL) {
		this.vPreviewImageURL = vPreviewImageURL;
	}


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


	public MyFavorites() {
		super();
	}

}
