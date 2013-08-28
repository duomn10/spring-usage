package org.springusage.mybatis.generator.utils;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class StringUtils {
	
	public static String classPrefix = "";
	
	public static String tabPrefix = "";

	/** 根据表名获取类名 */
	public static String getTableClassName(String tableName){
		if (tabPrefix.length() > 0 && !tableName.toLowerCase().startsWith(tabPrefix.toLowerCase())) {
			throw new RuntimeException("请确认处理的表的前缀是否为：" + tabPrefix);
		}
		String noPrefix = firstUpper(classPrefix) + "_" + tableName.substring(tabPrefix.length());
		return castUnderLine(noPrefix);
	}
	
	/** 根据表名获取对象名 */
	public static String getTableObjectName(String tableName){
		String className = firstUpper(classPrefix) + getTableClassName(tableName);
		return className.substring(0, 1).toLowerCase() + className.substring(1);
	}
	
	/** 根据列名获取属性的大写名称 */
	public static String getColumnMethodName(String columnName) {
		return castUnderLine(columnName);
	}
	
	/** 根据列名获取属性的名称  */
	public static String getColumnFieldName(String columeName) {
		String columnName = getColumnMethodName(columeName);
		return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
	}
	
	/** 去掉连接的下划线，并且每一部分的首字母大写 */
	private static String castUnderLine(String origin) {
		String sa[] = origin.split("[_]");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sa.length; i++) {
			String s = firstUpper(sa[i]);
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static String firstUpper(String s) {
		if (s.length() == 0)
			return "";
		char ch[];
		ch = s.toLowerCase().toCharArray();
		ch[0] = (char) (ch[0] - 32);
		String result = new String(ch);
		return result;
	}
	
	/**
	 * 生成UUID
	 * @return
	 */
	public static String UUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 字符串首字母大写
	 * @param src 源字符串
	 * @return
	 */
	public static String upperCaseFirst(String src) {
		if (src == null) return null;
		if (src.length() <= 0) return "";
		return src.substring(0, 1).toUpperCase() + src.substring(1);
	}
	
	/**
	 * 字符串首字母小写
	 * @param src 源字符串
	 * @return
	 */
	public static String lowerCaseFirst(String src) {
		if (src == null) return null;
		if (src.length() <= 0) return "";
		return src.substring(0, 1).toLowerCase() + src.substring(1);
	}
	
	/**
	 * 把数组以指定字符，拼接为一个字符串
	 * @param arr 字符串数组 
	 * @param joinStr 合并字符串
	 * @return 
	 */
	public static String arrayJoin(String[] arr, String joinStr) {
		if (arr == null || arr.length == 0) return null;
		if (joinStr == null) joinStr = "";
		StringBuffer buf = new StringBuffer();
		for (String str : arr) 
			buf.append(str).append(joinStr);
		return buf.substring(0, buf.length() - joinStr.length());
	}
	
	/**
	 * 把map中的内容以字符串的格式返回
	 * @param map
	 * @return [key1:'value1', key2:'value2']
	 */
	public static String map2String(Map<String, ?> map) {
		if (map == null || map.size() <= 0) return "";
		StringBuffer buf = new StringBuffer("[");
		for (Entry<String, ?> item : map.entrySet()) {
			buf.append(item.getKey()).append(":'").append(item.getValue().toString()).append("', ");
		}
		buf.replace(buf.length() - 2, buf.length(), "]");
		return buf.toString();
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() <= 0;
	}
	
	/**
	 * 把空对象用默认字符串替换
	 * @param str 未知结果的字符串
	 * @param defaultStr  默认替换的字符串
	 * @return
	 */
	public static String nullString(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}
	
	/**
	 * 把空对象用默认字符串("")替换
	 * @param str 未知结果的字符串
	 * @return
	 */
	public static String nullString(String str) {
		return nullString(str, "");
	}
	
//	/**
//	 * 检验是否符合正则表达式
//	 * @param regex	正则表达式
//	 * @param value 待检验的字符串
//	 * @return
//	 */
//	public static boolean checkRegex(String regex, String value) {
//		return Pattern.compile(regex).matcher(value).matches();
//	}
	
	public static void main(String[] args) {
	}
}
