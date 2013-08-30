package org.duomn.naive.common.util;

import java.util.UUID;

public class BusinessUtil {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().toLowerCase();
	}
	
}
