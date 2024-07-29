DROP TABLE IF EXISTS `storage_database_integrity`;
CREATE TABLE `storage_database_integrity`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主键id',
    `db_type`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '数据库类型：MYSQL、DM',
    `db_ip`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '数据库ip',
    `db_port`       int(11) NOT NULL COMMENT '数据库端口',
    `db_user`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '数据库用户名',
    `db_pwd`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库密码',
    `db_case`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库实例名，没有为空',
    `db_schema`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库模式名，没有为空',
    `db_table`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '数据库表名',
    `db_sql`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行查询sql语句',
    `start_limit`   int(11) NOT NULL COMMENT '数据起始位置，从1开始',
    `end_limit`     int(11) NOT NULL COMMENT '查询结束位置，最大10000',
    `frequency`     int(11) NOT NULL COMMENT '定时校验频率，单位：分钟',
    `key_id`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密钥id',
    `key_alg`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '密钥算法',
    `key_name`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密钥名称',
    `verify_status` tinyint(4) NULL DEFAULT NULL COMMENT '校验状态，0：未校验，1：成功，2：失败，3：异常',
    `hmac_hex`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据HMAC值的十六进制编码',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS "statistic_call_count";
CREATE TABLE "statistic_call_count"
(
    "id"             text(32) NOT NULL,
    "dimension"      text(64) NOT NULL,
    "success_num"    integer NOT NULL,
    "fail_num"       integer NOT NULL,
    "total_num"      integer NOT NULL,
    "tenant_account" text(64) NOT NULL,
    PRIMARY KEY ("id")
);
CREATE UNIQUE INDEX "dimension_unique" ON "statistic_call_count"
(
    "dimension" ASC,
    "tenant_account" ASC
);
