package com.pbkj.crius;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CodeGenerator {
	// 包名
	private static final String packageName = "cn.codemao.botmao.admin";
	private static final String author = "gzq";
	private static final String db_url = "jdbc:mysql://rm-bp1udh67050zd1n0b115.mysql.rds.aliyuncs.com/botmao?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&allowMultiQueries=true&serverTimezone=CTT";
	private static final String db_driver = "com.mysql.jdbc.Driver";
	private static final String db_userName = "chenpeng3";
	private static final String db_password = "LW1Kfae0HijbSh8iogs79uhnFHz6MZyP";
	private static final String entityPackageName = "domain.entity";
	private static final String entityDTOPackageName = "entity.dto";
	private static final String mapperPackageName = "mapper";
	private static final String controllerPackageName = "controller";
	// 代码生成器
	private static final AutoGenerator mpg = new AutoGenerator();
	private static final InjectionConfig cfg = new InjectionConfig() {
        @Override
        public void initMap() {}
        @Override
        public Map<String,Object> prepareObjectMap(Map<String,Object> objectMap) {
            objectMap.put("mapperLabel", "#{");
            objectMap.put("dtoName", objectMap.get("entity") + "DTO");
			return objectMap;
        };
    };
    public static void main(String[] args) {
        // 1.数据源配置
        dataSourceConfig(mpg);
        // 2.配置引擎模板
        templateConfig(mpg);
        // 3.自定义模板配置
        templateFileConfig(mpg);
        // 4.策略配置
        strategyConfig(mpg);
        // 5.生成代码
        mpg.execute();
    }
    
    /**
     * 	1.数据源配置
     */
	private static void dataSourceConfig(AutoGenerator mpg) {
		DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(db_url);
        dsc.setDriverName(db_driver);
        dsc.setUsername(db_userName);
        dsc.setPassword(db_password);
        mpg.setDataSource(dsc);
	}
	
    /**
     * 	2.模板配置
     */
	private static void templateConfig(AutoGenerator mpg) {
		TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
	};
	
    /**
     * 3.自定义模板配置
     */
	private static void templateFileConfig(AutoGenerator mpg) {
        String projectPath = globalConfig(mpg);
        List<FileOutConfig> focList = new ArrayList<>();
        // Mapper.xml 配置模板
        mapperTemplateFileConfig(projectPath, focList);
        // dto 配置模板
        dtoTemplateFileConfig(projectPath, focList);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
	}	

	/**
     *	4.策略配置
     */
	private static void strategyConfig(AutoGenerator mpg) {
        // 包配置
        PackageConfig pc = packageConfig(mpg);
		StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setInclude(scanner("表名,多个英文逗号分割").split(","));
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
	}
	
    /**
     * 	全局配置
     */
	private static String globalConfig(AutoGenerator mpg) {
		GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setActiveRecord(false);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        // 设置时间格式为 java.util.date
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);
		return projectPath;
	}
	
	/**
	 * Mapper.xml 配置模板
	 */
	private static void mapperTemplateFileConfig(String projectPath, List<FileOutConfig> focList) {
        // freemarker模板引擎配置文件后缀是 ftl
        String templatePath = "/templates/mapper.xml.ftl";
		focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
	}

	/**
	 * dto 配置模板
	 */
	private static void dtoTemplateFileConfig(String projectPath, List<FileOutConfig> focList) {
        // dto 模板引擎配置文件后缀是 ftl
        String templatePath = "/templates/entityDto.java.ftl";
        String packageNameFileFloder = (packageName + "." +entityDTOPackageName).replaceAll("\\.", "/");
		focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + packageNameFileFloder + "/" + tableInfo.getEntityName() + "DTO.java";
            }
        });
	}
	
	/**
	 * 	包配置
	 */
	private static PackageConfig packageConfig(AutoGenerator mpg) {
		PackageConfig pc = new PackageConfig();
        pc.setParent(packageName);
        pc.setController(controllerPackageName);
        pc.setMapper(mapperPackageName);
        pc.setEntity(entityPackageName);
        mpg.setPackageInfo(pc);
		return pc;
	}
    
	public static String scanner(String tip) {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + ":");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("表名不能为空");
    }
	
}