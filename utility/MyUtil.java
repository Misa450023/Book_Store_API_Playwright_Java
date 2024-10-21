package utility;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;

public class MyUtil {

	public static JsonNode respToNode(APIResponse resp) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(resp.body());

	}

	public static String toJsonString(Object object) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

}
