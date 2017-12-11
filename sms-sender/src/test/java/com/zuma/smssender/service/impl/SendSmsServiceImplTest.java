package com.zuma.smssender.service.impl;

import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.service.SendSmsService;
import com.zuma.smssender.util.CodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * author:ZhengXing
 * datetime:2017/12/4 0004 16:23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendSmsServiceImplTest {

	@Autowired
	private SendSmsService sendSmsService;
	@Test
	public void test() {
		String phone = "17826824998";
		SendSmsForm smsForm = new SendSmsForm().builder()
				.platformId(1000L)
				.channel(4)
				.phone(phone)
				.smsMessage("【口袋铃声】test123")
				.sign(CodeUtil.stringToMd5("a" + phone + "111111"))
				.timestamp(111111L)
				.build();
		ResultDTO resultDTO = sendSmsService.sendSms(smsForm);
		System.out.println(resultDTO);
	}

	public static void main(String[] args) {
		String sign = CodeUtil.stringToMd5(CodeUtil.stringToMd5("123456") + "20130806102030");
		System.out.println(sign);
	}
}