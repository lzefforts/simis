package com.simis.common;

/**
 * 费用枚举
 * Created by ArnoldLee on 17/4/30.
 */
public enum FeeEnum {

    //只有考试费
    EXAM_FEE_ONLY("BJGBYSPXZX0001","北京人民广播电台语言文字测试中心考试费用","0001"),

    //考试费+材料费
    EXAM_FEE_AND_BOOK_FEE("BJGBYSPXZX0002","北京人民广播电台语言文字测试中心考试和材料费用","0002"),

    //考试费+视频费
    EXAM_FEE_AND_VIDEO_FEE("BJGBYSPXZX0003","北京人民广播电台语言文字测试中心考试和视频费用","0003"),

    //考试费+材料费+食品费
    EXAM_FEE_AND_BOOK_FEE_AND_VIDEO_FEE("BJGBYSPXZX0004","北京人民广播电台语言文字测试中心考试和材料和视频费用","0004");

    //产品id
    private String productId;
    //描述
    private String description;
    //code
    private String code;

    private FeeEnum(String productId, String description,String code) {
        this.productId = productId;
        this.description = description;
        this.code = code;
    }

    /**
     * 根据指定的code的值构建一个枚举
     *
     * @param code
     *            code值
     * @return
     */
    public static FeeEnum createFeeEnum(String code) {
        FeeEnum type = null;
        FeeEnum[] objectTypes = FeeEnum.values();
        for (FeeEnum p : objectTypes) {
            if (p.getCode().equals(code)) {
                type = p;
                break;
            }
        }
        return type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
