package org.springusage.mybatis.generator.test;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;


public class Example {
    public Example(String templateFile) {
        try {

        	Properties prop = new Properties();
        	prop.setProperty("runtime.log", "velocity_example.log");
        	prop.setProperty("resource.loader", "class");
        	prop.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        	Velocity.init(prop);
        	// 默认是文件加载器
//        	Velocity.init("./bin/velocity.properties");
            
            VelocityContext context = new VelocityContext();
            context.put("list", getNames());

            Template template =  null;

            try {
                template = Velocity.getTemplate(templateFile);
            } catch( ResourceNotFoundException rnfe ) {
                System.out.println("Example : error : cannot find template " + templateFile );
            } catch( ParseErrorException pee ) {
                System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            if ( template != null)
                template.merge(context, writer);

            writer.flush();
            writer.close();
        } catch( Exception e ) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getNames() {
        ArrayList<String> list = new ArrayList<String>();

        list.add("ArrayList element 1");
        list.add("ArrayList element 2");
        list.add("ArrayList element 3");
        list.add("ArrayList element 4");

        return list;
    }

    public static void main(String[] args) {
    	// "."代表当前类路径，"/"代表项目路径
//    	String basePath = Example.class.getResource("/").getPath();
//    	InputStream in = Example.class.getResourceAsStream("/config/example.vm");
//    	String tempFile = basePath + "config/example.vm";
//    	String tempFile = "D:/DEV/eclipse/workspace/auto/bin/config/example.vm";
    	String tempFile = "./bin/config/example.vm";
        Example t = new Example(tempFile);
    }
}
