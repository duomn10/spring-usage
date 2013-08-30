package org.duomn.naive.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

public class JsonUtil {
	/** 1:成功;0:失败.*/
	private static final String RETURN_FLAG = "flag";
	
	private static final String RETURN_MSG = "msg";
	
	/** 获取简单的错误信息 */
	public static Map<String, Object> getSimpleErrorMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RETURN_FLAG, "0");
		map.put(RETURN_MSG, obj);
		return map;
	}
	
	/** 获取简单的成功信息 */
	public static Map<String, Object> getSimpleSuccessMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RETURN_FLAG, "1");
		map.put(RETURN_MSG, obj);
		return map;
	}

}
