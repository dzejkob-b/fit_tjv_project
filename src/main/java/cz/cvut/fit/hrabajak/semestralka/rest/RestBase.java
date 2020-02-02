package cz.cvut.fit.hrabajak.semestralka.rest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * zakladni rest featury
 */
public class RestBase {

	protected String getErrorJson(String message) {
		
		JSONObject json = new JSONObject();

		System.out.println("REST ERROR: " + message);

		try {
			json.put("error", message);

		} catch (JSONException ex) {
			// nothing ...
		}
		
		return json.toString();
	}

}
