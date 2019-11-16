package com.flights.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.ws.rs.core.Response;

import com.flights.helper.DatabaseHelper;
import com.flights.helper.EncryptHelper;
import com.flights.helper.ResponseHelper;
import com.flights.model.User;

public class UserDao {

	// Method to retrieve a user by userId from database.
	public static User getUser(int id) {
		ResultSet r = DatabaseHelper.selectQuery("select * from users where userId = ?", id + "");

		try {
			if (r != null && r.next()) {
				return extractEmployeeFromResultSet(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static User getUser(String email) {
		ResultSet r = DatabaseHelper.selectQuery("select * from users where email = ?", email);

		try {
			if (r != null && r.next()) {
				return extractEmployeeFromResultSet(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Response createSession(String email) {
		// First make the password hashed
		String user_insert_query = "UPDATE users set session_id = ? where email = ?";

		PreparedStatement ps = DatabaseHelper.getPreparedStatement(user_insert_query);

		try {
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, email);
			ps.executeUpdate();

			User u = getUser(email);

			return ResponseHelper.recordSuccessResponse(new String[] { "data" }, new Object[] { u });
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());

			return ResponseHelper.recordErrorResponse(e1.getMessage());
		}
	}

	public static Response persistUser(User u) {
		
		// First make the password hashed
		u.setPassword(EncryptHelper.getHashedPassword(u.getPassword()));

		String user_insert_query = "INSERT INTO users (name, email, role, password" + ") VALUES (?,?,?,?)";

		PreparedStatement ps = DatabaseHelper.getPreparedStatement(user_insert_query);

		try {
			ps.setString(1, u.getName().toUpperCase());
			ps.setString(2, u.getEmail().toLowerCase());
			ps.setString(3, u.getRole().toLowerCase());
			ps.setString(4, u.getPassword());
			ps.executeUpdate();

			u = getUser(u.getEmail().toLowerCase());

			return ResponseHelper.recordSuccessResponse(new String[] { "data" }, new Object[] { u });
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());

			return ResponseHelper.recordErrorResponse(e1.getMessage());
		}

	}

	public static boolean checkLogin(String email, String password) {
		password = EncryptHelper.getHashedPassword(password);

		ResultSet r = DatabaseHelper.selectQuery("select * from users where password = ? and email = ?", password, email);

		try {
			if (r != null && r.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static User validateSession(String sessionId) {
		ResultSet r = DatabaseHelper.selectQuery("select * from users where session_id = ?", sessionId);

		try {
			if (r != null && r.next()) {
				return extractEmployeeFromResultSet(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Helper method to construct user object from Database result set.
	private static User extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("userId"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setSessionId(rs.getString("session_id"));
		user.setRole(rs.getString("role"));
		user.setPassword(rs.getString("password"));
		return user;
	}

	public static void updatePassword(String email, String password) {

		// First make the password hashed
		password = EncryptHelper.getHashedPassword(password);

		String user_insert_query = "UPDATE users SET password = ? where email = ?";

		PreparedStatement ps = DatabaseHelper.getPreparedStatement(user_insert_query);

		try {
			ps.setString(1, password);
			ps.setString(2, email);
			ps.executeUpdate();

		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}

	}

	public static void invalidateSession(String sessionId) {
		// First make the password hashed
		String q = "UPDATE users set session_id = null where session_id = ?";

		PreparedStatement ps = DatabaseHelper.getPreparedStatement(q);

		try {
			ps.setString(1, sessionId);
			ps.executeUpdate();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}		
	}
}
