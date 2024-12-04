DROP TABLE IF EXISTS storage_database_integrity;
CREATE TABLE storage_database_integrity (
                                            id varchar(64) COLLATE pg_catalog.default NOT NULL,
                                            db_type varchar(32) COLLATE pg_catalog.default NOT NULL,
                                            db_ip varchar(32) COLLATE pg_catalog.default NOT NULL,
                                            db_port int4 NOT NULL,
                                            db_user varchar(64) COLLATE pg_catalog.default NOT NULL,
                                            db_pwd varchar(128) COLLATE pg_catalog.default NOT NULL,
                                            db_case varchar(64) COLLATE pg_catalog.default,
                                            db_schema varchar(64) COLLATE pg_catalog.default,
                                            db_table varchar(64) COLLATE pg_catalog.default NOT NULL,
                                            db_sql text COLLATE pg_catalog.default NOT NULL,
                                            start_limit int4 NOT NULL,
                                            end_limit int4 NOT NULL,
                                            frequency int4 NOT NULL,
                                            key_id varchar(128) COLLATE pg_catalog.default NOT NULL,
                                            key_alg varchar(32) COLLATE pg_catalog.default NOT NULL,
                                            key_name varchar(128) COLLATE pg_catalog.default,
                                            verify_status int2,
                                            hmac_hex varchar(255) COLLATE pg_catalog.default,
                                            create_time timestamp(6),
                                            update_time timestamp(6),
                                            remark varchar(255) COLLATE pg_catalog.default
);
ALTER TABLE storage_database_integrity ADD CONSTRAINT storage_database_integrity_pkey PRIMARY KEY (id);
COMMENT ON COLUMN storage_database_integrity.id IS '主键id';
COMMENT ON COLUMN storage_database_integrity.db_type IS '数据库类型：MYSQL、DM';
COMMENT ON COLUMN storage_database_integrity.db_ip IS '数据库ip';
COMMENT ON COLUMN storage_database_integrity.db_port IS '数据库端口';
COMMENT ON COLUMN storage_database_integrity.db_user IS '数据库用户名';
COMMENT ON COLUMN storage_database_integrity.db_pwd IS '数据库密码';
COMMENT ON COLUMN storage_database_integrity.db_case IS '数据库实例名，没有为空';
COMMENT ON COLUMN storage_database_integrity.db_schema IS '数据库模式名，没有为空';
COMMENT ON COLUMN storage_database_integrity.db_table IS '数据库表名';
COMMENT ON COLUMN storage_database_integrity.db_sql IS '执行查询sql语句';
COMMENT ON COLUMN storage_database_integrity.start_limit IS '数据起始位置，从1开始';
COMMENT ON COLUMN storage_database_integrity.end_limit IS '查询结束位置，最大10000';
COMMENT ON COLUMN storage_database_integrity.frequency IS '定时校验频率，单位：分钟';
COMMENT ON COLUMN storage_database_integrity.key_id IS '密钥id';
COMMENT ON COLUMN storage_database_integrity.key_alg IS '密钥算法';
COMMENT ON COLUMN storage_database_integrity.key_name IS '密钥名称';
COMMENT ON COLUMN storage_database_integrity.verify_status IS '校验状态，0：未校验，1：成功，2：失败，3：异常';
COMMENT ON COLUMN storage_database_integrity.hmac_hex IS '数据HMAC值的十六进制编码';
COMMENT ON COLUMN storage_database_integrity.create_time IS '创建时间';
COMMENT ON COLUMN storage_database_integrity.update_time IS '更新时间';
COMMENT ON COLUMN storage_database_integrity.remark IS '备注';

drop table if exists sys_app;
create table sys_app
(
    id          varchar(64) COLLATE pg_catalog.default not null,
    name        varchar(32) COLLATE pg_catalog.default not null,
    age         int4,
    addr        varchar(128) COLLATE pg_catalog.default,
    create_date timestamp(6)
);
alter table sys_app add constraint sys_app_pkey primary key (id);
create unique index user_name_unique_index on sys_app (name);

