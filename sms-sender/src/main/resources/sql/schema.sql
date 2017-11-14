/* 平台信息表 */
CREATE TABLE platform(
  id BIGINT AUTO_INCREMENT COMMENT '平台id',
  name VARCHAR(16) NOT NULL COMMENT '平台名字',
  token VARCHAR(128) DEFAULT '' COMMENT '令牌',
  callback_url VARCHAR(128) DEFAULT '' COMMENT '回调url',
  status TINYINT DEFAULT 1 COMMENT '状态： 0：停止授权；1：启用授权',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_token(token),
  KEY idx_name(name)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '平台信息表';

/*短信上行记录表*/
CREATE TABLE sms_up_record(
  id BIGINT AUTO_INCREMENT COMMENT '记录id',
  channel_id INT DEFAULT 0 COMMENT '通道id',
  channel_name VARCHAR(16) DEFAULT '' COMMENT '通道名',
  phone CHAR(11)  DEFAULT '' COMMENT '手机号',
  content VARCHAR(255) DEFAULT '' COMMENT '短信内容',
  request_body VARCHAR(512) DEFAULT '' COMMENT '请求对象json字符',
  up_time DATETIME  COMMENT '上行时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_phone(phone),
  KEY idx_channel_id(channel_id)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '短信上行记录表';

/*短信发送记录表*/
CREATE TABLE sms_send_record(
  id BIGINT AUTO_INCREMENT COMMENT '记录id',
  platform_id BIGINT DEFAULT 0 COMMENT '平台id',
  paltform_name VARCHAR(16) DEFAULT '' COMMENT '平台名',
  request_body VARCHAR(1024) DEFAULT '' COMMENT '调用者请求对象json字符',
  isSuccess TINYINT DEFAULT 0 COMMENT '是否成功， 0：否（失败），1是（成功）',
  result_body VARCHAR(1024)DEFAULT '' COMMENT '返回对象json字符',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',
  PRIMARY KEY (id)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '短信发送记录表';