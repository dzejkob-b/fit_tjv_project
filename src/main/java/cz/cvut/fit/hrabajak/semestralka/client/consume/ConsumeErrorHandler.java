package cz.cvut.fit.hrabajak.semestralka.client.consume;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStreamReader;

public class ConsumeErrorHandler implements ResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		InputStreamReader inp = new InputStreamReader(response.getBody(), "UTF-8");
		StringBuilder sb = new StringBuilder();

		int ch;

		while((ch = inp.read()) != -1) {
			sb.append((char) ch);
		}

		String message = "";

		try {
			JSONObject js = new JSONObject(sb.toString());

			message = js.getString("detail");
			
		} catch (JSONException err) {
			message = "Cannot parse response: `" + sb.toString() + "`";
		}

		message += " (HTTP: " + response.getStatusCode().toString() + ")";

		throw new ConsumeException(message);
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().isError();
	}
}
