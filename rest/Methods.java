package rest;

import java.util.Map;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class Methods {

	static Playwright playwright;

	public static APIRequestContext createRequest() {

		playwright = Playwright.create();
		APIRequest request = playwright.request();
		APIRequestContext requestContext = request.newContext();
		return requestContext;
	}

	public static void playwrightClose() {

		playwright.close();
	}

	public static RequestOptions init(Context context) {

		RequestOptions reqOpt = RequestOptions.create();

		if (context.body != null) {
			reqOpt.setData(context.body);
		}

		if (!context.queryParams.isEmpty()) {
			for (Map.Entry<String, String> queryParam : context.queryParams.entrySet()) {
				reqOpt.setQueryParam(queryParam.getKey(), queryParam.getValue());
			}
		}
		if (!context.headerParams.isEmpty()) {
			for (Map.Entry<String, String> headerParam : context.headerParams.entrySet()) {
				reqOpt.setHeader(headerParam.getKey(), headerParam.getValue());
			}
		}
//if(context.contentType!=null||!context.contentType.isEmpty()) {
//	for(Map.Entry<String, String>headerParam:context.headerParams.entrySet()) {
//		reqOpt.setHeader(headerParam.getKey(), headerParam.getValue());
//	}
//}

		return reqOpt;
	}

	public static APIResponse GET(Context context) {

		APIResponse resp = null;
		APIRequestContext request = createRequest();
		RequestOptions reqOpt = init(context);

		if (context.url != null) {
			resp = request.get(context.url, reqOpt);
		}
		return resp;

	}

	public static APIResponse POST(Context context) {

		APIResponse resp = null;
		APIRequestContext request = createRequest();
		RequestOptions reqOpt = init(context);

		if (context.url != null) {
			resp = request.post(context.url, reqOpt);
		}
		return resp;

	}

	public static APIResponse PATCH(Context context) {

		APIResponse resp = null;
		APIRequestContext request = createRequest();
		RequestOptions reqOpt = init(context);

		if (context.url != null) {
			resp = request.patch(context.url, reqOpt);
		}
		return resp;

	}

	public static APIResponse DELETE(Context context) {

		APIResponse resp = null;
		APIRequestContext request = createRequest();
		RequestOptions reqOpt = init(context);

		if (context.url != null) {
			resp = request.delete(context.url, reqOpt);
		}
		return resp;

	}

}
