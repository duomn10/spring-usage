package org.springusage.mybatis.generator.domain;

import java.sql.Types;

import org.springusage.mybatis.generator.utils.StringUtils;
public class Field {

	//列名
	private String columnName;

	//数据类型
	private String datatype;

	//注释
	private String remarks;

	//数据长度
	private String size;

	//是否可为空
	private boolean nullable;
	
	//小数部分的位数，不是小数时返回0
	private int decimal_digits;
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getRemarks() {
		if(null == this.remarks || this.remarks.length() > 10 || this.remarks.length() < 1){
			this.remarks = this.getFieldName();
		}
		return remarks;
	}
	
	public String getRealRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public int getDecimal_digits() {
		return decimal_digits;
	}

	public void setDecimal_digits(int decimalDigits) {
		decimal_digits = decimalDigits;
	}

	// help methods
	public String getMethodName() {
		return StringUtils.getColumnMethodName(columnName);
	}

	public String getFieldName() {
		return StringUtils.getColumnFieldName(columnName);
	}

	public String getJavaType() {
		String answer;
		switch (new Integer(this.datatype)) {
		case Types.ARRAY:
			answer = "Object";
			break;

		case Types.BIGINT:
			answer = "long";
			break;

		case Types.BINARY:
			answer = "byte[]";
			break;

		case Types.BIT:
			answer = "boolean";
			break;

		case Types.BLOB:
			answer = "byte[]";
			break;

		case Types.BOOLEAN:
			answer = "boolean";
			break;

		case Types.CHAR:
			answer = "String";
			break;

		case Types.CLOB:
			answer = "String";
			break;

		case Types.DATALINK:
			answer = "Object";
			break;

		case Types.DATE:
			answer = "Date";
			break;

		case Types.DECIMAL:
			if ((new Integer(this.getSize()) > 18) || this.getDecimal_digits() > 0) {
				answer = "BigDecimal";
			} else if (new Integer(this.getSize()) > 9) {
				answer = "long";
			} else {
				answer = "long";
			}
			break;

		case Types.DISTINCT:
			answer = "Object";
			break;

		case Types.DOUBLE:
			answer = "double";
			break;

		case Types.FLOAT:
			answer = "double";
			break;

		case Types.INTEGER:
			answer = "int";
			break;

		case Types.JAVA_OBJECT:
			answer = "Object";
			break;

		case Types.LONGVARBINARY:
			answer = "byte[]";
			break;

		case Types.LONGVARCHAR:
			answer = "String";
			break;

		case Types.NULL:
			answer = "Object";
			break;

		case Types.NUMERIC:
			if ((new Integer(this.getSize()) > 18) || this.getDecimal_digits() > 0) {
				answer = "BigDecimal";
			} else if (new Integer(this.getSize()) > 9) {
				answer = "long";
			} else {
				answer = "long";
			}
			break;

		case Types.OTHER:
			answer = "Object";
			break;

		case Types.REAL:
			answer = "float";
			break;

		case Types.REF:
			answer = "Object";
			break;

		case Types.SMALLINT:
			answer = "long";
			break;

		case Types.STRUCT:
			answer = "Object";
			break;

		case Types.TIME:
			answer = "Date";
			break;

		case Types.TIMESTAMP:
			answer = "Date";
			break;

		case Types.TINYINT:
			answer = "byte";
			break;

		case Types.VARBINARY:
			answer = "byte[]";
			break;

		case Types.VARCHAR:
			answer = "String";
			break;

		default:
			answer = "String";
			break;
		}
		return answer;
	}

	public String getJdbcType() {
		String answer;
		switch (new Integer(this.datatype)) {
		case Types.ARRAY:
			answer = "ARRAY";
			break;

		case Types.BIGINT:
			answer = "BIGINT";
			break;

		case Types.BINARY:
			answer = "BINARY";
			break;

		case Types.BIT:
			answer = "BIT";
			break;

		case Types.BLOB:
			answer = "BLOB";
			break;

		case Types.BOOLEAN:
			answer = "BOOLEAN";
			break;

		case Types.CHAR:
			answer = "CHAR";
			break;

		case Types.CLOB:
			answer = "CLOB";
			break;

		case Types.DATALINK:
			answer = "DATALINK";
			break;

		case Types.DATE:
			answer = "DATE";
			break;

		case Types.DECIMAL:
			answer = "DECIMAL";
			break;

		case Types.DISTINCT:
			answer = "DISTINCT";
			break;

		case Types.DOUBLE:
			answer = "DOUBLE";
			break;

		case Types.FLOAT:
			answer = "FLOAT";
			break;

		case Types.INTEGER:
			answer = "Long";
			break;

		case Types.JAVA_OBJECT:
			answer = "JAVA_OBJECT";
			break;

		case Types.LONGVARBINARY:
			answer = "LONGVARBINARY";
			break;

		case Types.LONGVARCHAR:
			answer = "LONGVARCHAR";
			break;

		case Types.NULL:
			answer = "NULL";
			break;

		case Types.NUMERIC:
			answer = "NUMERIC";
			break;

		case Types.OTHER:
			answer = "OTHER";
			break;

		case Types.REAL:
			answer = "REAL";
			break;

		case Types.REF:
			answer = "REF";
			break;

		case Types.SMALLINT:
			answer = "SMALLINT";
			break;

		case Types.STRUCT:
			answer = "STRUCT";
			break;

		case Types.TIME:
			answer = "TIME";
			break;

		case Types.TIMESTAMP:
			answer = "TIMESTAMP";
			break;

		case Types.TINYINT:
			answer = "TINYINT";
			break;

		case Types.VARBINARY:
			answer = "VARBINARY";
			break;

		case Types.VARCHAR:
			answer = "VARCHAR";
			break;

		default:
			answer = "VARCHAR";
			break;
		}
		return answer;
	}
}