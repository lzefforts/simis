package com.simis.common;

/**
 * 导出类型枚举
 * Created by ArnoldLee on 17/4/30.
 */
public enum ExportTypeEnum {

    //已登记未交费
    REGISTER_BUT_NOTPAID("0","已登记未交费"),

    //导出已缴费未注册
    PAID_BUT_NOTREGISTER("1","导出已缴费未注册"),

    //全部数据
    ALL("2","全部数据"),

    //有邮寄地址的
    HAS_MAIL_ADDRESS("3","有邮寄地址的");

    //产品id
    private String code;
    //描述
    private String description;
    //code

    private ExportTypeEnum(String code,String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据指定的code的值构建一个枚举
     *
     * @param code
     *            code值
     * @return
     */
    public static ExportTypeEnum createExportTypeEnum(String code) {
        ExportTypeEnum type = null;
        ExportTypeEnum[] objectTypes = ExportTypeEnum.values();
        for (ExportTypeEnum p : objectTypes) {
            if (p.getCode().equals(code)) {
                type = p;
                break;
            }
        }
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSameCode(String code) {
        return this.code.equals(code);
    }

}
