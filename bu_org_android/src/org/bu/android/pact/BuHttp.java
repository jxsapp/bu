package org.bu.android.pact;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.bu.android.log.BuLog;
import org.bu.android.misc.BuFileHolder;
import org.bu.android.misc.BuJSON;
import org.bu.android.misc.BuStringUtils;
import org.json.JSONObject;

public class BuHttp {

	public static interface BuHttpListener {
		void onSuccess(int statusCode, String content);

		void onSuccess4Bin(int statusCode, String path);

		void onFailed(int errorCode, String message);
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public static int getResponseErrorCode(HttpResponse response) {
		int errorcode = 0;

		Header[] headers = response.getHeaders("header");
		if (headers != null) {
			if (headers.length > 0) {
				String value = headers[0].getValue();
				errorcode = Integer.parseInt(value);
			}
		}

		return errorcode;
	}

	public void postJson(String uri, BuJSON json, BuHttpListener _listener) {
		sendData_Post_Put(new HttpPost(uri), json, _listener);
	}

	public void putJson(String uri, BuJSON json, BuHttpListener _listener) {
		sendData_Post_Put(new HttpPut(uri), json, _listener);
	}

	public void postBin(String uri, BuJSON json, String path, BuHttpListener _listener) {
		sendData_Post_Bin(new HttpPost(uri), json, path, _listener);
	}

	public void deleteJson(String uri, boolean withParams, BuHttpListener _listener) {

		deleteJson(uri, new BuJSON(new JSONObject()), withParams, _listener);
	}

	public void deleteJson(String uri, BuJSON json, boolean withParams, BuHttpListener _listener) {
		sendData_Get_Del(new HttpDelete(getUlr(uri, json, withParams)), json, _listener);
	}

	private String getUlr(String uri, BuJSON json, boolean withParams) {
		if (!withParams) {
			return uri;
		}
		String rst = "";
		StringBuilder urlBd = new StringBuilder(uri);
		if (uri.indexOf("\\?") == -1) {
			urlBd.append("?");
		} else {
			urlBd.append("&");
		}
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			urlBd.append(key + "=" + json.getString(key));
			urlBd.append("&");
		}

		if (urlBd.lastIndexOf("&") == urlBd.length() - 1) {
			rst = urlBd.substring(0, urlBd.length() - 1);
		} else {
			rst = urlBd.toString();
		}
		try {
			return URLEncoder.encode(rst.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rst.toString();
	}

	public void getJson(String uri, boolean withParams, BuHttpListener _listener) {
		getJson(uri, new BuJSON(new JSONObject()), withParams, _listener);
	}

	public void getJson(String uri, BuJSON json, boolean withParams, BuHttpListener _listener) {
		sendData_Get_Del(new HttpGet(getUlr(uri, json, withParams)), json, _listener);
	}

	public void getBin(String uri, boolean withParams, BuHttpListener _listener) {
		getBin(uri, new BuJSON(new JSONObject()), withParams, _listener);
	}

	public void getBin(String uri, BuJSON json, boolean withParams, BuHttpListener _listener) {
		sendData_Get_Bin(new HttpGet(getUlr(uri, json, withParams)), json, _listener);
	}

	private void sendData_Post_Put(HttpEntityEnclosingRequestBase httpPut, BuJSON json, BuHttpListener _listener) {
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-Type", "application/json;charset=utf-8");
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ("accessToken".equals(key)) {
				httpPut.setHeader(key, json.getString(key));
			}
		}
		DefaultHttpClient httpclient = getClient();
		try {
			httpPut.setEntity(new StringEntity(json.toString(), "utf-8"));
			HttpResponse httpResponse = httpclient.execute(httpPut);
			result(httpResponse, _listener);
		} catch (ClientProtocolException ex) {
			_listener.onFailed(ErrorCode.SocketException, "ClientProtocolException");
		} catch (IOException ex) {
			_listener.onFailed(ErrorCode.IOException, "IO Excetipon");
		}
	}

	private void sendData_Get_Del(HttpRequestBase httpGet, BuJSON json, BuHttpListener _listener) {
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ("accessToken".equals(key)) {
				httpGet.setHeader(key, json.getString(key));
			}
		}
		DefaultHttpClient httpclient = getClient();
		try {
			HttpResponse httpResponse = httpclient.execute(httpGet);
			result(httpResponse, _listener);
		} catch (ClientProtocolException ex) {
			_listener.onFailed(ErrorCode.SocketException, "ClientProtocolException");
		} catch (IOException ex) {
			_listener.onFailed(ErrorCode.IOException, "IO Excetipon");
		}
	}

	private void sendData_Get_Bin(HttpRequestBase httpGet, BuJSON json, BuHttpListener _listener) {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ("accessToken".equals(key)) {
				httpGet.setHeader(key, json.getString(key));
			}
		}

		DefaultHttpClient httpclient = getClient();
		try {
			HttpResponse httpResponse = httpclient.execute(httpGet);
			result4Bin(httpResponse, json.getString("save_path"), _listener);
		} catch (ClientProtocolException ex) {
			_listener.onFailed(ErrorCode.SocketException, "ClientProtocolException");
		} catch (IOException ex) {
			_listener.onFailed(ErrorCode.IOException, "IO Excetipon");
		}
	}

	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 100 * 1000;// 设置等待数据超时时间10秒钟

	/**
	 *  * 添加请求超时时间和等待时间  * @author spring sky  * Email vipa1888@163.com  * QQ:
	 * 840950105  * My name: 石明政  * @return HttpClient对象  
	 */
	public DefaultHttpClient getClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	private void sendData_Post_Bin(HttpPost httppost, BuJSON json, String path, BuHttpListener _listener) {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if ("accessToken".equals(key)) {
				httppost.setHeader(key, json.getString(key));
			}
		}
		DefaultHttpClient httpclient = getClient();
		try {
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(path), -1);
			httppost.setEntity(reqEntity);
			HttpResponse httpResponse = httpclient.execute(httppost);
			result(httpResponse, _listener);
		} catch (ClientProtocolException ex) {
			_listener.onFailed(ErrorCode.SocketException, "ClientProtocolException");
		} catch (IOException ex) {
			_listener.onFailed(ErrorCode.IOException, "IO Excetipon");
		}
	}

	private void result4Bin(HttpResponse httpResponse, String path, BuHttpListener _listener) throws IOException {
		int status = httpResponse.getStatusLine().getStatusCode();

		if (status != HttpURLConnection.HTTP_OK//
				&& status != HttpURLConnection.HTTP_CREATED//
				&& status != HttpURLConnection.HTTP_ACCEPTED //
				&& status != HttpURLConnection.HTTP_NO_CONTENT//
		) {
			byte[] responseByte = EntityUtils.toByteArray(httpResponse.getEntity());
			String content = new String(responseByte, 0, responseByte.length);
			_listener.onFailed(status, content);
		} else {

			InputStream in = httpResponse.getEntity().getContent();
			_listener.onSuccess4Bin(status, saveFile(in, path));
		}
	}

	private static String saveFile(InputStream fis, String path) throws IOException {

		boolean isRst = BuFileHolder.saveFile(new File(path), fis);
		BuLog.i("save_file", path + " ..." + isRst + "...");
		return path;
	}

	private void result(HttpResponse httpResponse, BuHttpListener _listener) throws IOException {
		int status = httpResponse.getStatusLine().getStatusCode();
		byte[] responseByte = EntityUtils.toByteArray(httpResponse.getEntity());
		String content = new String(responseByte, 0, responseByte.length);

		if (status != HttpURLConnection.HTTP_OK//
				&& status != HttpURLConnection.HTTP_CREATED//
				&& status != HttpURLConnection.HTTP_ACCEPTED //
				&& status != HttpURLConnection.HTTP_NO_CONTENT//
		) {
			_listener.onFailed(status, content);
		} else {
			if (BuStringUtils.isEmpety(content)) {
				content = "";
			}
			_listener.onSuccess(status, content);
		}
	}

}
