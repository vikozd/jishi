package com.zhangdong.JiShi.Bean;

public class BodyPart {
    private String VCID;
	private String vcName;
	private String vcImageURL;
	
	
	public BodyPart(){
		super();
	}
	
	public BodyPart(String VCID,String vcName,String vcImageURL){
		super();
		this.VCID=VCID;
		this.vcName=vcName;
		this.vcImageURL=vcImageURL;
	}
	
	public String getVCID() {
		return VCID;
	}

	public void setVCID(String vCID) {
		VCID = vCID;
	}

	public void setvcName(String vcName) {
		this.vcName=vcName;
	}
	
	public String getvcName() {
		return vcName;
	}
	
	public void setvcImageURL(String vcImageURL) {
		this.vcImageURL=vcImageURL;
	}
	
	public String getvcImageURL() {
		return vcImageURL;
	}
}
