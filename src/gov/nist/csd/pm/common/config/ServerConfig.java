package gov.nist.csd.pm.common.config;

import gov.nist.csd.pm.server.PmEngine;
//import gov.nist.csd.pm.server.dao.ActiveDirectory.ADGraph.ADGraphManager;
import gov.nist.csd.pm.server.dao.ActiveDirectory.ActiveDirectoryDAO;
import gov.nist.csd.pm.sql.PmDatabase;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;

import javax.naming.directory.DirContext;

public class ServerConfig {

	public static boolean debugFlag = true;
	public static boolean auditDebug = false;
	public static String datastore = "SQL";
	public static PmEngine myEngine;
	public static PmDatabase pmDB;
	
	public static DirContext ctx;
	public static String sThisDomain;
	
	//AD DAO
	public static ActiveDirectoryDAO ADDAO;
	public static Connection conn;
	// MySQL DAOs

    public static PmDatabase getPmDB() throws PropertyVetoException, IOException{
        if(pmDB == null){
            System.out.println("new db connection");
            pmDB = new PmDatabase();
        }
        return pmDB;
    }

}
