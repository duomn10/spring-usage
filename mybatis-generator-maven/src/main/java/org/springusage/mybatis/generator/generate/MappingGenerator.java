package org.springusage.mybatis.generator.generate;

import org.apache.velocity.VelocityContext;
import org.springusage.mybatis.generator.domain.Table;


public class MappingGenerator extends AbstractorGenerator {

	@Override
	public void setContextValue(VelocityContext context, Table table) {
		context.put("table", table);
		context.put("fieldList", table.getFieldList());
		context.put("className", table.getClassName());
	}

	@Override
	public String getTemplatePath() {
		return "org/springusage/mybatis/generator/template/mapping.vm";
	}

	@Override
	public String getDestFilePath(VelocityContext context, Table table) {
		return "domain/mapping/" + table.getClassName() + ".xml";
	}

}
