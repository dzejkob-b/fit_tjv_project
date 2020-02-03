package cz.cvut.fit.hrabajak.semestralka.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

/**
 * zakladni rest featury
 */
public class RestBase {

	protected String getErrorJson(String message, HttpStatus st) {
		
		JSONObject json = new JSONObject();

		System.out.println("REST ERROR: " + message);

		try {
			json.put("type", "/errors/error");
			json.put("title", message);
			json.put("status", Integer.toString(st.value()));
			json.put("detail", message);

		} catch (JSONException ex) {
			// nothing ...
		}
		
		return json.toString();
	}

}
