package com.simis.dao.impl;

import com.simis.base.impl.BaseHibernateDaoImpl;
import com.simis.base.impl.BusiHibernateDaoImpl;
import com.simis.basedao.vo.QueryCondition;
import com.simis.common.ExportTypeEnum;
import com.simis.common.TradeExportExcelEnum;
import com.simis.dao.CustomerDao;
import com.simis.model.CustomerModel;
import com.simis.pay.wechat.common.MD5;
import com.simis.util.MD5Util;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 一拳超人 on 17/3/29.
 */
@Repository("customerDao")
public class CustomerDaoImpl extends BaseHibernateDaoImpl implements CustomerDao {

    @Override
    public CustomerModel queryByUserName(String userName) {
        StringBuilder hql = new StringBuilder("from CustomerModel where userName =:userName and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("userName",userName);
        return (CustomerModel)this.queryUniquneObject(hql.toString(),params);
    }

    @Override
    public QueryCondition exportExcel(Map<String, Object> conditions, TradeExportExcelEnum exportEnum, ExportTypeEnum exportTypeEnum,String exportTime) {
        QueryCondition query = new QueryCondition();
        if(!StringUtils.isEmpty(exportTime)){
            conditions.put("examTime",exportTime);
        }
        query = whereSql(conditions,query,exportTypeEnum);
        String sql = null;
        switch (exportTypeEnum){
            case REGISTER_BUT_NOTPAID:{//已登记已交费
                sql = createRegisterAndPaidCustomerListQuery(query);//createOnlyRegisterCustomerListQuery(query);
                break;
            }
            case PAID_BUT_NOTREGISTER:{//只交费未注册
                sql = createOnlyPaidCustomerListQuery(query);
                break;
            }
            case HAS_MAIL_ADDRESS:{//有邮寄地址且已经交费的
                sql = createHasMailAddressCustomerListQuery(query);
                break;
            }
            case ALL:{//全部
                sql = createCustomerListQuery(query);
                break;
            }
            default:{}
        }
        query.setFullSQL(sql.toString());
        return query;
    }

    private QueryCondition whereSql(Map<String, Object> conditions,QueryCondition query,ExportTypeEnum exportTypeEnum){
        StringBuilder whereSql = new StringBuilder("");
        whereSql.append(" where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();
        if(conditions!=null && !conditions.isEmpty()){
            if(conditions.get("batchNo")!=null && !conditions.get("batchNo").equals("")){
                whereSql.append(" and batch_no like :batchNo ");
                params.put("batchNo",conditions.get("batchNo"));
            }
            if(conditions.get("examTime")!=null && !conditions.get("examTime").equals("")){
                params.put("examTime",conditions.get("examTime"));
            }
        }
        switch (exportTypeEnum){
            case REGISTER_BUT_NOTPAID://已登记已交费
            case PAID_BUT_NOTREGISTER://只交费未注册
            case ALL:{//全部
                whereSql.append(" order by aa.cardNo,aa.type ");
                break;
            }
            case HAS_MAIL_ADDRESS:{//有邮寄地址且已经交费的
                whereSql.append(" order by aa.createTime asc ");
                break;
            }
            default:{}
        }
//        whereSql.append(" order by aa.create_time asc ");
//        whereSql.append(" group by aa.cardNo,aa.type ");
        if(whereSql.length()>0){
            query.setWhereSQL(whereSql.toString());
        }
        if(params!=null && !params.isEmpty()){
            query.setConditions(params);
        }
        return query;
    }

    //导出全部数据
    private String createCustomerListQuery(QueryCondition query){
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ( ");
        sql.append("select name,phone,email,card_no as cardNo,");
        sql.append("pay_date as payDate,exam_time as examTime,");
        sql.append("case when sex = 1 then '女' else '男' end as sex,");
        sql.append("case when is_already_paid = 0 then '否' when is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '0' type, ");
        sql.append(" sc.address, ");
        sql.append(" sc.create_time createTime, ");
        sql.append(" '正常注册数据' dataType");
        sql.append(" from sims_customer sc ");
        sql.append(" where sys_state = 1  ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and sc.exam_time = :examTime");
        }
        sql.append(" union all ");
        sql.append("select sci.name,sci.phone,sci.email,sci.card_no as cardNo,");
        sql.append("sci.pay_date as payDate,sci.exam_time as examTime,");
        sql.append("case when sci.sex = 1 then '女' else '男' end as sex,");
        sql.append("case when sci.is_already_paid = 0 then '否' when sci.is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '1' type, ");
        sql.append(" sci.address, ");
        sql.append(" null createTime, ");
        sql.append(" '外部导入数据' dataType");
        sql.append(" from sims_customer_import sci ");
        sql.append(" left join sims_customer scm on sci.card_no=scm.card_no and scm.sys_state = 1 ");
        sql.append(" where sci.sys_state = 1 ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and sci.exam_time = :examTime ");
        }
        sql.append(" ) aa ");
        sql.append(query.getWhereSQL());
        query.setFullSQL(sql.toString());
        return sql.toString();
    }

    //导出已在语言委员会注册且已交费的人
    private String createRegisterAndPaidCustomerListQuery(QueryCondition query){
        StringBuilder sql = new StringBuilder("");
//        sql.append("select * from ( ");
//        sql.append("select name,phone,email,card_no as cardNo,");
//        sql.append("pay_date as payDate,exam_time as examTime,");
//        sql.append("case when sex = 1 then '女' else '男' end as sex,");
//        sql.append("case when is_already_paid = 0 then '否' when is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
//        sql.append(" '0' type, ");
//        sql.append(" sc.address, ");
//        sql.append(" sc.create_time createTime, ");
//        sql.append(" '正常注册数据' dataType");
//        sql.append(" from sims_customer sc ");
//        sql.append(" where sys_state = 1  ");
//        sql.append(" and is_already_paid = 1 ");
//        sql.append(" ) aa ");

        sql.append("select * from ( ");
        sql.append("select sc.name,sc.phone,sc.email,sc.card_no as cardNo,");
        sql.append("sc.pay_date as payDate,sc.exam_time as examTime,");
        sql.append("case when sc.sex = 1 then '女' else '男' end as sex,");
        sql.append("case when sc.is_already_paid = 0 then '否' when sc.is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '0' type, ");
        sql.append(" sc.address, ");
        sql.append(" sc.create_time createTime, ");
        sql.append(" '正常注册数据' dataType");
        sql.append(" from sims_customer sc ");
        sql.append(" inner join sims_customer_import scim on scim.card_no=sc.card_no and scim.sys_state = 1 ");
        sql.append(" where sc.sys_state = 1  ");
        sql.append(" and sc.is_already_paid = 1 ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and sc.exam_time =  :examTime ");
        }
        sql.append(" union all ");
        sql.append("select sci.name,sci.phone,sci.email,sci.card_no as cardNo,");
        sql.append("sci.pay_date as payDate,sci.exam_time as examTime,");
        sql.append("case when sci.sex = 1 then '女' else '男' end as sex,");
        sql.append("case when sci.is_already_paid = 0 then '否' when sci.is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '1' type, ");
        sql.append(" sci.address, ");
        sql.append(" null createTime, ");
        sql.append(" '外部导入数据' dataType");
        sql.append(" from sims_customer_import sci ");
        sql.append(" inner join sims_customer scm on sci.card_no=scm.card_no and scm.is_already_paid = 1 and scm.sys_state = 1 ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and scm.exam_time = :examTime ");
        }
        sql.append(" where sci.sys_state = 1 ");
        sql.append(" ) aa ");
        sql.append(query.getWhereSQL());
        query.setFullSQL(sql.toString());
        return sql.toString();
    }



    //导出只登记(在语言委员会)未交费的人
    private String createOnlyRegisterCustomerListQuery(QueryCondition query){
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ( ");
        sql.append("select name,phone,email,card_no as cardNo,");
        sql.append("pay_date as payDate,exam_time as examTime,");
        sql.append("case when sex = 1 then '女' else '男' end as sex,");
        sql.append("case when is_already_paid = 0 then '否' when is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '0' type, ");
        sql.append(" address, ");
        sql.append(" create_time createTime, ");
        sql.append(" '正常注册数据' dataType");
        sql.append(" from sims_customer_import sc ");
        sql.append(" where sys_state = 1  ");
//        sql.append(" and is_already_paid = 0 ");
        sql.append(" and exists(select 1 from sims_customer sct where sct.card_no=sc.card_no and sct.is_already_paid = 0) ");
        sql.append(" ) aa ");
        sql.append(query.getWhereSQL());
        query.setFullSQL(sql.toString());
        return sql.toString();
    }

    //导出未注册只交费的人
    private String createOnlyPaidCustomerListQuery(QueryCondition query){
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ( ");
        sql.append("select name,phone,email,card_no as cardNo,");
        sql.append("pay_date as payDate,exam_time as examTime,");
        sql.append("case when sex = 1 then '女' else '男' end as sex,");
        sql.append("case when is_already_paid = 0 then '否' when is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '0' type, ");
        sql.append(" sc.address, ");
        sql.append(" sc.create_time createTime, ");
        sql.append(" '正常注册数据' dataType");
        sql.append(" from sims_customer sc ");
        sql.append(" where sys_state = 1  ");
        sql.append(" and is_already_paid = 1 ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and sc.exam_time =  :examTime ");
        }
        sql.append(" and not exists(select 1 from sims_customer_import sct where sct.card_no=sc.card_no) ");
        sql.append(" ) aa ");
        sql.append(query.getWhereSQL());
        query.setFullSQL(sql.toString());
        return sql.toString();
    }


    //导出有邮寄地址的人
    private String createHasMailAddressCustomerListQuery(QueryCondition query){
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ( ");
        sql.append("select name,phone,email,card_no as cardNo,");
        sql.append("pay_date as payDate,exam_time as examTime,");
        sql.append("case when sex = 1 then '女' else '男' end as sex,");
        sql.append("case when is_already_paid = 0 then '否' when is_already_paid = 1 then '是' else '' end as isAlreadyPaid, ");
        sql.append(" '0' type, ");
        sql.append(" sc.address, ");
        sql.append(" sc.create_time createTime, ");
        sql.append(" '正常注册数据' dataType");
        sql.append(" from sims_customer sc ");
        sql.append(" where sys_state = 1  ");
        sql.append(" and is_already_paid = 1 ");
        sql.append(" and address is not null ");
        if(query.getConditions()!=null && !query.getConditions().isEmpty()
                && query.getConditions().containsKey("examTime")){
            sql.append(" and sc.exam_time =  :examTime ");
        }
        sql.append(" ) aa ");
        sql.append(query.getWhereSQL());
        query.setFullSQL(sql.toString());
        return sql.toString();
    }

    @Override
    public boolean validateCustomer(String userName, String passWord) {
        StringBuilder hql = new StringBuilder("from CustomerModel where userName =:userName and passWord =:passWord and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("userName",userName);
        params.put("passWord", MD5Util.MD5Encode(passWord,null));
        if(this.queryUniquneObject(hql.toString(),params) == null){
            return false;
        }
        return true;
    }

    @Override
    public void deleteByExamTime(String examTime) {
        StringBuilder hql = new StringBuilder("update CustomerModel set systemState=0 where examTime = :examTime and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("examTime",examTime);
        this.update(hql.toString(),params);
    }
}
