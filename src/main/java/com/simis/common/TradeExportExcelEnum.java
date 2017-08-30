package com.simis.common;

public enum TradeExportExcelEnum {
    // --------------------------------单一数据申请----------------------------------
    /**
     * 客户信息
     */
    CUSTOMER("customer", "客户信息", "customer.xml", TradeExportExcelBeanName.CUSTOMER_DAO);

    private String code;

    private String name;

    private String xmlName;

    private String beanName;

    private TradeExportExcelEnum(String code, String name, String xmlName, String beanName) {
        this.code = code;
        this.name = name;
        this.xmlName = xmlName;
        this.beanName = beanName;
    }

    public static TradeExportExcelEnum createTradeExportExcelEnum(String code) {
        TradeExportExcelEnum result = null;
        TradeExportExcelEnum[] values = TradeExportExcelEnum.values();
        for (TradeExportExcelEnum exportNum : values) {
            if (exportNum.getCode().equalsIgnoreCase(code)) {
                result = exportNum;
                break;
            }
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
