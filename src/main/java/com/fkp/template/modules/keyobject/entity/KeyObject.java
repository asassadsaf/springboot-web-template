package com.fkp.template.modules.keyobject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yaoxin
 * remarks 日期数据存放时间戳
 */
@Data
@TableName("key_object")
public class KeyObject {
    @TableId("ID")
    private String id;

    @TableField(exist = false)
    private int protectLevel = 1;

    @TableField("CURVE")
    private String curve;

    @TableField("GROUPFLAG")
    private Integer groupFlag = 0;

    @TableField("KEYBYTESTRING")
    private byte[] keyByteString;

    @TableField("KEYFORMATTYPE")
    private String keyFormatType;

    @TableField("KEYHASH")
    private byte[] keyHash;

    @TableField("KEYLENGTH")
    private Integer keyLength;

    @TableField("LDAPUSER")
    private String ldapUser;

    @TableField("QLENGTH")
    private Integer qLength;

    @TableField("S_ACTIVATIONDATE")
    private String sActivationDate;

    @TableField("S_ARCHIVEDATE")
    private String sArchiveDate;

    @TableField("S_COMPROMISEDATE")
    private String sCompromiseDate;

    @TableField("S_COMPROMISEOCCURRENCEDATE")
    private String sCompromiseOccurrenceDate;

    @TableField("S_CRYPTOGRAPHICALGORITHM")
    private String sCryptographicAlgorithm;

    @TableField("S_CRYPTOGRAPHICLENGTH")
    private Integer sCryptographicLength;

    @TableField("S_CRYPTOGRAPHICUSAGEMASK")
    private String sCryptographicUsageMask = "1023";

    @TableField("S_DEACTIVATIONDATE")
    private String sDeactivationDate = "";

    @TableField("S_DESTROYDATE")
    private String sDestroyDate;

    @TableField("S_EXTEND1")
    private String sExtend1;

    @TableField("S_EXTEND2")
    private String sExtend2;

    @TableField("S_EXTEND3")
    private String sExtend3;

    @TableField("S_EXTEND4")
    private String sExtend4;

    @TableField("S_EXTEND5")
    private String sExtend5;

    @TableField("S_EXTRACTABLE")
    private String sExtractable = "true";

    @TableField("S_INITIALDATE")
    private String sInitialDate;

    @TableField("S_KEYVERSION")
    private Integer sKeyVersion = 1;

    @TableField("S_LASTCHANGEDATE")
    private String sLastChangeDate = "";

    @TableField("S_NAMETYPE")
    private String sNameType;

    @TableField("S_NAMEVALUE")
    private String sNameValue = StringUtils.EMPTY;

    @TableField("S_OBJECTTYPE")
    private String sObjectType;

    @TableField("S_POLICYCOUNT")
    private Integer sPolicyCount;

    @TableField("S_PROCESSSTARTDATE")
    private String sProcessStartDate;

    @TableField("S_PROTECTSTOPDATE")
    private String sProtectStopDate = "";

    @TableField("S_STATE")
    private Integer sState;

    @TableField("S_UNIQUEIDENTIFIER")
    private String sUniqueIdentifier;

    @TableField("SHORTUNIQUEIDENTIFIER")
    private String shortUniqueIdentifier;

    @TableField("USERNAME")
    private String username;

    @TableField("TENANTAccount")
    private String tenantAccount;

    @TableField("S_FRESH")
    private Integer sFresh = 1;

    @TableField("CERTISSUER")
    private String certIssuer;

    @TableField("CERTSERIALNUMBER")
    private String certSerialNumber;

    @TableField("CERTSUBJECT")
    private String certSubject;

    @TableField("ARCHIVEFlag")
    private String archiveFlag = "N";

    @TableField("KeyStore")
    private String keyStore;

    @TableField(exist = false)
    private String sRotationStartTime = "";

    @TableField(exist = false)
    private String sRotationStatus = "0";

    @TableField(exist = false)
    private String sAlreadyRotate = "0";

    @TableField(exist = false)
    private String sDeleteAble = "1";

    @TableField(exist = false)
    private String keyMaterial;

    @TableField(exist = false)
    private String origin;

    @TableField("KeyUse")
    private String keyUse;

    @TableField("HID")
    private Integer hid;

    @TableField("ENC_TYPE")
    private String encType;

    @TableField("LMKNUMBER")
    private String lmkNumber;

    @TableField("LMK_PLAIN")
    private String lmkPlain = "true";

}
