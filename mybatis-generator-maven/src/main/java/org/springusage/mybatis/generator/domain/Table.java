package org.springusage.mybatis.generator.domain;

import java.util.ArrayList;
import java.util.List;

import org.springusage.mybatis.generator.utils.StringUtils;

public class Table {
	private String tableName;
	private List<Field> fieldList = new ArrayList<Field>();
	private List<String> primaryKeyList;

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}

	public String getTableName() {
		return tableName.toUpperCase();
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getPrimaryKeyList() {
		return primaryKeyList;
	}

	public void setPrimaryKeyList(List<String> primaryKeyList) {
		this.primaryKeyList = primaryKeyList;
	}
	
	//help methods
	public String getClassName(){
		return StringUtils.getTableClassName(tableName);
	}
	
	public String getObjectName(){
		return StringUtils.getTableObjectName(tableName);
	}

}
