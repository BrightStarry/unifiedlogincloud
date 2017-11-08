/* 平台信息表 */
CREATE TABLE platform(
  id BIGINT AUTO_INCREMENT COMMENT '平台id',
  name VARCHAR(16) NOT NULL COMMENT '平台名字',
  token VARCHAR(128) DEFAULT '' COMMENT '令牌',
  status TINYINT DEFAULT 1 COMMENT '状态： 0：停止授权；1：启用授权',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_token(token),
  KEY idx_name(name)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '平台信息表';