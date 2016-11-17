package com.huawei.esdk.sso.service;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.huawei.esdk.platform.common.utils.MaskUtils;
import com.huawei.esdk.sso.SDKResult;
import com.huawei.esdk.sso.SSOResult;

public class SSOXMLService extends AbstractService implements ISSOService
{
    protected static final Logger LOGGER = Logger.getLogger(SSOXMLService.class);
    
    private static SSOXMLService instance = new SSOXMLService();
    
    private SSOXMLService()
    {
    }
    
    public static SSOXMLService getInstance()
    {
        return instance;
    }
    
    @Override
    public String ssoAuth(String message)
    {
        Map<String, Object> params = parseXML(message);
        SDKResult<SSOResult> sdkResult = processor.doAuthenticate(params);
        String ssoErrorCode = sdkResult.getResultCode();
        if (!"0".equals(ssoErrorCode))
        {
            LOGGER.warn("Fail to auth through SSO Server, the error code is :" + ssoErrorCode);
            LOGGER.warn("The message is:" + "\n\r" + MaskUtils.maskXMLElementValue(message, "pwd"));
        }
        String esdkCode = getEsdkErrorCode(ssoErrorCode);
        
        String xml = buildResMsgBody(esdkCode, null == sdkResult.getResult() ? new SSOResult() : sdkResult.getResult());
        return xml;
    }
    
    @Override
    public String buildResMsgBody(String code, SSOResult result)
    {
        String xml = "<root><resCode>";
        xml = xml + code + "</resCode>";
        xml = xml + "<UID>" + (null == result.getUid() ? "" : result.getUid()) + "</UID>";
        xml = xml + "<credential>" + (null == result.getCredential() ? "" : result.getCredential()) + "</credential>";
        xml = xml + "<remark1>" + (null == result.getRemark1() ? "" : result.getRemark1()) + "</remark1>";
        xml = xml + "<remark2>" + (null == result.getRemark2() ? "" : result.getRemark2()) + "</remark2>";
        xml = xml + "</root>";
        return xml;
    }
    
    private Map<String, Object> parseXML(String xml)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("enterpriseID", getElementFromStr("enterpriseID", xml));
		map.put("clientIPAddresss", getElementFromStr("clientIPAddresss", xml));
		map.put("authType", getElementFromStr("authType", xml));
		map.put("credential", getElementFromStr("credential", xml));
		map.put("account", getElementFromStr("account", xml));
		map.put("pwd", getElementFromStr("pwd", xml));
		map.put("remark1", getElementFromStr("remark1", xml));
		map.put("remark2", getElementFromStr("remark2", xml));

        return map;
    }
	
	private String getElementFromStr(String elemName, String str){
		if(null == elemName || null == str ||
		elemName.isEmpty() || str.isEmpty())
		{
		return "";
		}
		int start = str.indexOf("<" + elemName + ">");
		int end = str.lastIndexOf("</" + elemName + ">");

		if(-1 == start || -1 == end)
		{
		return "";
		}

		return str.substring(start + elemName.length() + 2, end);
	}

}
