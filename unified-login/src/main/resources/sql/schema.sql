/* 平台信息表 */
CREATE TABLE platform(
  id BIGINT AUTO_INCREMENT COMMENT '平台id',
  name VARCHAR(16) NOT NULL COMMENT '平台名字',
  token VARCHAR(128) DEFAULT '' COMMENT '令牌',
  status TINYINT DEFAULT 1 COMMENT '状态： 0：停止授权；1：启用授权',
  create_time TIMESTAMP NOT NULL DEFAULT 0 COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_token(token),
  KEY idx_name(name)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '平台信息表';

/*用户信息表*/
CREATE TABLE user(
  id BIGINT AUTO_INCREMENT COMMENT '用户id',
  username VARCHAR(32) NOT NULL COMMENT '用户名',
  password VARCHAR(32) NOT NULL COMMENT '用户密码',
  status TINYINT DEFAULT 1 COMMENT '状态： 0：停用；1：启用',
  create_time TIMESTAMP NOT NULL DEFAULT 0 COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_username(username)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '用户信息表';

/*用户-平台-关系表*/
CREATE TABLE user_platform_relation(
  id BIGINT AUTO_INCREMENT   COMMENT  'id',
  user_id BIGINT NOT NULL COMMENT '用户id',
  username VARCHAR(16) NOT NULL COMMENT '用户名',
  platform_id BIGINT NOT NULL COMMENT '平台id',
  platform_name VARCHAR(16) NOT NULL COMMENT '平台名',
  create_time TIMESTAMP NOT NULL DEFAULT 0 COMMENT '创建时间',
  update_time TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '修改时间',

  PRIMARY KEY(id),
  KEY idx_user_platform(user_id,platform_id)
)ENGINE = InnoDB AUTO_INCREMENT = 1000 COMMENT = '用户-平台-关系表';