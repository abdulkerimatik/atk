/*
 * Copyright (C) 2015 DISID CORPORATION S.L.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see `<http://www.gnu.org/licenses/>`.
 */
package com.atk.mail.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.RandomValuePropertySource;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * = Application-specific externalised configuration
 * 
 * An {@link EnvironmentPostProcessor} to let each application to use its own application-specific properties in a
 * multi-application server.
 * 
 * The application-specific properties outside the packaged jar/war will have a precedence immediately lower than the
 * `RandomValuePropertySource` and immediately higher than the application `ConfigurationPropertySources`.
 * 
 * The default
 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config[Spring
 * Boot PropertySource order] will be modified as follows:
 * 
 * . Command line arguments. . Properties from `SPRING_APPLICATION_JSON` . JNDI attributes from `java:comp/env` . Java
 * System properties (`System.getProperties()`) . OS environment variables . A RandomValuePropertySource that only has
 * properties in random. . *Application-specific properties outside the packaged jar/war* . Profile-specific application
 * properties outside of your packaged jar (application-{profile}.properties and YAML variants) . Profile-specific
 * application properties packaged inside your jar (application-{profile}.properties and YAML variants) . Application
 * properties outside of your packaged jar (application.properties and YAML variants) . Application properties packaged
 * inside your jar (application.properties and YAML variants) . etc.
 *
 * @see https://github.com/spring-projects/spring-boot/issues/4800
 * @author Enrique Ruiz
 */
public class EnvPostProcessor implements EnvironmentPostProcessor, Ordered {

	/** The ProperySource name */
	public static final String APPLICATION_SPECIFIC_CONFIGURATION_PROPERTY_SOURCE_NAME = "applicationSpecificConfigurationProperties";

	/** The default order for the processor */
	public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 15;

	private static final Logger LOG = LoggerFactory.getLogger(EnvPostProcessor.class);

	private int order = DEFAULT_ORDER;

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * {@inheritDoc} TBC
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// Load default properties
		Properties envProps = loadProperties(new File("config"), "default.properties");
		PropertiesPropertySource billingPropSource = new PropertiesPropertySource("envAppProperties", envProps);
		addApplicationPropertySource(environment, billingPropSource);

		// If there isn't app name setted the app config location cannot be
		// calculated.
		// containsProperty method checks if the environment contains the app
		// name, or the underscore / uppercase variation
		// SPRING_APPLICATION_NAME (only at System PropertySource)
		if (!environment.containsProperty("spring.application.name")) {
			return;
		}

		// If there isn't app config base location setted the app config
		// location cannot be calculated.
		// containsProperty method checks if the environment contains the app
		// config base location, or the underscore / uppercase variation
		// SPRING_APPLICATION_CONFIG_LOCATION (only at System PropertySource)
		if (!environment.containsProperty("spring.application.config.location")) {
			return;
		}

		String appName = environment.getProperty("spring.application.name");
		System.setProperty("spring.application.name", appName);

		// The default configuration file name is "application.properties",
		// check if it was changed by specifying the "spring.config.name"
		// environment property.
		String appCfgName = "application.properties";
		if (environment.containsProperty("spring.config.name")) {
			appCfgName = environment.getProperty("spring.config.name") + ".properties";
		} else {
			appCfgName = environment.getProperty("spring.application.name") + ".properties";
		}

		String appCfgLocation = environment.getProperty("spring.application.config.location");
		File location = new File(appCfgLocation);

		// Populate the Properties instance
		Properties appProperties = loadProperties(location, appCfgName);
		PropertiesPropertySource modulePropSource = new PropertiesPropertySource(appCfgName + "-config-properties",
				appProperties);

		// Add the application-specific Properties to the application
		// Environment
		addApplicationPropertySource(environment, modulePropSource);
	}

	/**
	 * Load the application-specific properties.
	 * 
	 * @param parent
	 *            The parent file that denotes a directory
	 * @param filename
	 *            The child pathname that denotes the file in the given parent directory
	 * @return the property list (key and element pairs) read from the given file.
	 */
	private Properties loadProperties(File parent, String filename) {
		if (StringUtils.isEmpty(filename)) {
			LOG.error("The given filename is empty.");
		} else if (!parent.isDirectory()) {
			LOG.error("The given parent directory ".concat(parent.getAbsolutePath()).concat(" is not a directory."));
		} else {

			File propertyFile = new File(parent, filename);
			if (propertyFile.exists() && propertyFile.isFile()) {
				FileSystemResource resource = new FileSystemResource(propertyFile);
				Properties properties;
				try {
					properties = PropertiesLoaderUtils.loadProperties(resource);
					return properties;
				} catch (IOException ex) {
					LOG.error("Unable to load " + filename, ex);
					System.out.println("Unable to load " + filename + ". " + ex.getMessage());
				}
			} else {
				LOG.error("Unable to load " + filename + " , resource not exists or not file");
				System.out.println("Unable to load " + filename + " , resource not exists or not file");
			}
		}
		return new Properties();
	}

	/**
	 * Add the given PropertySource to the application Environment with a precedence immediately lower than the
	 * `RandomValuePropertySource` and immediately higher than the application `ConfigurationPropertySources`.
	 * 
	 * The default
	 * http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config[Spring
	 * Boot PropertySource order] will be modified as follows:
	 * 
	 * . Command line arguments. . Properties from `SPRING_APPLICATION_JSON` . JNDI attributes from `java:comp/env` .
	 * Java System properties (`System.getProperties()`) . OS environment variables . A RandomValuePropertySource that
	 * only has properties in random. . *Application-specific properties outside the packaged jar/war* .
	 * Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML
	 * variants) . Profile-specific application properties packaged inside your jar (application-{profile}.properties
	 * and YAML variants) . Application properties outside of your packaged jar (application.properties and YAML
	 * variants) . Application properties packaged inside your jar (application.properties and YAML variants) . etc.
	 * 
	 * @param environment
	 * @param source
	 */
	private void addApplicationPropertySource(ConfigurableEnvironment environment, PropertySource<?> source) {
		MutablePropertySources sources = environment.getPropertySources();

		// Find the right PropertySource after which ot add the
		// application-specific properties
		String name = findPropertySource(sources);
		if (!StringUtils.isEmpty(name) && sources.contains(name)) {
			sources.addAfter(name, source);
		} else {
			// If no previous PropertySources found add the
			// application-specific at the 1st place (highest precedence)
			sources.addFirst(source);
		}
	}

	/**
	 * Get the name of the lower PropertySource to be able to add a new PropertySource immediately higher than the
	 * application `ConfigurationPropertySources`.
	 * <p/>
	 * The default <a href=
	 * "http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config[Spring
	 * Boot PropertySource order] will be modified as follows:
	 * 
	 * . Command line arguments. . Properties from `SPRING_APPLICATION_JSON` . JNDI attributes from `java:comp/env` .
	 * Java System properties (`System.getProperties()`) . OS environment variables . A RandomValuePropertySource that
	 * only has properties in random. . *Application-specific properties outside the packaged jar/war* .
	 * Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML
	 * variants) . Profile-specific application properties packaged inside your jar (application-{profile}.properties
	 * and YAML variants) . Application properties outside of your packaged jar (application.properties and YAML
	 * variants) . Application properties packaged inside your jar (application.properties and YAML variants) . etc.
	 * 
	 * @param sources
	 * @return The name of the {@code PropertySource} after which to add the application-specific properties, or null if
	 *         any of the previous PropertySources found.
	 */
	private String findPropertySource(MutablePropertySources sources) {
		if (sources.contains(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME)) {
			return RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME;
		} else if (sources.contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
			return StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;
		} else if (sources.contains(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)) {
			return StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
		} else if (sources.contains(StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME)) {
			return StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME;
		} else if (sources.contains(StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME)) {
			return StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME;
		} else if (sources.contains(StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME)) {
			return StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME;
		}
		return null;
	}
}