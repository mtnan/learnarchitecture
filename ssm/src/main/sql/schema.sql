CREATE TABLE seckill (
  `seckill_id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NOT NULL ,
  `number` INT NOT NULL ,
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `start_time` TIMESTAMP NOT NULL ,
  `end_time` TIMESTAMP NOT NULL ,
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
);

INSERT INTO seckill(name, number, start_time, end_time)
VALUES
  ('iphone', 10, '2017-03-30 00:00:00', '2017-03-31 00:00:00'),
  ('ipad', 20, '2017-03-30 00:00:00', '2017-03-31 00:00:00'),
  ('xiaomi', 30, '2017-03-30 00:00:00', '2017-03-31 00:00:00'),
  ('奔驰', 5, '2017-03-30 00:00:00', '2017-03-31 00:00:00');

CREATE TABLE success_killed (
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀商品id',
  `user_phone` BIGINT NOT NULL ,
  `state` TINYINT NOT NULL DEFAULT -1,
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  KEY idx_create_time(create_time)
) COMMENT '秒杀成功表';

