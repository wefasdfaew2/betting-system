package com.org.ui;

import javax.security.auth.login.Configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

public class UIProperty {
	XMLConfiguration  config ;
	public UIProperty(String config_filename) throws ConfigurationException{
		this.config = new XMLConfiguration (config_filename);
	}
	public XMLConfiguration getConfig() {
		return config;
	}
	public void setConfig(XMLConfiguration config) {
		this.config = config;
	}
	
}
