/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagobi.tools.importexport.transformers;

import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.tools.importexport.ITransformer;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class TransformerFrom2_1_0To2_2_0 implements ITransformer {

	static private Logger logger = Logger.getLogger(TransformerFrom2_1_0To2_2_0.class);

	public byte[] transform(byte[] content, String pathImpTmpFolder, String archiveName) {
		logger.debug("IN");
		try {
			TransformersUtilities.decompressArchive(pathImpTmpFolder, archiveName, content);
		} catch(Exception e) {
			logger.error("Error while unzipping 2.1.0 exported archive", e);	
		}
		archiveName = archiveName.substring(0, archiveName.lastIndexOf('.'));
		changeDatabase(pathImpTmpFolder, archiveName);
		// compress archive
		try {
			content = TransformersUtilities.createExportArchive(pathImpTmpFolder, archiveName);
		} catch (Exception e) {
			logger.error("Error while creating creating the export archive", e);	
		}
		// delete tmp dir content
		File tmpDir = new File(pathImpTmpFolder);
		GeneralUtilities.deleteContentDir(tmpDir);
		logger.debug("OUT");
		return content;
	}

	private void changeDatabase(String pathImpTmpFolder, String archiveName) {
		logger.debug("IN");
		Connection conn = null;
		try {
			conn = TransformersUtilities.getConnectionToDatabase(pathImpTmpFolder, archiveName);
			//fixDomains(conn);
			addKpiTables(conn);
			conn.commit();
		} catch (Exception e) {
			logger.error("Error while changing database", e);	
		} finally {
			logger.debug("OUT");
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("Error closing connection to export database", e);
			}
		}
	}


	/*
	 * Adjust domains: in some SpagoBI 2.0.0 installations there were a hibernate dialect with name 'HQL' instead of 'HSQL'.
	 * Furthermore, hibernate dialects' value code was 'HSQL', 'MySQL', 'ORACLE' ... instead of the actual hibernate dialect java class
	 * 
	 * @param conn The jdbc connection to export database
	 * @throws Exception
	 */
	private void fixDomains(Connection conn) throws Exception {
		logger.debug("IN");
		Statement stmt = conn.createStatement();
		//String sql =  "UPDATE SBI_DOMAINS SET VALUE_CD = 'HSQL', VALUE_NM = 'HSQL' WHERE VALUE_CD = 'HQL' AND DOMAIN_CD = 'DIALECT_HIB'";
		//stmt.executeUpdate(sql);
		logger.debug("OUT");
	}

	/*
	 * Adjust Kpi Tables: in some SpagoBI 2.2.0 also Kpi Export is defined and some empty tables must be created
	 * 
	 * @param conn The jdbc connection to export database
	 * @throws Exception
	 */
	private void addKpiTables(Connection conn) throws Exception {
		logger.debug("IN");
		Statement stmt = conn.createStatement();

		String sql =  "CREATE TABLE SBI_RESOURCES (RESOURCE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" RESOURCE_TYPE_ID INTEGER NOT NULL," +
				" TABLE_NAME VARCHAR(40) default NULL," +
				" COLUMN_NAME VARCHAR(40) default NULL," +
				" RESOURCE_NAME VARCHAR(40) default NULL," +
				" RESOURCE_DESCR VARCHAR(400) default NULL" +
				" ) ";
		stmt.executeUpdate(sql);
		
		sql =  "CREATE TABLE SBI_THRESHOLD (" +
				" THRESHOLD_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" THRESHOLD_TYPE_ID INTEGER NOT NULL," +
				" name VARCHAR(127) default NULL," +
				" description VARCHAR(255) default NULL," +
				" code VARCHAR(45) default NULL" +
				" ) ";
		stmt.executeUpdate(sql);
		
		sql =  "CREATE TABLE SBI_THRESHOLD_VALUE (" +
				" id_threshold_value INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" THRESHOLD_ID INTEGER NOT NULL," +
				" SEVERITY_ID INTEGER default NULL," +
				" min_value DOUBLE default NULL," +
				" max_value DOUBLE default NULL," +
				" label VARCHAR(20) default NULL," +
				" colour VARCHAR(20) default NULL," +
				" position INTEGER default NULL)";
		stmt.executeUpdate(sql);
		
		sql =  "CREATE TABLE SBI_KPI_PERIODICITY (" +
				" id_kpi_periodicity INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" name VARCHAR(400) default NULL," +
				" months INTEGER default NULL," +
				" days INTEGER default NULL," +
				" hours INTEGER default NULL," +
				" minutes INTEGER default NULL," +
				" chron_string VARCHAR(20) default NULL," +
				" start_date TIMESTAMP NOT NULL)";
		stmt.executeUpdate(sql);
		
		sql =  "CREATE TABLE SBI_KPI (" +
				" KPI_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" id_measure_unit INTEGER default NULL," +
				" DS_ID INTEGER default NULL," +
				" THRESHOLD_ID INTEGER default NULL," +
				" id_kpi_parent INTEGER default NULL," +
				" name VARCHAR(400) NOT NULL," +
				" document_label VARCHAR(40) default NULL," +
				" code VARCHAR(40) default NULL," +
				" metric VARCHAR(1000) default NULL," +
				" description VARCHAR(1000) default NULL," +
				" weight DOUBLE default NULL," +
				" flg_is_father CHAR  default NULL," +
				" kpi_type INTEGER default NULL," +
				" metric_scale_type INTEGER default NULL," +
				" measure_type INTEGER default NULL," +
				" interpretation VARCHAR(1000) default NULL," +
				" input_attributes VARCHAR(1000) default NULL," +
				" model_reference VARCHAR(255) default NULL," +
				" target_audience VARCHAR(1000) default NULL)";
		stmt.executeUpdate(sql);
		
		
		sql =  "CREATE TABLE SBI_KPI_INSTANCE (" +
				" id_kpi_instance INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" KPI_ID INTEGER NOT NULL," +
				" THRESHOLD_ID INTEGER NOT NULL," +
				" id_measure_unit INTEGER default NULL," +
				" weight DOUBLE default NULL," +
				" BEGIN_DT TIMESTAMP NOT NULL," +
				" CHART_TYPE_ID INTEGER default NULL, " +
				"target DOUBLE default NULL) ";
		stmt.executeUpdate(sql);
			
		sql =  "CREATE TABLE SBI_KPI_INST_PERIOD (" +
				" KPI_INST_PERIOD_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" KPI_INSTANCE_ID INTEGER NOT NULL," +
				" PERIODICITY_ID INTEGER NOT NULL," +
				" DEFAULT_VALUE tinyint default NULL) ";
		stmt.executeUpdate(sql);

		sql =  "CREATE TABLE SBI_KPI_MODEL (" +
				" KPI_MODEL_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" KPI_ID INTEGER default NULL," +
				" KPI_MODEL_TYPE_ID INTEGER NOT NULL," +
				" KPI_PARENT_MODEL_ID INTEGER default NULL," +
				" KPI_MODEL_CD VARCHAR(40) default NULL," +
				" KPI_MODEL_NM VARCHAR(400) default NULL," +
				" KPI_MODEL_DESC VARCHAR(1000) default NULL) ";
		stmt.executeUpdate(sql);
		
		sql =  "CREATE TABLE SBI_KPI_MODEL_INST (" +
				" KPI_MODEL_INST INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" KPI_MODEL_INST_PARENT INTEGER default NULL," +
				" id_kpi_instance INTEGER default NULL," +
				" name VARCHAR(400) NOT NULL," +
				" description VARCHAR(1000) default NULL," +
				" label VARCHAR(100) NOT NULL," +
				" start_date TIMESTAMP default NULL," +
				" end_date TIMESTAMP default NULL," +
				" KPI_MODEL_ID INTEGER default NULL)";
		stmt.executeUpdate(sql);

		sql =  "CREATE TABLE SBI_KPI_MODEL_RESOURCES (" +
				" KPI_MODEL_RESOURCES_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" RESOURCE_ID INTEGER NOT NULL," +
				" KPI_MODEL_INST INTEGER NOT NULL)";
		stmt.executeUpdate(sql);

		sql =  "CREATE TABLE SBI_ALARM (" +
				" ALARM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" id_kpi_instance INTEGER NOT NULL," +
				" MODALITY_ID INTEGER NOT NULL," +
				" DOCUMENT_ID INTEGER default NULL," +
				" LABEL VARCHAR(50) default NULL," +
				" NAME VARCHAR(50) default NULL," +
				" DESCR VARCHAR(200) default NULL," +
				" TEXT VARCHAR(1000) NOT NULL," +
				" URL VARCHAR(20) default NULL," +
				" SINGLE_EVENT CHAR  default NULL," +
				" AUTO_DISABLED CHAR  default NULL," +
				" id_threshold_value INTEGER NOT NULL)";
		stmt.executeUpdate(sql);

		sql =  "CREATE TABLE SBI_ALARM_CONTACT (" +
				" ALARM_CONTACT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL," +
				" NAME VARCHAR(100) NOT NULL," +
				" EMAIL VARCHAR(100) default NULL," +
				" MOBILE VARCHAR(50) default NULL," +
				" RESOURCES VARCHAR(200) default NULL)";
		stmt.executeUpdate(sql);
		
		logger.debug("OUT");
	}
	

}
