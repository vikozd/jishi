package com.zhangdong.JiShi.Bean;

public class MyFavorites {
	private String vTitle;
	private String vPreviewImageURL;
	private String vURL;
	private String VID;
	private String UFID;

	private String vDoctorName;
	private String vDoctorDepartment;
	private String vDoctorHospital;
	
	public String getUFID() {
		return UFID;
	}

	
	public String getvDoctorName() {
		return vDoctorName;
	}


	public void setvDoctorName(String vDoctorName) {
		this.vDoctorName = vDoctorName;
	}


	public String getvDoctorDepartment() {
		return vDoctorDepartment;
	}


	public void setvDoctorDepartment(String vDoctorDepartment) {
		this.vDoctorDepartment = vDoctorDepartment;
	}


	public String getvDoctorHospital() {
		return vDoctorHospital;
	}


	public void setvDoctorHospital(String vDoctorHospital) {
		this.vDoctorHospital = vDoctorHospital;
	}


	public void setUFID(String UFID) {
		this.UFID = UFID;
	}

	public MyFavorites(String vPreviewImageURL,String vTitle, String vURL,
			String VID,String UFID,String vDoctorName,String vDoctorDepartment,String vDoctorHospital) {
		super();
		this.vPreviewImageURL = vPreviewImageURL;
		this.vTitle = vTitle;
		this.vURL = vURL;
		this.VID = VID;
		this.UFID = UFID;
		this.vDoctorName=vDoctorName;
		this.vDoctorDepartment=vDoctorDepartment;
		this.vDoctorHospital=vDoctorHospital;
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
