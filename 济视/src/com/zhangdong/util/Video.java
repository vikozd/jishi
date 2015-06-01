package com.zhangdong.util;

public class Video {
	

	String VID;
	String vTitle;
	String vDescription;
	String vURL;
	String vPreviewImageURL;
	String VTID;
	String vDoctorName;
	String vDoctorHospital;
    String vDoctorDepartment;
    String Counter;
	public String getVID() {
		return VID;
	}
	public void setVID(String vID) {
		VID = vID;
	}
	public String getvTitle() {
		return vTitle;
	}
	public void setvTitle(String vTitle) {
		this.vTitle = vTitle;
	}
	public String getvDescription() {
		return vDescription;
	}
	public void setvDescription(String vDescription) {
		this.vDescription = vDescription;
	}
	public String getvURL() {
		return vURL;
	}
	public void setvURL(String vURL) {
		this.vURL = vURL;
	}
	public String getvPreviewImageURL() {
		return vPreviewImageURL;
	}
	public void setvPreviewImageURL(String vPreviewImageURL) {
		this.vPreviewImageURL = vPreviewImageURL;
	}
	public String getVTID() {
		return VTID;
	}
	public void setVTID(String vTID) {
		VTID = vTID;
	}
	public String getvDoctorName() {
		return vDoctorName;
	}
	public void setvDoctorName(String vDoctorName) {
		this.vDoctorName = vDoctorName;
	}
	public String getvDoctorHospital() {
		return vDoctorHospital;
	}
	public void setvDoctorHospital(String vDoctorHospital) {
		this.vDoctorHospital = vDoctorHospital;
	}
	public String getvDoctorDepartment() {
		return vDoctorDepartment;
	}
	public void setvDoctorDepartment(String vDoctorDepartment) {
		this.vDoctorDepartment = vDoctorDepartment;
	}
	public String getCounter() {
		return Counter;
	}
	public void setCounter(String counter) {
		Counter = counter;
	}
	@Override
	public String toString() {
		return "Video [VID=" + VID + ", vTitle=" + vTitle + ", vDescription="
				+ vDescription + ", vURL=" + vURL + ", vPreviewImageURL="
				+ vPreviewImageURL + ", VTID=" + VTID + ", vDoctorName="
				+ vDoctorName + ", vDoctorHospital=" + vDoctorHospital
				+ ", vDoctorDepartment=" + vDoctorDepartment + ", Counter="
				+ Counter + "]";
	}
    
	

	

}
