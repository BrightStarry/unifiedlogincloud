package com.zuma.smssender.dto.changxiang;

import lombok.*;

/**
 * author:ZhengXing
 * datetime:2017/12/4 0004 15:38
 * 畅想 状态报告 api
 */
public interface ChangXiangSendSmsResponse {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	class Request {
		private String name;//帐号
		private String report;//状态报告

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = false)
	class Response{
		private String status;//状态 返回 success 或 error:客户端自定义错误
	}

}
