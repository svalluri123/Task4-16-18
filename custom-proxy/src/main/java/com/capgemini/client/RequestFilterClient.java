/**
 * 
 */
package com.capgemini.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author Sirisha
 *
 */
public class RequestFilterClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RequestFilterClient clientFilter = new RequestFilterClient();
		try {
			clientFilter.post("Post Test");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Comments
	 */

	public void post(String body) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("http://127.0.0.1:8100/greeting");

		String xml = "<xml>" 
					+ "	<customer>" 
					+ "		<name>Customer Name</name>"
					+ "		<address>Customer Address</address>" 
					+ "	</customer>" 
					+ "</xml>";

		HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
		httpPost.setEntity(entity);
		HttpResponse response = client.execute(httpPost);

		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
	}
}
