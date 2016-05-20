package org.hao.weixin.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hao.weixin.kit.SecurityKit;
import org.hao.weixin.model.WeixinContext;
import org.hao.weixin.msg.MessageKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WeixinController {
	
	public static final String TOKEN = "hao_weixin_token";
	//测试修改

	@RequestMapping(value="/wget",method=RequestMethod.GET)
	public void init(HttpServletRequest req,HttpServletResponse resp) throws IOException {
//		signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//		timestamp	时间戳
//		nonce	随机数
//		echostr
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		String[] arrs = {WeixinController.TOKEN,nonce,timestamp};
		Arrays.sort(arrs);
		StringBuffer sb = new StringBuffer();
		for(String a:arrs) {
			System.out.println(a);
			sb.append(a);
		}
		String sha1 = SecurityKit.sha1(sb.toString());
		System.out.println(sha1.equals(signature));
		if(sha1.equals(signature)) {
			resp.getWriter().println(echostr);
		}
	}
	
	@RequestMapping(value="/wget",method=RequestMethod.POST)
	public void getInfo(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		Map<String,String> msgMap = MessageKit.reqMsg2Map(req);
		System.out.println(msgMap);
		String respCon = MessageKit.handlerMsg(msgMap);
		resp.setContentType("application/xml;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		System.out.println(respCon);
		System.out.println("sssss-----"+req.getSession().getId());
		
		resp.getWriter().write(respCon);
	}
	
	
	@RequestMapping("/at")
	public void testAccessToken(HttpServletResponse resp) throws IOException {
		resp.getWriter().println(WeixinContext.getAccessToken());
	}
}
