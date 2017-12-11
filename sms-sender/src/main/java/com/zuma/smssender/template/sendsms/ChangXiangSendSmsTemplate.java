package com.zuma.smssender.template.sendsms;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.changxiang.ChangXiangSendSmsAPI;
import com.zuma.smssender.enums.error.ChangXiangErrorEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.DateUtil;
import com.zuma.smssender.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * author:ZhengXing
 * datetime:2017/12/4 0004 15:49
 * 畅想 发送短信
 */
@Slf4j
@Component
public class ChangXiangSendSmsTemplate   extends SendSmsTemplate<ChangXiangSendSmsAPI.Request, ChangXiangSendSmsAPI.Response> {

	@Override
	ResultDTO<ErrorData> getResponse(CommonSmsAccount account, String phones, String smsMessae, SendSmsForm sendSmsForm, Long recordId) {
		//转为 请求对象
		ChangXiangSendSmsAPI.Request request = toRequestObject(account, phones, smsMessae);
		ChangXiangSendSmsAPI.Response response;
		//发送请求，并返回ZhangYouResponse对象
		try {
			response = sendHttpRequest(request, Config.CHANGXIANG_SEND_SMS_URL);
		} catch (SmsSenderException e) {
			//自定义异常捕获到,日志已经记录
			//返回异常返回对象
			return ResultDTO.error(e.getCode(), e.getMessage(), new ErrorData(phones, smsMessae));
		}
		//判断是否成功-根据api的response
		if (!response.getSuccess()) {
			//根据掌游异常码获取异常枚举
			ChangXiangErrorEnum errorEnum = EnumUtil.getByCode(response.getCode(), ChangXiangErrorEnum.class);
			return ResultDTO.error(errorEnum, new ErrorData(phones, smsMessae));
		}

		//流水号处理
		//构建缓存对象
		CommonCacheDTO cacheDTO = CommonCacheDTO.builder()
				.id(response.getCode())//流水号
				.platformId(sendSmsForm.getPlatformId())//平台id
				.timestamp(sendSmsForm.getTimestamp())//时间戳
				.phones(phones)//手机号
				.smsMessage(smsMessae)//短信消息
				.recordId(recordId)//该次发送记录数据库id
				.build();
		//存入缓存,key使用 掌游前缀 + 流水号
		CacheUtil.put(Config.KUANXIN_PRE + cacheDTO.getId(), cacheDTO);

		//成功
		return ResultDTO.success(new ErrorData());
	}

	@Override
	ChangXiangSendSmsAPI.Request toRequestObject(CommonSmsAccount account, String phones, String smsMessage) {
		//时间戳
		String dateStr = DateUtil.dateToString(new Date(),DateUtil.FORMAT_A);
		//签名
		String sign = CodeUtil.stringToMd5(CodeUtil.stringToMd5(account.getBKey()) + dateStr);
		return ChangXiangSendSmsAPI.Request.builder()
				.name(account.getAKey())
				.seed(dateStr)
				.key(sign)
				.dest(phones)
				.content(smsMessage)
				.build();
	}



	@Override
	ChangXiangSendSmsAPI.Response stringToResponseObject(String result) {
		try {
			//根据| 分割，获取[0]code 和[1]流水号
			String[] temp = StringUtils.split(result, ":");
			return ChangXiangSendSmsAPI.Response.builder()
					.success(temp[0].equalsIgnoreCase(ChangXiangErrorEnum.SUCCESS.getCode()))
					.code(temp[1])
					.build();
		} catch (Exception e) {
			log.error("【群正sendSms】返回的string转为response对象失败.resultString={},error={}", result, e.getMessage(), e);
			throw new SmsSenderException(ErrorEnum.STRING_TO_RESPONSE_ERROR);
		}
	}
}
