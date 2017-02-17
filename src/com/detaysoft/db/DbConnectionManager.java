package com.detaysoft.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnectionManager {

	public static Connection connection=null;

	public static Connection getConnection()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String username="prd";
			String password="prd";
			String url="jdbc:MySQL://192.168.10.96:3306/openfire";
			connection=DriverManager.getConnection(url,username,password);
			System.out.println("veri tabani baglantisi basarili.");
			
			
		} catch (Exception e) {
			System.out.println("veri taban�na baglanti saglanilamadi. "+e);
		}
		
		return connection;
	}
}
