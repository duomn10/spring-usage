package org.springusage.mybatis.generator.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springusage.mybatis.generator.domain.Field;
import org.springusage.mybatis.generator.domain.Table;



public class TableManager {
	
	private static final Log log = LogFactory.getLog(TableManager.class);

	private static List<String> getAllTableNames(Connection connection) throws Exception {
		List<String> allTables = new ArrayList<String>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = meta.getTables(null, PropertyUtils.getProperty("jdbc.username").toUpperCase(), null, new String[] { "TABLE" });
		String prefix = PropertyUtils.getProperty("scan_table_condition").toLowerCase();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			if (tableName.indexOf("$") > 0 || tableName.indexOf("0") > 0) { // 过滤系统表
				continue;
			}
			if (!tableName.toLowerCase().startsWith(prefix)) {
				continue;
			}
			allTables.add(tableName);
		}
		rs.close();
		return allTables;
	}

	public static List<Table> getTableList() throws Exception {
		Connection connection = DBPool.acquireConnection();
		try {
			log.debug("Query TablesName from DB.");
			List<String> allTables = getAllTableNames(connection);
			log.debug("Tables sum count:" + allTables.size());
			String projectName = PropertyUtils.getProperty("projectName");
			String companyName = PropertyUtils.getProperty("companyName");
			log.debug("companyName:'" + companyName + "', projectName:'" + projectName + "'" );
			List<Table> tableList = new ArrayList<Table>();
			for (String tableName : allTables) {
				log.debug("Query table '" + tableName + "' fields details.");
				Table table = new Table();
				table.setTableName(tableName);
				List<Field> fieldList = getFieldListByTableName(connection, tableName);
				table.setFieldList(fieldList);
				List<String> primaryKey = getPrimaryKeyListByTableName(connection, tableName);
				table.setPrimaryKeyList(primaryKey);
				tableList.add(table);
			}
			
			File file = new File("E:/generatorCode");
			FileUtils.deleteDirectory(file);
//			FileUtils.mkdirs(new File("E://generatorCode/src/com/"+companyName+"/" + projectName + "/action"));
//			FileUtils.mkdirs(new File("E://generatorCode/src/com/"+companyName+"/" + projectName + "/domain/mapping"));
//			FileUtils.mkdirs(new File("E://generatorCode/src/com/"+companyName+"/" + projectName + "/service/impl"));
//			FileUtils.mkdirs(new File("E://generatorCode/src/com/"+companyName+"/" + projectName + "/dao/impl"));
//			FileUtils.mkdirs(new File("E://generatorCode/WebRoot/view/"));
			
			return tableList;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			DBPool.releaseConnection(connection);
		}
	}

	private static List<Field> getFieldListByTableName(Connection connection, String tableName) throws Exception {
		List<Field> fieldList = new ArrayList<Field>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = null;
		rs = meta.getColumns(null, PropertyUtils.getProperty("jdbc.username").toUpperCase(), tableName, null);
		while (rs.next()) {
			Field field = new Field();
			// 列名称
			String columnName = rs.getString("COLUMN_NAME");
			//小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 0。 
			int decimal_digits = rs.getInt("DECIMAL_DIGITS");
			// 来自 java.sql.Types 的 SQL 类型
			int datatype = rs.getInt("DATA_TYPE");
			// 列的大小。
			int size = rs.getInt("COLUMN_SIZE");
			// 是否允许使用 NULL。
			int nullable = rs.getInt("NULLABLE");
			if (nullable == DatabaseMetaData.columnNullable) {
				field.setNullable(true);
			} else {
				field.setNullable(false);
			}
			//描述列的注释（可为 null）
			String remarks = rs.getString("REMARKS");
			field.setRemarks(remarks);
			field.setColumnName(columnName);
			field.setSize(String.valueOf(size));
			field.setDatatype(String.valueOf(datatype));
			field.setDecimal_digits(decimal_digits);
			fieldList.add(field);
		}
		rs.close();
		return fieldList;
	}

	private static List<String> getPrimaryKeyListByTableName(Connection connection, String tableName) throws Exception {

		List<String> primaryKeyList = new ArrayList<String>();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = null;
		rs = meta.getPrimaryKeys(null, tableName, tableName);

		while (rs.next()) {
			String culumnName = rs.getString("COLUMN_NAME");
			primaryKeyList.add(culumnName);
		}
		rs.close();
		return primaryKeyList;
	}
	
	public static void main(String[] args) {
		PropertyUtils.init();
		DBPool.init();
		try {
			List<Table> tables = TableManager.getTableList();
			System.out.println("Table Count:" + tables.size());
			for (Table table : tables) {
				String className = table.getClassName();			
				System.out.println(className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBPool.destory();
	}
}
