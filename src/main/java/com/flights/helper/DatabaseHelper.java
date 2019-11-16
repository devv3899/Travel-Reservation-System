package com.flights.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
	
	static Connection con;

	private static final String DB_URL = "cs336db.cevjd6qfgzlu.us-east-2.rds.amazonaws.com"; 
	private static final int DB_PORT = 3306;   
	private static final String DB_NAME = "Flights";   
	private static final String DB_USERNAME = "cs336";   
	private static final String DB_PASSWORD = "Dev12345678";   
	
	// We keep the connection open, and using that itself, we execute our queries.
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + DB_URL + ":" + DB_PORT
					+ "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public static ResultSet selectQuery(String query) {
		ResultSet rs = null;
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet selectQuery(String query, String ...params) {
		ResultSet rs = null;
		try {
			PreparedStatement stmt = con.prepareStatement(query);
			for(int i=0; i<params.length; i++) {
				stmt.setString(i+1, params[i]);
			}
			
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static PreparedStatement getPreparedStatement(String query) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	public static int executeUpdateViaPreparedStatement(PreparedStatement stmt) {
		try {
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static ResultSet executeQueryViaPreparedStatement(PreparedStatement stmt) {
		try {
			return stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
