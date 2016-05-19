package org.hao.weixin.model;

public class WeixinContext {
	private static String accessToken = "t7HJub_5YrUc7neOhWeqs-HlSasELO7hNsMnTpDcDuwwHVTFEEac1xgfPMUOrt19pj89B3yq2c2OhFalcRdpYGFWQfYEcFphcjVo4Liwb4pUUcyC4zXqjV-eCNPcse-3RGJiAAAETW";
	
	public static void setAccessToken(String accessToken) {
		WeixinContext.accessToken = accessToken;
	}
	
	public static String getAccessToken() {
		return WeixinContext.accessToken;
	}
}
