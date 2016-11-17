package com.huawei.esdk.sso.utils;



import java.io.IOException;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils
{
	private static final Logger LOGGER = Logger.getLogger(JsonUtils.class);

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static
	{
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public static <T> T fromJson(String jsonAsString, Class<T> pojoClass)
	{
		try
		{
			return objectMapper.readValue(jsonAsString, pojoClass);
		} catch (JsonParseException e)
		{
			LOGGER.error("JSON ERROR." + e);
		} catch (JsonMappingException e)
		{
			LOGGER.error("JSON ERROR." + e);
		} catch (IOException e)
		{
			LOGGER.error("JSON ERROR." + e);
		}
		return null;
	}
}
