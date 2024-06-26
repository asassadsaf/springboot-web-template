package com.fkp.template.modules.xkip.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/6/13 9:48
 */
@Data
public class CertDigestListBean {
    private List<String> alias;

    public CertDigestListBean(List<CertDigestBean> list) {
        this.alias = new ArrayList<>();
        for (CertDigestBean certDigestBean : list) {
            certDigestBean.instance(0);
            alias.add(certDigestBean.getAlias());
        }
    }
}
