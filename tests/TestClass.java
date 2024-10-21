package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.HttpHeader;

import junit.framework.Assert;
import pojos.Order;
import pojos.UpdateOrder;
import pojos.User;
import rest.Context;
import rest.EContentType;
import rest.Methods;
import utility.MyUtil;

public class TestClass {

	String baseURL = "https://simple-books-api.glitch.me";
	String token;
	String orderId;

	List<String> booksIDs = new ArrayList<>();

	@AfterTest
	public void tearDown() {

		Methods.playwrightClose();
	}

	@Test(priority = 0)
	public void test00_GET_STATUS() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/status";
		APIResponse resp = Methods.GET(context);

		JsonNode node = MyUtil.respToNode(resp);

		Assert.assertEquals(node.get("status").asText(), "OK");
		Assert.assertEquals(resp.status(), 200);
		Assert.assertEquals(resp.statusText(), "OK");

	}

	@Test(priority = 1)
	public void test01_GET_BOOKS() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/books";
		APIResponse resp = Methods.GET(context);

		JsonNode node = MyUtil.respToNode(resp);

		List<String> names = node.findValuesAsText("name");
		booksIDs = node.findValuesAsText("id");

		Assert.assertEquals(resp.status(), 200);
		Assert.assertEquals(resp.statusText(), "OK");
		Assert.assertTrue(names.stream().allMatch(el -> el != null));
		Assert.assertTrue(names.stream().allMatch(el -> !el.isEmpty()));
	}

	@Test(priority = 2)
	public void test02_GET_BOOK() throws IOException {

		for (int a = 1; a <= booksIDs.size(); a++) {
			Context context = new Context();
			context.url = baseURL + "/books/" + String.valueOf(a);
			APIResponse resp = Methods.GET(context);

			JsonNode node = MyUtil.respToNode(resp);

			Assert.assertEquals(resp.status(), 200);
			Assert.assertTrue(!node.get("author").asText().isEmpty());
			Assert.assertTrue(node.get("price").asDouble() > 1.0);
		}
	}

	@Test(priority = 3)
	public void test03_GET_BOOK_WITH_QUERIE() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/books";
		context.queryParams.put("type", "fiction");
		APIResponse resp = Methods.GET(context);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(resp.body());

		Assert.assertEquals(resp.status(), 200);
		Assert.assertEquals(resp.statusText(), "OK");
		Assert.assertTrue(node.findValuesAsText("type").stream().allMatch(el -> el.equals("fiction")));

	}

	@Test(priority = 4)
	public void test05_GET_TOKEN() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/api-clients/";
		context.body = MyUtil
				.toJsonString(new User("Zaim", "zaim" + String.valueOf(System.currentTimeMillis() + "@gmail.com")));
		context.headerParams.put("content-type", EContentType.JSON.getContentType());

		APIResponse resp = Methods.POST(context);

		JsonNode node = MyUtil.respToNode(resp);
		Map<String, String> headers = resp.headers();

		Assert.assertEquals(resp.status(), 201);
		Assert.assertTrue(!node.get("accessToken").asText().isEmpty());
		Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");
		Assert.assertEquals(headers.get("connection"), "close");

		token = node.get("accessToken").asText();

	}

	@Test(priority = 5)
	public void test06_ORDER_A_BOOK() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders";
		context.body = MyUtil.toJsonString(new Order(1, "kazan"));
		context.headerParams.put("content-type", EContentType.JSON.getContentType());
		context.headerParams.put("Authorization", "Bearer " + token);

		APIResponse resp = Methods.POST(context);
		JsonNode node = MyUtil.respToNode(resp);

		Assert.assertEquals(resp.status(), 201);
		Assert.assertEquals(node.get("created").asText(), "true");
		Assert.assertNotNull(node.get("orderId").asText());

		orderId = node.get("orderId").asText();
	}

	@Test(priority = 6)
	public void test07_GET_ORDER() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders/" + orderId;
		context.headerParams.put("Authorization", "Bearer " + token);

		APIResponse resp = Methods.GET(context);
		JsonNode node = MyUtil.respToNode(resp);

		Assert.assertEquals(resp.status(), 200);
		Assert.assertEquals(node.get("bookId").asText(), "1");
		Assert.assertEquals(node.get("customerName").asText(), "kazan");
		Assert.assertNotNull(node.get("createdBy").asText());

	}

	@Test(priority = 7)
	public void test08_UPDATE_ORDER() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders/" + orderId;
		context.headerParams.put("Authorization", "Bearer " + token);
		context.headerParams.put("content-type", EContentType.JSON.getContentType());
		context.body = MyUtil.toJsonString(new UpdateOrder("jamak"));

		APIResponse resp = Methods.PATCH(context);

		Assert.assertEquals(resp.status(), 204);
	}

	@Test(priority = 8)
	public void test07_GET_UPDATED_ORDER() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders/" + orderId;
		context.headerParams.put("Authorization", "Bearer " + token);

		APIResponse resp = Methods.GET(context);
		JsonNode node = MyUtil.respToNode(resp);

		Assert.assertEquals(resp.status(), 200);
		Assert.assertEquals(node.get("bookId").asText(), "1");
		Assert.assertEquals(node.get("customerName").asText(), "jamak");
		Assert.assertNotNull(node.get("createdBy").asText());
	}

	@Test(priority = 9)
	public void test07_DELETE_ORDER() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders/" + orderId;
		context.headerParams.put("Authorization", "Bearer " + token);

		APIResponse resp = Methods.DELETE(context);

		Assert.assertEquals(resp.status(), 204);

	}

	@Test(priority = 10)
	public void test07_TRY_GET_DELETED_ORDER() throws IOException {

		Context context = new Context();
		context.url = baseURL + "/orders/" + orderId;
		context.headerParams.put("Authorization", "Bearer " + token);

		APIResponse resp = Methods.GET(context);
		JsonNode node = MyUtil.respToNode(resp);

		Assert.assertTrue(node.get("error").asText().contains("No order with id"));
		Assert.assertTrue(node.get("error").asText().contains(orderId));
	}

}
