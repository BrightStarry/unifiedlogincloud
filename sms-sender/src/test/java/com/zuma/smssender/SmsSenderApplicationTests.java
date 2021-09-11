package com.zuma.smssender;

import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.service.PlatformService;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SmsSenderApplicationTests {
	private static HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
	@Autowired
	private PlatformService platformService;


	@Test
	public void test() {
		String url = "http://127.0.0.1:8080/sendsms";

		Integer channel = null;
		String phone = "xxxxxx,xxxxxx,xxxxxx";
		String smsMessage = "测试1!&测试2!&测试3";
		Long platformId = 1000L;
		long timestamp = System.currentTimeMillis();

		String token = platformService.findOne(platformId).getToken();


		SendSmsForm param = SendSmsForm.builder()
				.channel(channel)
				.phone(phone)
				.platformId(platformId)
				.timestamp(timestamp)
				.sign(CodeUtil.stringToMd5(token + phone + timestamp))
				.smsMessage(smsMessage)
				.build();
		log.info("参数:{}", param);
		String result = httpClientUtil.doPostForString(url, param);
		log.info("执行结果:{}",result);
	}

	public static void main(String[] args) {
		String s = CodeUtil.stringToMd5("a" + "xxxxxx,xxxxxx,xxxxxx" + "111111");
		System.out.println(s);

//		byte[] bytes = CodeUtil.intToByte4(-2147483648);
//		for (byte aByte : bytes) {
//			System.out.println(aByte);
//		}

	}


}
