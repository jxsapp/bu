package org.bu.android.pact;

import org.bu.android.log.BuLog;
import org.bu.android.misc.BuJSON;
import org.bu.android.misc.BuStringUtils;
import org.bu.android.pact.BuHttp.BuHttpListener;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

public interface BuPackMaster {
	static final int SUCCESS = 0;
	static final int FAILED = 1;
	static final int ERROR = 2;

	public abstract class BuPactListener {
		public void onSuccess(int statusCode, String content) {

		}

		public void onSuccess4bin(int statusCode, String save_path) {

		}

		public void onSuccess(int statusCode, BuJSON json) {

		}

		public void onFailure(int errorCode, String message) {

		}

	}

	public abstract class BuPackLogic<L> {
		protected String TAG = "";
		protected L mListener;
		protected Handler mHandler = null;

		public BuPackLogic(Looper looper, L l) {
			this(l);
			mHandler = new Handler(looper, new Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					return mHandleMessage(msg);
				}
			});
		}

		protected final void mHandlerSuccess(int status, Bundle data) {
			Message msg = new Message();
			msg.arg1 = status;
			msg.setData(data);
			msg.what = SUCCESS;
			mHandler.sendMessage(msg);
		}

		protected final void mHandlerFailure(int errorCode, Bundle data) {
			Message msg = new Message();
			msg.arg1 = errorCode;
			msg.setData(data);
			msg.what = FAILED;
			mHandler.sendMessage(msg);
		}

		protected final void mHandlerError(int errorCode, Bundle data) {
			Message msg = new Message();
			msg.arg1 = errorCode;
			msg.setData(data);
			msg.what = ERROR;
			mHandler.sendMessage(msg);
		}

		public BuPackLogic(L l) {
			TAG = getClass().getSimpleName();
			this.mListener = l;
		}

		protected abstract boolean mHandleMessage(Message msg);

		protected void getJson(final String url, boolean withParams, final BuPactListener buPactListener) {
			getJson(url, new JSONObject(), withParams, buPactListener);
		}

		protected void getJson(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.GET, withParams).send();
		}

		protected void getBin(final String url, boolean withParams, final BuPactListener buPactListener) {
			getBin(url, new JSONObject(), withParams, buPactListener);
		}

		protected void getBin(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.GET_BIN, withParams).send();
		}

		protected void postBin(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.POST_BIN, withParams).send();
		}

		protected void postJson(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.POST, withParams).send();
		}

		protected void putJson(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.PUT, withParams).send();
		}

		protected void delJson(final String url, boolean withParams, final BuPactListener buPactListener) {
			delJson(url, new JSONObject(), withParams, buPactListener);
		}

		protected void delJson(final String url, final JSONObject json, boolean withParams, final BuPactListener buPactListener) {
			new BuSendJson(url, json, buPactListener, Method.DELETE, withParams).send();
		}

		enum Method {
			POST, GET, PUT, DELETE, GET_BIN, POST_BIN;
		}

		final class BuSendJson extends Thread implements BuHttpListener {
			private String url;
			private JSONObject json;
			private BuPactListener buPactListener;
			private Method method = Method.POST;
			private boolean withParams = false;

			private BuSendJson(String url, JSONObject json, BuPactListener buPactListener, Method method, boolean withParams) {
				super();
				this.url = url;
				this.json = json;
				this.buPactListener = buPactListener;
				this.method = method;
				this.withParams = withParams;
				BuLog.d(TAG, url + "?" + json.toString());
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				BuLog.d(TAG, statusCode + "\n" + content + "");
				if (BuStringUtils.isEmpety(content)) {
					content = "{}";
				}
				if (content.startsWith("[")) {
					String rst = "{\"results\":%s}";
					rst = String.format(rst, content);
					content = rst;
				}
				buPactListener.onSuccess(statusCode, content);
				try {
					JSONObject object = new JSONObject(content);
					buPactListener.onSuccess(statusCode, new BuJSON(object));
				} catch (Exception e) {
					BuLog.e(TAG, "", e);
				}
			}

			@Override
			public void onSuccess4Bin(int statusCode, String path) {
				buPactListener.onSuccess4bin(statusCode, path);
			}

			@Override
			public void onFailed(int errorCode, String message) {
				BuLog.e(TAG, errorCode + ": \n" + message + "");
				buPactListener.onFailure(errorCode, message);
			}

			protected void send() {
				this.start();
			}

			@Override
			public void run() {
				if (method == Method.POST) {
					new BuHttp().postJson(url, new BuJSON(json), this);
				} else if (method == Method.GET) {
					new BuHttp().getJson(url, new BuJSON(json), withParams, this);
				} else if (method == Method.PUT) {
					new BuHttp().putJson(url, new BuJSON(json), this);
				} else if (method == Method.DELETE) {
					new BuHttp().deleteJson(url, new BuJSON(json), withParams, this);
				} else if (method == Method.GET_BIN) {
					new BuHttp().getBin(url, new BuJSON(json), withParams, this);
				} else if (method == Method.POST_BIN) {
					BuJSON pa = new BuJSON(json);
					new BuHttp().postBin(url, pa, pa.getString("_file_path_"), this);
				}
			}

		}

	}
}
