package com.huawei.esdk.sso.demo;

import com.huawei.esdk.sso.SDKResult;
import com.huawei.esdk.sso.SSOAuthProcessor;
import com.huawei.esdk.sso.SSOResult;
import java.util.Map;
import org.apache.log4j.Logger;

public class DemoImpl
  implements SSOAuthProcessor
{
  private static final Logger LOGGER = Logger.getLogger(DemoImpl.class);

  public SDKResult<SSOResult> doAuthenticate(Map<String, Object> parameters)
  {
    String authType = (String)parameters.get("authType");
    LOGGER.info("authType=" + authType);
    String credential = (String)parameters.get("credential");
    LOGGER.info("credential=" + credential);
    String account = (String)parameters.get("account");
    LOGGER.info("account=" + account);
    String pwd = (String)parameters.get("pwd");
    LOGGER.info("pwd=" + pwd);

    SDKResult result = new SDKResult();
    SSOResult ssoResult = new SSOResult();

    String resultCode = null;
    if ("Ticket".equals(authType))
    {
      if ((credential == null) || (credential.trim().length() == 0))
      {
        resultCode = "-14";
      }
      else if (credential.equals(getPropValue("ticket.minus.30")))
      {
        resultCode = "-30";
      }
      else if (credential.equals(getPropValue("ticket.minus.32")))
      {
        resultCode = "-32";
      }
      else if (credential.equals(getPropValue("ticket.minus.33")))
      {
        resultCode = "-33";
      }
      else if (credential.equals(getPropValue("ticket.minus.40")))
      {
        resultCode = "-40";
      }
      else if (credential.equals(getPropValue("ticket.minus.41")))
      {
        resultCode = "-41";
      }
      else
      {
        String[] validTickets = getPropValue("ticket.valid.tickets").split(",");
        String[] validUids = getPropValue("ticket.valid.uids").split(",");
        boolean ticketOK = false;
        for (int i = 0; i < validTickets.length; ++i)
        {
          if (!credential.equals(validTickets[i]))
            continue;
          ticketOK = true;
          ssoResult.setUid(validUids[i]);
          break;
        }

        if (ticketOK)
        {
          resultCode = "0";
        }
        else
        {
          resultCode = "-31";
        }
      }
    }
    else if ("AccountAndPwd".equals(authType))
    {
      if ((account == null) || (account.trim().length() == 0))
      {
        resultCode = "-14";
      }
      else if (account.equals(getPropValue("account.minus.1")))
      {
        resultCode = "-1";
      }
      else if (account.equals(getPropValue("account.minus.5")))
      {
        resultCode = "-5";
      }
      else if (account.equals(getPropValue("account.minus.8")))
      {
        resultCode = "-8";
      }
      else if (account.equals(getPropValue("account.minus.9")))
      {
        resultCode = "-9";
      }
      else if (account.equals(getPropValue("account.minus.40")))
      {
        resultCode = "-40";
      }
      else if (account.equals(getPropValue("account.minus.41")))
      {
        resultCode = "-41";
      }
      else
      {
        String[] validAccts = getPropValue("account.valid.ids").split(",");
        String[] validAcctsPwd = getPropValue("account.valid.pwds").split(",");
        String[] validTickets = getPropValue("account.valid.tickets").split(",");
        boolean hasUser = false;
        for (int i = 0; i < validAccts.length; ++i)
        {
          if (!account.equals(validAccts[i]))
            continue;
          hasUser = true;
          if (validAcctsPwd[i].equals(pwd))
          {
            ssoResult.setCredential(validTickets[i]); break;
          }

          resultCode = "-2";

          break;
        }

        if (!hasUser)
        {
          resultCode = "-3";
        }
        else if (resultCode == null)
        {
          resultCode = "0";
        }

      }

      ssoResult.setUid(account);
    }
    else
    {
      resultCode = "-1";
    }

    result.setResultCode(resultCode);
    result.setResult(ssoResult);
    ssoResult.setRemark1("remarks-1");

    return result;
  }

  private String getPropValue(String key)
  {
    return PropertiesUtils.getValue(key);
  }
}