package org.springusage.mybatis.generator.generate;

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.springusage.mybatis.generator.domain.Table;
import org.springusage.mybatis.generator.utils.DBPool;
import org.springusage.mybatis.generator.utils.GenerateUtils;
import org.springusage.mybatis.generator.utils.PropertyUtils;
import org.springusage.mybatis.generator.utils.TableManager;


public class DomainGenerat extends AbstractorGenerator {
	
	@Override
	public void setContextValue(VelocityContext context, Table table) {
         context.put("className", table.getClassName());
         context.put("fieldList", table.getFieldList());
	}

	@Override
	public String getTemplatePath() {
		return "org/springusage/mybatis/generator/template/domain.vm";
	}

	@Override
	public String getDestFilePath(VelocityContext context, Table table) {
		return "domain/" + table.getClassName() + ".java";
	}
	
	public static void main(String[] args) throws Exception {
		PropertyUtils.init();
		DBPool.init();
		GenerateUtils.prepareSetting();
		
		List<Table> tableList = TableManager.getTableList();
		DomainGenerat domainG = new DomainGenerat();
		for (Table table : tableList) {
			domainG.go(table);
		}
		
		DBPool.destory();
	}

}
