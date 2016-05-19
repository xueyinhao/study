package org.hao.weixin.quartz;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hao.weixin.json.AccessToken;
import org.hao.weixin.json.ErrorEntity;
import org.hao.weixin.model.WeixinContext;
import org.hao.weixin.model.WeixinFinalValue;
import org.hao.basic.util.JsonUtil;
import org.springframework.stereotype.Component;

@Component
public class RefreshAccessTokenTask {
	public static final String at = "";
	public void refreshToken() {
		System.out.println("------获取accessToken---------");
		WeixinContext.setAccessToken(at);
		HttpGet get = null;
		CloseableHttpResponse resp = null;
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			String url = WeixinFinalValue.ACCESS_TOKEN_URL;
			url = url.replaceAll("APPID", WeixinFinalValue.APPID);
			url = url.replaceAll("APPSECRET", WeixinFinalValue.APPSECRET);
			get = new HttpGet(url);
			resp = client.execute(get);
			int statusCode = resp.getStatusLine().getStatusCode();
			if(statusCode>=200&&statusCode<300) {
				HttpEntity entity = resp.getEntity();
				String content = EntityUtils.toString(entity);
				try {
					AccessToken at = (AccessToken)JsonUtil.getInstance().json2obj(content, AccessToken.class);
					WeixinContext.setAccessToken(at.getAccess_token());
					System.out.println("------获取accessToken成功---------"+WeixinContext.getAccessToken());
				} catch (Exception e) {
					ErrorEntity err = (ErrorEntity)JsonUtil.getInstance().json2obj(content, ErrorEntity.class);
					System.out.println("获取token异常:"+err.getErrcode()+","+err.getErrmsg());
					refreshToken();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(resp!=null) resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(client!=null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
