package com.flights.resource;

import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.flights.dao.ResetPasswordDao;
import com.flights.dao.UserDao;
import com.flights.helper.EmailHelper;
import com.flights.helper.EncryptHelper;
import com.flights.helper.ResponseHelper;
import com.flights.model.ResetPassword;
import com.flights.model.User;

@Path("/")
public class UserResource {

	@POST
	@Path("/signupUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(User user) {

		if (user == null) {
			return ResponseHelper.recordErrorResponse("Invalid User Data");
		}

		User u = UserDao.getUser(user.getUserId());
		if (u != null) {
			return ResponseHelper.recordErrorResponse("User already exists with user Id");
		}

		u = UserDao.getUser(user.getEmail());
		if (u != null) {
			return ResponseHelper.recordErrorResponse("User already exists with provided email address");
		}

		// Everything is clear. Insert the User.
		return UserDao.persistUser(user);
	}

	// We are using POST request, So that data comes in the body.
	@POST
	@Path("/loginUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(HashMap<String, String> userData) {
		if (!userData.containsKey("email") || !userData.containsKey("password")) {
			return ResponseHelper.recordErrorResponse("Please provide username and password.");
		}
		String email = userData.get("email").toLowerCase();
		String password = userData.get("password");

		User u = UserDao.getUser(email);
		if (u == null) {
			return ResponseHelper.recordErrorResponse("No user is present with given email address.");
		}

		if (EncryptHelper.getHashedPassword(password).equals(u.getPassword())) {
			return UserDao.createSession(email);
		}

		return ResponseHelper.recordErrorResponse("Invalid password.");
	}
	
	// We are using POST request, So that data comes in the body.
	@POST
	@Path("/logOffUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logOffUser(HashMap<String, String> userData) {
		if (!userData.containsKey("sessionId")) {
			return ResponseHelper.recordErrorResponse("Please provide sessionId.");
		}
		String sessionId = userData.get("sessionId");

		User u = UserDao.validateSession(sessionId);
		if(u == null) {
			return ResponseHelper.recordErrorResponse("Invalid sessionId.");
		}
		
		UserDao.invalidateSession(sessionId);
		return ResponseHelper.recordSuccessResponse(new String[] { "data" }, new Object[] { "Logged off successfully." });
	}

	// We are using POST request, So that data comes in the body.
	@POST
	@Path("/resetPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(@Context UriInfo uriInfo,
			HashMap<String, String> userData) {

		String email = userData.get("email").toLowerCase();
		User u = UserDao.getUser(email);
		if (u == null) {
			return ResponseHelper.recordErrorResponse("No user is present with given email address.");
		}
		
		// add a new Token to the database.
		String token = ResetPasswordDao.createToken(email);
		
		String host = uriInfo.getAbsolutePath().toString();
		host = host.substring(0, host.lastIndexOf('/'));

		String subject = "Reset Password for Flights Portal";
		String from = "devnandol@gmail.com";
		String to = email;
		String body = "Please click ";
		body += "<a href='" + host + "/Prod/resetPassword?token=" + token + 
				"'>here</a>";
		body += " to reset your password.";

		try {
			EmailHelper.sendHtmlEmail(subject, body, "", from, to);
		} catch (MessagingException e) {
			System.out.println("Could not send email for reseting password on " + email);
			return ResponseHelper.recordErrorResponse("Error while sending password reset email.");
		}

		return ResponseHelper.recordSuccessResponse(new String[] { "data" }, new Object[] { "We have sent mail to your email address." });
	}


	@GET
	@Path("/resetPassword")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.WILDCARD)
	public Response resetPasswordPage(@QueryParam("token") String token) {
		
		if(token == null || token.isEmpty()) {
			return Response.status(200).entity("Invalid Token.").build();
		}
		
		ResetPassword rp = ResetPasswordDao.validateToken(token);
		if(rp == null) {
			return Response.status(200).entity("Invalid Token.").build();
		}
		
		if(Time.valueOf(LocalTime.now()).getTime() - rp.getTime().getTime() > 120*60*1000) {
			return Response.status(200).entity("Token is older than 2 hours. Please reset the password again.").build();
		}
		
		// Else, We can successfully now reset the password.
		String page = ResponseHelper.serveResourceHTML("changePassword.html");
		page = page.replace("$EMAIL_TOKEN", token);

		return Response.status(200).entity(page).build();
	}


	// We are using POST request, So that data comes in the body.
	@POST
	@Path("/changePassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(HashMap<String, String> userData) {

		String token = userData.get("token");
		String password = userData.get("password");
			
		if(password.length() < 6) {
			return ResponseHelper.recordErrorResponse("Invalid Password. Shuld be atleast 6 characters.");
		}
		ResetPassword rp = ResetPasswordDao.validateToken(token);
		if(rp == null) {
			return ResponseHelper.recordErrorResponse("Invalid Token.");
		}
		
		UserDao.updatePassword(rp.getEmail(), password);
		
		ResetPasswordDao.removeTokens(rp.getEmail());

		return ResponseHelper.recordSuccessResponse(new String[] { "data" }, new Object[] { "Password Changed Successfully." });
	}
	
	
	// to Serve Static HTML pages:
	@Path("/login")
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.WILDCARD)
	public Response login() {
		return Response.status(200).entity(ResponseHelper.serveResourceHTML("login.html")).build();
	}

	@Path("/signUp")
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.WILDCARD)
	public Response signUp() {
		return Response.status(200).entity(ResponseHelper.serveResourceHTML("signUp.html")).build();
	}

	@Path("/forgotPassword")
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.WILDCARD)
	public Response forgotPassword() {
		return Response.status(200).entity(ResponseHelper.serveResourceHTML("forgotPassword.html")).build();
	}


	@GET
	@Path("/dashboard")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.WILDCARD)
	public Response dashBoard(@QueryParam("sessionId") String sessionId) {
		
		User u = UserDao.validateSession(sessionId);
		if(u == null) {
			return Response.status(200).entity("Invalid SessionId.").build();
		}
		
		// Else, We can successfully onboard user.
		String page = ResponseHelper.serveResourceHTML("dashboard.html");
		page = page.replace("${USER_NAME}", u.getName());

		return Response.status(200).entity(page).build();
	}
}
