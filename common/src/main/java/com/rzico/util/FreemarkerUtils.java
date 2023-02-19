/*
 * Copyright 2005-2013 rzico-boot. All rights reserved.
 */
package com.rzico.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import org.springframework.context.ApplicationContext;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * Utils - Freemarker
 * 
 * @author rsico Team
 * @version 3.0
 */
@SuppressWarnings("unchecked")
public final class FreemarkerUtils {

	/** ConvertUtilsBean */
	private static final ConvertUtilsBean convertUtils;
	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	static {
		convertUtils = new ConvertUtilsBean() {
			@Override
			public String convert(Object value) {
				if (value != null) {
					Class<?> type = value.getClass();
					if (type.isEnum() && super.lookup(type) == null) {
						super.register(new EnumConverter(type), type);
					} else if (type.isArray() && type.getComponentType().isEnum()) {
						if (super.lookup(type) == null) {
							ArrayConverter arrayConverter = new ArrayConverter(type, new EnumConverter(type.getComponentType()), 0);
							arrayConverter.setOnlyFirstToString(false);
							super.register(arrayConverter, type);
						}
						Converter converter = super.lookup(type);
						return ((String) converter.convert(String.class, value));
					}
				}
				return super.convert(value);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum() && super.lookup(clazz) == null) {
					super.register(new EnumConverter(clazz), clazz);
				}
				return super.convert(value, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(String[] values, Class clazz) {
				if (clazz.isArray() && clazz.getComponentType().isEnum() && super.lookup(clazz.getComponentType()) == null) {
					super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
				}
				return super.convert(values, clazz);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Object value, Class targetType) {
				if (super.lookup(targetType) == null) {
					if (targetType.isEnum()) {
						super.register(new EnumConverter(targetType), targetType);
					} else if (targetType.isArray() && targetType.getComponentType().isEnum()) {
						ArrayConverter arrayConverter = new ArrayConverter(targetType, new EnumConverter(targetType.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register(arrayConverter, targetType);
					}
				}
				return super.convert(value, targetType);
			}
		};

		DateConverter dateConverter = new DateConverter();
		dateConverter.setPatterns(DATE_PATTERNS);
		convertUtils.register(dateConverter, Date.class);
	}

	/**
	 * 不可实例化
	 */
	private FreemarkerUtils() {
	}

	public static FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
		FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
		factory.setTemplateLoaderPath("classpath:/template/");
		factory.setDefaultEncoding("UTF-8");
		factory.setPreferFileSystemAccess(false);  //设置为false，spring才能以相对路径来访问我们的模板文件夹
		FreeMarkerConfigurer result = new FreeMarkerConfigurer();
		freemarker.template.Configuration configuration = factory.createConfiguration();
		configuration.setTagSyntax(0);
		configuration.setClassicCompatible(true);
		result.setConfiguration(configuration);
		Properties settings = new Properties();
		settings.put("template_update_delay", "0");
		settings.put("default_encoding", "UTF-8");
		settings.put("number_format", "0.######");
		settings.put("classic_compatible", true);
		settings.put("template_exception_handler", "ignore");
		result.setFreemarkerSettings(settings);
		return result;
	}

	/**
	 * 解析字符串模板
	 * 
	 * @param template
	 *            字符串模板
	 * @param model
	 *            数据
	 * @return 解析后内容
	 */
	public static String process(String template, Map<String, ?> model) throws IOException, TemplateException {
		Configuration configuration = null;
		FreeMarkerConfigurer freeMarkerConfigurer = freemarkerConfig();
		if (freeMarkerConfigurer != null) {
			configuration = freeMarkerConfigurer.getConfiguration();
		}

		return process(template, model, configuration);

	}

	/**
	 * 解析字符串模板
	 * 
	 * @param template
	 *            字符串模板
	 * @param model
	 *            数据
	 * @param configuration
	 *            配置
	 * @return 解析后内容
	 */
	public static String process(String template, Map<String, ?> model, Configuration configuration) throws IOException, TemplateException {
		if (template == null) {
			return null;
		}
		if (configuration == null) {
			configuration = new Configuration(Configuration.VERSION_2_3_28);
		}
		StringWriter out = new StringWriter();
		new Template("template", new StringReader(template), configuration).process(model, out);
		return out.toString();
	}

	/**
	 * 获取参数
	 * 
	 * @param name
	 *            名称
	 * @param type
	 *            类型
	 * @param params
	 *            参数
	 * @return 参数,若不存在则返回null
	 */
	public static <T> T getParameter(String name, Class<T> type, Map<String, TemplateModel> params) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(type);
		Assert.notNull(params);
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		Object value = DeepUnwrap.unwrap(templateModel);
		return (T) convertUtils.convert(value, type);
	}

	/**
	 * 获取变量
	 * 
	 * @param name
	 *            名称
	 * @param env
	 *            Environment
	 * @return 变量
	 */
	public static TemplateModel getVariable(String name, Environment env) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		return env.getVariable(name);
	}

	/**
	 * 设置变量
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            变量值
	 * @param env
	 *            Environment
	 */
	public static void setVariable(String name, Object value, Environment env) throws TemplateException {
		Assert.hasText(name);
		Assert.notNull(env);
		if (value instanceof TemplateModel) {
			env.setVariable(name, (TemplateModel) value);
		} else {
			env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
		}
	}

	/**
	 * 设置变量
	 * 
	 * @param variables
	 *            变量
	 * @param env
	 *            Environment
	 */
	public static void setVariables(Map<String, Object> variables, Environment env) throws TemplateException {
		Assert.notNull(variables);
		Assert.notNull(env);
		for (Entry<String, Object> entry : variables.entrySet()) {
			String name = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof TemplateModel) {
				env.setVariable(name, (TemplateModel) value);
			} else {
				env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
			}
		}
	}

}