package cn.gson.oasys.services;

import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.utils.HttpRequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.cserver.rest.util.CserverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 系统相关操作
 */
@Service
@Transactional
public class SystemService {

    private static final Logger logger = LoggerFactory.getLogger(SystemService.class);

    @Value("${PLATFORM_NAME}")
    private String PLATFORM_NAME;

    @Value("${SOFT_ID}")
    private String SOFT_ID;

    /**
     * 获取token
     * @return
     */
    public String getToken(){
        //获取access_token
        try {
            logger.info("获取token入参：PLATFORM_NAME="+PLATFORM_NAME+"  SOFT_ID="+SOFT_ID);
            JSONObject tokenObj = CserverUtil.getAccessToken(PLATFORM_NAME, SOFT_ID);
            logger.info("获取token返参："+tokenObj);
            if (tokenObj.getBoolean("success")){
                return tokenObj.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取token异常："+e.getMessage());
        }
        return null;
    }

    /**
     * UUID生成sysid
     * @return
     */
    public String getSysId(){
        return UUID.randomUUID().toString();
    }

    /**
     * 调用平台接口注册账号
     * @param user
     */
    public JSONObject registerUser(User user,String sysId) {
        JSONObject userObj = new JSONObject();
        userObj.put("sysid",sysId);
        userObj.put("loginName",user.getUserName());
        userObj.put("name", user.getRealName());
        userObj.put("password","123456" );
        userObj.put("register_time",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        userObj.put("email",user.getEamil());
        userObj.put("gender","0" );
        userObj.put("birth","2017-04-12");
        userObj.put("mobile_phone",user.getUserTel());
        userObj.put("company","中服软件");
        userObj.put("province","陕西省");
        userObj.put("city","西安市");
        userObj.put("address","雁塔区高新二路");
        userObj.put("major","计算机科学");
        userObj.put("education","本科");
        logger.info("调用平台接口注册用户入参："+userObj.toString());
        //添加用户 返回结果
        JSONObject addUserJson = null;
        try {
            addUserJson = CserverUtil.addPtUser(PLATFORM_NAME, SOFT_ID, sysId, userObj);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用平台接口注册用户异常："+e.getMessage());
            addUserJson = new JSONObject();
            addUserJson.put("success",false);
            addUserJson.put("resultMessage","添加用户异常");
        }
        logger.info("调用平台接口注册用户返参："+addUserJson);
        return addUserJson;
    }

    /**
     * 修改用户信息
     * @param user
     */
    public JSONObject updatePtUser(User user,String sysId) {
        JSONObject userObj = new JSONObject();
        userObj.put("sysid",sysId);
        userObj.put("loginName",user.getUserName());
        userObj.put("name", user.getRealName());
        //userObj.put("password","123456" );
        userObj.put("register_time",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        userObj.put("email",user.getEamil());
        userObj.put("gender","0" );
        userObj.put("birth","2017-04-12");
        userObj.put("mobile_phone",user.getUserTel());
        userObj.put("company","中服软件");
        userObj.put("province","陕西省");
        userObj.put("city","西安市");
        userObj.put("address",user.getAddress());
        userObj.put("major","计算机科学");
        userObj.put("education",user.getUserEdu());
        logger.info("调用平台接口修改用户信息入参："+userObj.toString());
        //添加用户 返回结果
        JSONObject addUserJson = null;
        try {
            addUserJson = CserverUtil.updatePtUser(PLATFORM_NAME, SOFT_ID, sysId, userObj);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用平台接口修改用户信息异常："+e.getMessage());
            addUserJson = new JSONObject();
            addUserJson.put("success",false);
            addUserJson.put("resultMessage","修改用户信息异常");
        }
        logger.info("调用平台接口修改用户信息返参："+addUserJson);
        return addUserJson;
    }

    /**
     * 调用平台接口记录日志
     * @param username
     * @param sysId
     */
    public JSONObject writeLog(String username,String sysId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("loginName",username);
        jsonObject.put("sysid",sysId);
        jsonObject.put("operation","查看");
        jsonObject.put("object","进入系统");
        jsonObject.put("data",username+"登录系统");
        logger.info("调用平台接口记录日志入参："+jsonObject.toString());
        //String jsonStr = HttpRequestUtil.sendPost(BASE_URL +"/csaas/api/writeLog?access_token=" + token, jsonObject.toString());
        JSONObject logJson = null;
        try {
            logJson = CserverUtil.writeLog(PLATFORM_NAME, SOFT_ID, sysId, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用平台接口记录日志异常："+e.getMessage());
            logJson = new JSONObject();
            logJson.put("success",false);
            logJson.put("resultMessage","日志添加异常");
        }
        logger.info("调用平台接口记录日志返参："+logJson);
        return logJson;
    }

    /**
     * 检查系统有效性
     * @return
     */
    public JSONObject checkSysStatus(String sysId){
        logger.info("检查系统有效性入参：sysId="+sysId);
        //查询系统有效性 返回结果
        JSONObject sysInfoJson = null;
        try {
            sysInfoJson = CserverUtil.getSystemInfo(PLATFORM_NAME, SOFT_ID,sysId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用平台接口记录日志异常："+e.getMessage());
            sysInfoJson = new JSONObject();
            sysInfoJson.put("success",false);
            sysInfoJson.put("resultMessage","检查系统异常");
        }
        logger.info("检查系统有效性出参："+sysInfoJson);
        return sysInfoJson;
    }

    /**
     * 获取登录地址
     * @return
     */
    public JSONObject getLoginUrl(){
        //单点登录
        JSONObject loginObj = null;
        try {
            loginObj = CserverUtil.getLoginUrl(PLATFORM_NAME, SOFT_ID);
            logger.info("获取登录地址出参："+loginObj);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取登录地址异常："+e.getMessage());
            loginObj = new JSONObject();
            loginObj.put("success",false);
            loginObj.put("resultMessage","获取登录地址异常");
        }
        return loginObj;
    }

    /**
     * 获取退出地址
     * @return
     */
    public JSONObject getLogoutUrl(){
        //单点登录
        JSONObject logoutObj = null;
        try {
            logoutObj = CserverUtil.getLogoutUrl(PLATFORM_NAME, SOFT_ID);
            logger.info("获取退出地址出参："+logoutObj);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取退出地址异常："+e.getMessage());
            logoutObj = new JSONObject();
            logoutObj.put("success",false);
            logoutObj.put("resultMessage","获取退出地址异常");
        }
        return logoutObj;
    }
}
