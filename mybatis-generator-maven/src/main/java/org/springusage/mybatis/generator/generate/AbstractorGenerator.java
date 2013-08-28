package org.springusage.mybatis.generator.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springusage.mybatis.generator.domain.Table;
import org.springusage.mybatis.generator.utils.PropertyUtils;


public abstract class AbstractorGenerator {
	
	private String basePath;
	
	/**
	 * 根据模板生成文件
	 * @param context
	 * @param tempFile
	 * @param destFile
	 */
	public final void go(Table table) {
		// 设置填充模板的参数
		VelocityContext context = new VelocityContext();
		context.put("companyName", PropertyUtils.getProperty("companyName"));
        context.put("projectName", PropertyUtils.getProperty("projectName"));
        context.put("moduleName", PropertyUtils.getProperty("moduleName"));
        context.put("author", "duomn");
        context.put("generatTime", new Date());
        setContextValue(context, table);
        
        // 获取模板
        String templatePath = getTemplatePath();
        Template template = Velocity.getTemplate(templatePath, "UTF-8");
        
        String destPath = getDestFilePath(context, table);
        destPath = (basePath.endsWith("/") ? basePath : basePath + "/") 
        		+ (destPath.startsWith("/") ? destPath.substring(1) : destPath);
        File file = new File(destPath);
        if (!file.getParentFile().exists()) { // 建立父级的文件目录
        	file.getParentFile().mkdirs();
        }
        
        BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			template.merge(context, writer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public final void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/** 设置生成文件时所需要的参数 */
	protected abstract void setContextValue(VelocityContext context, Table table);

	/** 返回模板文件的路径，路径与Velocity的设置相关 */
	protected abstract String getTemplatePath();
	
	/** 返回生成文件的路径 */
	protected abstract String getDestFilePath(VelocityContext context, Table table);
	
}
