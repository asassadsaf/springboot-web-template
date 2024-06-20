-- ----------------------------
-- Table structure for storage_database_integrity
-- ----------------------------
DROP TABLE IF EXISTS "storage_database_integrity";
CREATE TABLE "storage_database_integrity" (
  "id" text(64) NOT NULL,
  "db_type" text(32) NOT NULL,
  "db_ip" text(32) NOT NULL,
  "db_port" integer NOT NULL,
  "db_user" text(64) NOT NULL,
  "db_pwd" text(128) NOT NULL,
  "db_case" text(64),
  "db_schema" text(64),
  "db_table" text(64) NOT NULL,
  "db_sql" text NOT NULL,
  "start_limit" integer NOT NULL,
  "end_limit" integer NOT NULL,
  "frequency" integer NOT NULL,
  "key_id" text(128) NOT NULL,
  "key_alg" text(32) NOT NULL,
  "key_name" text(128),
  "verify_status" integer,
  "hmac_hex" text(255),
  "create_time" text,
  "update_time" text,
  "remark" text(255),
  PRIMARY KEY ("id")
);

