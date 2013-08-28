package org.springusage.mybatis.generator.generate;

import org.apache.velocity.VelocityContext;
import org.springusage.mybatis.generator.domain.Table;


public class DaoImplGenerator extends AbstractorGenerator {
	
	@Override
	public void setContextValue(VelocityContext context, Table table) {
         context.put("table", table);
	}

	@Override
	public String getTemplatePath() {
		return "org/springusage/mybatis/generator/template/daoimpl.vm";
	}

	@Override
	public String getDestFilePath(VelocityContext context, Table table) {
		return "dao/impl/" + table.getClassName() + "DaoImpl.java";
	}
	

}
