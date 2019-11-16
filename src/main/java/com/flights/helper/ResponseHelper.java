package com.flights.helper;

import javax.ws.rs.core.Response;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class ResponseHelper {

	public static Response recordErrorResponse(String errorMsg) {
		HashMap<String, Object> response = new HashMap<>();
		response.put("success", 0);
		response.put("reason", errorMsg);
		return Response.status(200).entity(response).build();
	}
	
	public static Response recordSuccessResponse(String keys[], Object values[]) {
		HashMap<String, Object> response = new HashMap<>();
		response.put("success", 1);
		
		for(int i=0; i<keys.length; i++) {
			response.put(keys[i], values[i]);
		}

		return Response.status(200).entity(response).build();
	}
	
	public static String serveResourceHTML(String htmlPage) {
		StringBuilder data = new StringBuilder();
		try {
			ClassLoader loader = ResponseHelper.class.getClassLoader();
			try (InputStream resourceStream = loader.getResourceAsStream(htmlPage)) {
				
				Scanner reader = new Scanner(resourceStream);
				while(reader.hasNextLine()) {
					data.append(reader.nextLine() + "\n");
				}
				reader.close();
			}
		} catch (Exception ex) {
			System.out.println("Error in reading " + htmlPage);
		}
		return data.toString();
	}
}
