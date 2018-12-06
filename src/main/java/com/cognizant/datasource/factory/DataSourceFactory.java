package com.cognizant.datasource.factory;

import com.cognizant.datasource.FileDataSource;
import com.cognizant.datasource.IDataSource;
import com.cognizant.datasource.JDBCDataSource;

public class DataSourceFactory {
	private static DataSourceFactory factory = new DataSourceFactory();
	
	private FileDataSource fileDataSource = null;
	private JDBCDataSource jdbcDataSource = null;
	
	public static synchronized DataSourceFactory getFactory() {
		if(factory == null) {
			factory = new DataSourceFactory();
		}
		return factory;
	}
	
	public IDataSource getDataSource(String type) {
		if(type.equalsIgnoreCase("File")) {
			if(fileDataSource == null) {
				fileDataSource = new FileDataSource();
			}
			return fileDataSource;
//		} 
//		else if (type.equalsIgnoreCase("JDBC")) {
//			if(jdbcDataSource == null) {
//				jdbcDataSource = new JDBCDataSource();
//			}
//			return jdbcDataSource;
		} else {
			return null;
		}
	}
}