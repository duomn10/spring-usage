package org.springusage.mybatis.generator.utils;

import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.springusage.mybatis.generator.domain.Table;
import org.springusage.mybatis.generator.generate.DaoImplGenerator;
import org.springusage.mybatis.generator.generate.DomainGenerat;
import org.springusage.mybatis.generator.generate.IDaoGenerator;
import org.springusage.mybatis.generator.generate.MappingGenerator;


/**
 * 使用模板生成文件的工具类
 * @author duomn
 *
 */
public class GenerateUtils {
	
	public static void prepareSetting() {
		Properties prop = new Properties();
    	prop.setProperty("runtime.log", "velocity_example.log");
    	prop.setProperty("resource.loader", "class");
    	prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    	Velocity.init(prop);
	}
	
	public static void main(String[] args) {
		PropertyUtils.init();
		DBPool.init();
		GenerateUtils.prepareSetting();
		StringUtils.tabPrefix = PropertyUtils.getProperty("table_prefix");
		StringUtils.classPrefix = PropertyUtils.getProperty("class_prefix");
//		String basePath = "E://generatorCode/src/com/" + PropertyUtils.getProperty("companyName") 
//					+ "/" + PropertyUtils.getProperty("projectName") + "/" + PropertyUtils.getProperty("moduleName") + "/";
//		String basePath = "D:/DEV/eclipse/workspace/auto/src/com/duomn/test/exsto";
		String basePath = PropertyUtils.getProperty("generate_base_path");
		
		try {
			List<Table> tableList = TableManager.getTableList();
			DomainGenerat domainG = new DomainGenerat();
			domainG.setBasePath(basePath);
			
			MappingGenerator mappingG = new MappingGenerator();
			mappingG.setBasePath(basePath);
			
			IDaoGenerator idaoG = new IDaoGenerator();
			idaoG.setBasePath(basePath);
			
			DaoImplGenerator daoimplG = new DaoImplGenerator();
			daoimplG.setBasePath(basePath);
			
			for (Table table : tableList) {
				domainG.go(table);
				mappingG.go(table);
				idaoG.go(table);
				daoimplG.go(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DBPool.destory();
	}
	
}
