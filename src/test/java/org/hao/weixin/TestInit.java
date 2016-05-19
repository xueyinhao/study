package org.hao.weixin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hao.basic.util.JsonUtil;
import org.hao.weixin.json.AccessToken;
import org.hao.weixin.kit.SecurityKit;
import org.hao.weixin.media.MediaKit;
import org.hao.weixin.model.WeixinContext;
import org.hao.weixin.model.WeixinFinalValue;
import org.hao.weixin.model.WeixinMenu;
import org.hao.weixin.msg.MessageKit;
import org.hao.weixin.quartz.RefreshAccessTokenTask;
import org.junit.Test;

public class TestInit {

	@Test
	public void testSha1() {
		// SecurityKit.sha1("hello");
		// byte a = 3;
		// System.out.println(String.format("%02x", a));
		String sha1 = SecurityKit.sha1("hello");
		System.out.println(sha1);
	}
	
	@Test
	public void testHttpClient() {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			String url = WeixinFinalValue.ACCESS_TOKEN_URL;
			url = url.replaceAll("APPID", WeixinFinalValue.APPID);
			url = url.replaceAll("APPSECRET", WeixinFinalValue.APPSECRET);
			System.out.println(url);
			HttpGet get = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(get);
			int statusCode = resp.getStatusLine().getStatusCode();
			if(statusCode>=200&&statusCode<300) {
				HttpEntity entity = resp.getEntity();
				String content = EntityUtils.toString(entity);
				AccessToken at = (AccessToken)JsonUtil.getInstance().json2obj(content, AccessToken.class);
				System.out.println(at.getAccess_token()+","+at.getExpires_in());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMenu() {
		try {
			List<WeixinMenu> wms = new ArrayList<WeixinMenu>();
			WeixinMenu wm1 = new WeixinMenu();
			wm1.setId(1);
			wm1.setName("网站");
			wm1.setType("view");
			wm1.setUrl("https://www.qidijin.com");
			wms.add(wm1);
			
			WeixinMenu wm2 = new WeixinMenu();
			wm2.setName("测试资源");
			List<WeixinMenu> wm2Sub = new ArrayList<WeixinMenu>();
			wm2.setSub_button(wm2Sub);
			//加入2菜单
			wms.add(wm2);
			
			wm1 = new WeixinMenu();
			wm1.setId(21);
			wm1.setName("事件测试");
			wm1.setType("click");
			wm1.setKey("B0001");
			wm2Sub.add(wm1);
			
			wm1 = new WeixinMenu();
			wm1.setId(22);
			wm1.setName("扫码推事件");
			wm1.setType("scancode_push");
			wm1.setKey("B0002");
			wm2Sub.add(wm1);
			
			wm1 = new WeixinMenu();
			wm1.setId(23);
			wm1.setName("扫码推事件且弹出提示框");
			wm1.setType("scancode_waitmsg");
			wm1.setKey("B0003");
			wm2Sub.add(wm1);
			
			wm1 = new WeixinMenu();
			wm1.setId(24);
			wm1.setName("弹出系统拍照发图");
			wm1.setType("pic_sysphoto");
			wm1.setKey("B0004");
			wm2Sub.add(wm1);
			
			wm1 = new WeixinMenu();
			wm1.setId(25);
			wm1.setName("弹出拍照或者相册发图");
			wm1.setType("pic_photo_or_album");
			wm1.setKey("B0005");
			wm2Sub.add(wm1);
			
			
			WeixinMenu wm3 = new WeixinMenu();
			wm3.setName("测试资源3");
			List<WeixinMenu> wm3Sub = new ArrayList<WeixinMenu>();
			wm3.setSub_button(wm3Sub);
			//加入3菜单
			wms.add(wm3);
			
			wm1 = new WeixinMenu();
			wm1.setId(31);
			wm1.setName("弹出微信相册发图器");
			wm1.setType("pic_weixin");
			wm1.setKey("C0001");
			wm3Sub.add(wm1);
			
			wm1 = new WeixinMenu();
			wm1.setId(32);
			wm1.setName("弹出地理位置选择器");
			wm1.setType("location_select");
			wm1.setKey("C0002");
			wm3Sub.add(wm1);
			
//			wm1 = new WeixinMenu();
//			wm1.setId(33);
//			wm1.setName("下发消息除文本消息");
//			wm1.setType("media_id");
//			wm1.setKey("C0003");
//			wm3Sub.add(wm1);
//			
//			wm1 = new WeixinMenu();
//			wm1.setId(34);
//			wm1.setName("跳转图文消息URL");
//			wm1.setType("view_limited");
//			wm1.setKey("C0004");
//			wm3Sub.add(wm1);
			
			
			
			Map<String,List<WeixinMenu>> maps = new HashMap<String,List<WeixinMenu>>();
			maps.put("button",wms);
			String json = JsonUtil.getInstance().obj2json(maps);
			
			CloseableHttpClient client = HttpClients.createDefault();
			String url = WeixinFinalValue.MENU_ADD;
			url = url.replace("ACCESS_TOKEN", WeixinContext.getAccessToken());
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type","application/json");
			StringEntity entity = new StringEntity(json, 
					ContentType.create("application/json", "utf-8"));
			post.setEntity(entity);
			CloseableHttpResponse resp = client.execute(post);
			int sc = resp.getStatusLine().getStatusCode();
			if(sc>=200&&sc<300) {
				System.out.println(EntityUtils.toString(resp.getEntity()));
			}
		} catch (UnsupportedCharsetException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMsg() {
		try {
			Map<String,String> maps = new HashMap<String, String>();
			maps.put("123", "abc");
			maps.put("bcd", "222");
			maps.put("bcd", "<abc>ddd</abc>");
			System.out.println(MessageKit.map2xml(maps));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPostMedia() {
		String mid = MediaKit.postMedia("d:/02.jpg", "image");
		System.out.println(mid);
	}
	
	@Test
	public void testGetMedia() {
		try {
			MediaKit.getMedia("_I53ClKoGvcQC4z1mWLf-O_nDJ_rw2p-LtfJOslSONSzUEtv8eKEvlDbn8m71d9m",new File("f:/aaaa.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
