package com.zuma.smssender.dto.changxiang;

import lombok.*;

/**
 * author:ZhengXing
 * datetime:2017/12/4 0004 15:28
 * 畅想发送短信api
 */
public interface ChangXiangSendSmsAPI {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	class Request {
		private String name;//帐号
		private String seed;//当前时间
		private String key;//md5(md5(password)+seed))
		private String dest;//手机号
		private String content;//短信内容
		private String reference;//参考信息
		private String delay;//定时参数
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	class Response{
		private Boolean success;//是否成功
		private String code;//成功消息编号 或 错误码
	}
}
