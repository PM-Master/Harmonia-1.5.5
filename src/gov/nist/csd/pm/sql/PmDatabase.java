package gov.nist.csd.pm.sql;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class PmDatabase {
	/** The MySQL database connector. */
	public ComboPooledDataSource ds;
	
	public PmDatabase() throws PropertyVetoException, IOException {
		System.out.println(System.getProperty("user.dir"));
		ds = new ComboPooledDataSource();
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.setDialogTitle("Select property file for database connection");
//		fileChooser.setCurrentDirectory(new File("../conf/DB_properties"));
		int returnValue = 0;// fileChooser.showOpenDialog(null);
		if (returnValue != JFileChooser.CANCEL_OPTION) {
			Properties props = new Properties();
			String file = "../conf/DB_properties/db_props.properties";// fileChooser.getSelectedFile().getPath();
			// InputStream inputStream =
			// getClass().getClassLoader().getResourceAsStream(file);
			InputStream inputStream = new FileInputStream(new File(file));
			if (inputStream != null) {
				props.load(inputStream);

				ds.setDriverClass(props.getProperty("driver"));
				ds.setJdbcUrl(props.getProperty("url"));
				ds.setUser(props.getProperty("username"));
				ds.setPassword(props.getProperty("password"));

				ds.setMinPoolSize(3);
				ds.setAcquireIncrement(1);
				ds.setMaxPoolSize(30);
				ds.setMaxStatements(20);
			}
		}
	}

	public Connection getConnection() throws Exception /*, ClassNotFoundException */ {
		/*System.out.println("Busy connections: " + ds.getNumBusyConnections() +
				", Total connections: " + ds.getNumConnections() +
				", Unclosed connections: " + ds.getNumUnclosedOrphanedConnections() +
				", Idle connections: " + ds.getNumIdleConnections());*/
		return ds.getConnection();
	}
	
	public void closeConnection(){
		
	}
//		Connection connection = null;
//		
//		try {
//		  Class.forName("com.mysql.jdbc.Driver");
//	      //System.out.println("Connecting to database...");
//	      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root", "root");
//			if (connection != null) {
//				//System.out.println("connected to db");
//				connection.setAutoCommit(true); // No need to manually commit
//				
//			} else {
//				JOptionPane.showMessageDialog(null, "Could not establish database connection...");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Exception("Could not get database connection");
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		}
//		//System.out.println("CONNECTION{");
//		return connection;

//	}
}
