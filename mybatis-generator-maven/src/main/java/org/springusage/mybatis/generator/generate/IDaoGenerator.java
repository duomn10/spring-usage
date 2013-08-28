package org.springusage.mybatis.generator.generate;

import org.apache.velocity.VelocityContext;
import org.springusage.mybatis.generator.domain.Table;


public class IDaoGenerator extends AbstractorGenerator {
	
	@Override
	public void setContextValue(VelocityContext context, Table table) {
//         context.put("className", table.getClassName());
//         context.put("fieldList", table.getFieldList());
         context.put("table", table);
	}

	@Override
	public String getTemplatePath() {
		return "org/springusage/mybatis/generator/template/idao.vm";
	}

	@Override
	public String getDestFilePath(VelocityContext context, Table table) {
		return "dao/I" + table.getClassName() + "Dao.java";
	}
	

}
