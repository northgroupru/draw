/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.mondrian.util;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import mondrian.olap.Util.PropertyList;

class DatabaseUtils {
	
	private static final DatabaseUtils INSTANCE = new DatabaseUtils();
	
	public static final DatabaseUtils getInstance() {
		return INSTANCE;
	}
	
	private java.sql.Connection olap4jConnection;
	private mondrian.olap.Connection mondrianConnection;
	
	private DatabaseUtils() {
	}
	
	public java.sql.Connection getTestDatabaseConnection(boolean init) throws SQLException {
		return java.sql.DriverManager.getConnection(getConnectionString());
	}
	
	public java.sql.Connection getOlap4jConnection() throws SQLException, ClassNotFoundException {
		if (olap4jConnection == null || olap4jConnection.isClosed()) {
			Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
			String url = "jdbc:mondrian:";
			Properties props = new Properties();
			props.setProperty("Jdbc", getConnectionString());
			props.setProperty("Catalog", getMondrianSchemaPath());
			props.setProperty("JdbcDrivers", getDriverClassName());
			olap4jConnection = DriverManager.getConnection(url, props);
		}
		return olap4jConnection;
	}

	public mondrian.olap.Connection getMondrianConnection() {
		if (mondrianConnection == null) {
			PropertyList list = new PropertyList();
			list.put("Provider", "mondrian");
			list.put("Catalog", getMondrianSchemaPath());
			list.put("Jdbc", getConnectionString());
			mondrianConnection = mondrian.olap.DriverManager.getConnection(list, null);
		}
		return mondrianConnection;
	}

	private String getDriverClassName() {
		return "org.hsqldb.jdbc.JDBCDriver";
	}
	
	private String getConnectionString() {
		return "jdbc:hsqldb:res:test";
	}

	private String getMondrianSchemaPath() {
		URL url = DatabaseUtils.class.getResource("/test.xml");
		return url.getFile();
	}
	
}
