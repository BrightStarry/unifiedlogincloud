package com.zuma.smssender;

import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
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

	@Test
	public void textSendXml() throws IOException {
		HttpPost post = new HttpPost("http://127.0.0.1/callback/qunzheng/sendsms");
		List<BasicNameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<result>\n" +
				"<response>2</response> \n" +
				"<sms>\n" +
				"<phone>13601655231</phone> \n" +
				"<pno>2013052523410511622e47b16c140f</pno> \n" +
				"<state>11</state>\n" +
				"<description>手机黑名单</description>\n" +
				"</sms>\n" +
				"<sms>\n" +
				"<phone>1386612345678</phone> \n" +
				"<pno>2013052523410511622e47b16c140f</pno> \n" +
				"<state>10</state>\n" +
				"</sms>\n" +
				"<sms>\n" +
				"<phone>18676767676</phone> \n" +
				"<pno>20130530225527701599f4c9c82827</pno> \n" +
				"<state>10</state>\n" +
				"<description>成功</description>\n" +
				"</sms>\n" +
				"</result>\n"));
		post.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
		HttpResponse response = httpClientUtil.getHttpClient().execute(post);
	}

	@Test
	public void test() {
		String url = "http://127.0.0.1/sendSms";

		Integer channel = null;
		String phone = "17826824998";
		String smsMessage = "测试";
		Long platformId = 1000L;
		long timestamp = System.currentTimeMillis();


		SendSmsForm param = SendSmsForm.builder()
				.channel(channel)
				.phone(phone)
				.platformId(platformId)
				.timestamp(timestamp)
				.sign(CodeUtil.stringToMd5(platformId + phone + timestamp))
				.smsMessage(smsMessage)
				.build();
		log.info("参数:{}", param);
		String result = httpClientUtil.doPostForString(url, param);
		log.info("执行结果:{}",result);
	}



}
