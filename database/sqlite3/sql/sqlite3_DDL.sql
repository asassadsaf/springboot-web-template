-- ----------------------------
-- Table structure for storage_database_integrity
-- ----------------------------
DROP TABLE IF EXISTS "storage_database_integrity";
CREATE TABLE "storage_database_integrity"
(
    "id"            text(64) NOT NULL,
    "db_type"       text(32) NOT NULL,
    "db_ip"         text(32) NOT NULL,
    "db_port"       integer NOT NULL,
    "db_user"       text(64) NOT NULL,
    "db_pwd"        text(128) NOT NULL,
    "db_case"       text(64),
    "db_schema"     text(64),
    "db_table"      text(64) NOT NULL,
    "db_sql"        text    NOT NULL,
    "start_limit"   integer NOT NULL,
    "end_limit"     integer NOT NULL,
    "frequency"     integer NOT NULL,
    "key_id"        text(128) NOT NULL,
    "key_alg"       text(32) NOT NULL,
    "key_name"      text(128),
    "verify_status" integer,
    "hmac_hex"      text(255),
    "create_date"   datetime,
    "update_date"   datetime,
    "remark"        text(255),
    PRIMARY KEY ("id")
);

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

create table "sys_app"
(
    "id"          text(64)  not null,
    "name"        text(32)  not null,
    "age"         integer   null,
    "addr"        text(128) null,
    "create_date" datetime  null,
    PRIMARY KEY ("id")
);

create unique index "user_name_unique_index" on "sys_app" ("name" asc);
