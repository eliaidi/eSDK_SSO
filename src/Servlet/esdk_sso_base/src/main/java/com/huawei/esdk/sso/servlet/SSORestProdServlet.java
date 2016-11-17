package com.huawei.esdk.sso.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.bean.log.InterfaceLogBean;
import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.log.itf.IInterfaceLog;
import com.huawei.esdk.sso.SSOAuthProcessor;
import com.huawei.esdk.sso.SSOResult;
import com.huawei.esdk.sso.service.ISSOService;
import com.huawei.esdk.sso.service.SSORestProdService;

public class SSORestProdServlet extends HttpServlet
{
    /*
     * Serialization UID
     */
    private static final long serialVersionUID = 1L;
    
    protected static final Logger LOGGER = Logger.getLogger(SSOXMLServlet.class);
    
    private ISSOService ssoService;
    
    private List<String> ips = new ArrayList<String>();
    
    private String processRequest(HttpServletRequest req)
    {
        LOGGER.debug("message received");
        String ip = req.getRemoteHost();
        String result = "";
        
        try
        {
            if (!ips.contains(ip))
            {
                result = ssoService.buildResMsgBody("2", new SSOResult());
                LOGGER.warn("the ip :" + ip + "is not allowed to SSO");
            }
            else
            {
                String inputMessage = IOUtils.toString(req.getInputStream());
                
//                LOGGER.debug("The incoming message is :" + inputMessage);
                result = ssoService.ssoAuth(inputMessage);
                LOGGER.debug("The response message is " + result);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("processRequest method Exception");
            result = ssoService.buildResMsgBody("-1", new SSOResult());
        }
        
        return result;
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
    	try
    	{
	        String uuid = null;
	        OutputStream os = null;
	        
	        try
	        {
	        	uuid = UUID.randomUUID().toString();
	            doInterfaceLogReq(uuid, req);
	            
	            String result = processRequest(req);
	//            resp.setContentType("application/json");
	            
	            os = resp.getOutputStream();
	            os.write(result.getBytes("UTF-8"));
	            doInterfaceLogRes(uuid, result);
	        }
	        catch (IOException e)
	        {
	            LOGGER.error("doPost method IOException");
	            doInterfaceLogRes(uuid, "-1");
	        }
	        catch (Exception e)
	        {
	            LOGGER.error("doPost method Exception");
	            doInterfaceLogRes(uuid, "");
	        }
			catch (Throwable t) 
			{
				LOGGER.error("caught throwable at top level");
			}
	        finally
	        {
	            try
	            {
	                if (null != os)
	                {
	                    os.close();
	                }
	            }
	            catch (IOException e)
	            {
	                LOGGER.error("doPost method Exception");
	            }
	        }
    	}
        catch (Throwable t) 
    	{
    		LOGGER.error("caught throwable at top level");
    	}
    }
    
    private void doInterfaceLogReq(String messageId, HttpServletRequest req)
    {
        InterfaceLogBean bean = new InterfaceLogBean();
        bean.setTransactionId(messageId);
        bean.setProduct("SSO");
        bean.setInterfaceType("2");
        bean.setProtocolType("HTTP");
        bean.setReq(true);
        bean.setName("SSO.authenticate.product.rest");
        bean.setSourceAddr(req.getRemoteHost());
        bean.setTargetAddr(req.getLocalAddr());
        bean.setReqTime(new Date());
//            bean.setReqParams();
        
        IInterfaceLog logger = ApplicationContextUtil.getBean("interfaceLogger");
        logger.info(bean);
    }
    
    private void doInterfaceLogRes(String messageId, String jsonPayload)
    {
        int first = jsonPayload.indexOf("\"resCode\"") + 11;
        int end = jsonPayload.indexOf("\"", first);
        String resultCode;
        if (first > -1)
        {
            resultCode = jsonPayload.substring(first, end);
        }
        else
        {
            resultCode = "-1";
        }
        
        InterfaceLogBean bean = new InterfaceLogBean();
        bean.setTransactionId(messageId);
        bean.setReq(false);
        bean.setRespTime(new Date());
        bean.setResultCode(resultCode);
        
        IInterfaceLog logger = ApplicationContextUtil.getBean("interfaceLogger");
        logger.info(bean);
    }
    
    public void init(ServletConfig config)
    {
        String authIPs = StringUtils.avoidNull(ConfigManager.getInstance().getValue("auth_ips"));
        String[] hosts = authIPs.split(",");
        for (String host : hosts)
        {
            ips.add(host);
        }
        
        ssoService = SSORestProdService.getInstance();
        
        String className = ConfigManager.getInstance().getValue("sso_auth_processor");
        
        String errorMsg =
            "Registeration the implementation of SSOAuthProcessor failed, please check the configuration.";
        try
        {
            Class<?> clz = Class.forName(className);
            Object obj = clz.newInstance();
            SSOAuthProcessor authProcessor = (SSOAuthProcessor)obj;
            ssoService.registerSSOAuthProcessor(authProcessor);
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error(errorMsg);
            LOGGER.error("init method Exception");
        }
        catch (InstantiationException e)
        {
            LOGGER.error(errorMsg);
            LOGGER.error("init method Exception");
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error("init method Exception");
        }
    }
}
