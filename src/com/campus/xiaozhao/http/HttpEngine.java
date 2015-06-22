package com.campus.xiaozhao.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.component.logger.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @author antoniochen
 * this class is the engine for http
 */
public class HttpEngine {
	public HttpEngine() {

	}

	private static final String TAG = "HttpEngine";
	private int mTimeOut = 60000;
	private String mUserAgent = "";

	public void setUserAgent(String ua) {
		mUserAgent = ua;
	}

	public void setTimeOUT(int to) {
		mTimeOut = to;
	}

	private AsyncHttpClient mHttpClient = getHttpsClientInstance();

	private AsyncHttpClient getHttpsClientInstance() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(mTimeOut);
		client.setUserAgent(mUserAgent);
		return client;
	}

	/**
	 * HTTP GET FOR STRING 
	 * @param url
	 * @param params
	 * @param headers
	 * @param responseHandler
	 */
	public void httpGet(String url, RequestParams params,
			Header[] headers, AsyncHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		Logger.i(TAG, "GO AHEAD TO GET: " + url);
		try {
			mHttpClient.get(null, url, headers, params, responseHandler);
		} catch (Exception e) {
			Logger.i(TAG, "FAIL TO GET: " + url);
		}
	}

	/**
	 * HTTP POST FOR STRING
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public void httpPost(String url, Header[] headers, HttpEntity entity,
			String contentType, final AsyncHttpResponseHandler responseHandler) {
		try {
			Logger.i(TAG, "GO AHEAD TO POST: " + url);
			mHttpClient.post(null, url, headers, entity, contentType,
					responseHandler);
		} catch (Exception e) {
			Logger.i(TAG, "FAIL TO POST: " + url);
		}
	}

	
	/**
	 * HTTP GET FOR BINARY 
	 * @param url
	 * @param params
	 * @param headers
	 * @param responseHandler
	 */
	public void httpGet(String url, RequestParams params,
			Header[] headers, BinaryHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		Logger.i(TAG, "GO AHEAD TO GET: " + url);
		try {
			mHttpClient.get(null, url, headers, params, responseHandler);
		} catch (Exception e) {
			Logger.i(TAG, "FAIL TO GET: " + url);
		}
	}

	/**
	 * HTTP POST FOR BINARY
	 * @param url
	 * @param headers
	 * @param entity
	 * @param contentType
	 * @param responseHandler
	 */
	public void httpPost(String url, Header[] headers, HttpEntity entity,
			String contentType, final BinaryHttpResponseHandler responseHandler) {
		try {
			Logger.i(TAG, "GO AHEAD TO POST: " + url);
			mHttpClient.post(null, url, headers, entity, contentType,
					responseHandler);
		} catch (Exception e) {
			Logger.i(TAG, "FAIL TO POST: " + url);
		}
	}
}
