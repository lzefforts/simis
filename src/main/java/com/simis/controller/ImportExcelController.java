package com.simis.controller;

import com.simis.common.Constants;
import com.simis.service.CustomerImportService;
import com.simis.util.ImportExcelUtil;
import com.simis.vo.CustomerImportVo;
import com.simis.vo.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入excel
 * Created by 一拳超人
 */
@Controller
@RequestMapping("/manage/")
public class ImportExcelController {
    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerImportService customerImportService;

    /**
     * 描述：通过传统方式form表单提交方式导入excel文件
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="upload",method={RequestMethod.GET,RequestMethod.POST})
    public void uploadExcel(HttpServletRequest request) throws Exception {
        ResultMsg msg = new ResultMsg();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        LOGGER.info("通过传统方式form表单提交方式导入excel文件！");
        InputStream in =null;
        List<List<Object>> listob = null;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        String examTime = request.getParameter("importTime");
        //转换为List<CustomerImportVo>形式
        List<CustomerImportVo> resultVos = transferData(listob,examTime);
        //保存到导入历史表
        customerImportService.saveImportData(resultVos);
        request.setAttribute("uploadFlag","上传成功!");
    }

    /**
     * 描述：通过 jquery.form.js 插件提供的ajax方式上传文件
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value="ajaxUpload",method={RequestMethod.GET,RequestMethod.POST})
    public  ResultMsg  ajaxUploadExcel(@RequestParam("file") MultipartFile file,
                                       @RequestParam("importTime") String importTime,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultMsg msg = new ResultMsg();
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        LOGGER.info("通过 jquery.form.js 提供的ajax方式上传文件！");
        InputStream in =null;
        List<List<Object>> listob = null;
//        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        try {
            //转换为List<CustomerImportVo>形式
            List<CustomerImportVo> resultVos = transferData(listob,importTime);
            //保存到导入历史表
            customerImportService.saveImportData(resultVos);
            LOGGER.info("保存到导入历史表中");
            msg.setSuccess(true);
            msg.setMsg("上传文件成功");
        } catch (Exception e) {
            LOGGER.error("导入数据处理异常",e);
            msg.setSuccess(false);
        }
        return msg;
    }

    //转换数据
    private List<CustomerImportVo> transferData(List<List<Object>> listob,String importTime){
        LOGGER.info("导入数据转换开始");
        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        List<CustomerImportVo> results = new ArrayList<>();
        for (int i = 0; i < listob.size(); i++) {
            if(i<2) continue;
            List<Object> lo = listob.get(i);
            int hasDataColSize = lo.size();//有数据的列数
            LOGGER.info("导入数据每行的个数为{}",hasDataColSize);
            CustomerImportVo vo = new CustomerImportVo();
            vo.setExamTime(importTime);
            if(hasDataColSize == 0){
                LOGGER.info("#####excel中第{}行无数据#####",i);
                return results;
            }
            vo.setName(String.valueOf(lo.get(0)));//客户名
            if(hasDataColSize>1 && "男".equals(String.valueOf(lo.get(1)))){//性别
                vo.setSex(Constants.SEX_MAN);
            }
            if(hasDataColSize>1 && "女".equals(String.valueOf(lo.get(1)))){
                vo.setSex(Constants.SEX_WOMAN);
            }
            if(hasDataColSize>3) {
                vo.setCardNo(String.valueOf(lo.get(3)));//身份证号
            }
//            if(hasDataColSize>7){
//                vo.setPhone(String.valueOf(lo.get(7)));//电话
//            }
//            if(hasDataColSize>3) {
//                vo.setEmail(String.valueOf(lo.get(3)));//邮箱
//            }
//
//            if(hasDataColSize>5) {
//                vo.setEmail(String.valueOf(lo.get(5)));//语言委员会注册时间
//            }
//            if(hasDataColSize>6 && "是".equals(String.valueOf(lo.get(6)))){//是否已经交费
//                vo.setIsAlreadyPaid(Constants.ALEADY_PAID);
//            }
//            if(hasDataColSize>6 && "否".equals(String.valueOf(lo.get(6)))){
//                vo.setIsAlreadyPaid(Constants.NOT_PAID);
//            }
//            if(hasDataColSize>7) {
//                vo.setEmail(String.valueOf(lo.get(7)));//批次号
//            }
//            if(hasDataColSize>8) {
//                vo.setEmail(String.valueOf(lo.get(8)));//交费日期
//            }
            results.add(vo);
        }
        LOGGER.info("导入数据转换结束");
        return results;
    }
}
