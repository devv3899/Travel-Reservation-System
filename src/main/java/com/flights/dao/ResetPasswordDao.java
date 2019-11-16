package com.flights.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.flights.helper.DatabaseHelper;
import com.flights.model.ResetPassword;

public class ResetPasswordDao {

	// Method to retrieve a user by userId from database.
	public static String createToken(String email) {
		String token = UUID.randomUUID().toString();
		
		String query = "INSERT INTO reset_password (email, token) VALUES (?,?)";
		PreparedStatement ps = DatabaseHelper.getPreparedStatement(query);

		try {
			ps.setString(1, email);
			ps.setString(2, token);
			ps.executeUpdate();

			return token;
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
			return "";
		}

	}

	public static ResetPassword validateToken(String token) {
		ResultSet r = DatabaseHelper.selectQuery("select * from reset_password where token = ?", token);

		try {
			if (r != null && r.next()) {
				ResetPassword rp = extractResetPasswordFromResultSet(r);
				if(rp.getToken().equals(token)) {
					return rp;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void removeTokens(String email) {

		String query = "DELETE FROM reset_password  where email = ?";
		PreparedStatement ps = DatabaseHelper.getPreparedStatement(query);

		try {
			ps.setString(1, email);
			ps.executeUpdate();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}
	}

	// Helper method to construct reset password object from Database result set.
	private static ResetPassword extractResetPasswordFromResultSet(ResultSet rs) throws SQLException {
		ResetPassword r = new ResetPassword();
		r.setEmail(rs.getString("email"));
		r.setToken(rs.getString("token"));
		r.setTime(rs.getTime("creation_time"));
		return r;
	}
}
