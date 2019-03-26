package com.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarshallingUtil {

	private static final Logger LOG = LoggerFactory.getLogger(MarshallingUtil.class);

	private static ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		return mapper;
	}

	public static String marshalToJson(Object o) {

		ObjectMapper mapper = getObjectMapper();

		String str = null;
		try {
			str = mapper.writeValueAsString(o);
		} catch (Exception e) {
			LOG.error("Exception while marshlling to json {}", e);
		}
		return str;
	}

	private MarshallingUtil() {

	}

}
