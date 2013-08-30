package org.duomn.naive.common.util;

public class PagingUtil {
	
	/**
	 * 获取分页的起始记录
	 * @param pn 第pn页
	 * @param pageSize 每页的记录条数
	 * @return 第pn页的第一个记录的下标
	 */
	public static int getStartIndex(int pn, int pageSize) {
		return (pn - 1) * pageSize;
	}

}
