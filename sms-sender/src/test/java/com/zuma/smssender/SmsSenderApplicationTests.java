package com.zuma.smssender;

import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.channels.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SmsSenderApplicationTests {
	private static HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();

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
