package com.hts.SharePic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public final class Util 
{
	private static boolean ENABLE_LOG = true;
	public static String encodePostBody(Bundle parameters, String boundary) 
	{
		if (parameters == null) return "";
		StringBuilder sb = new StringBuilder();
		for (String key : parameters.keySet()) 
		{
			Object parameter = parameters.get(key);
			if (!(parameter instanceof String))
			{
				continue;
			}
			sb.append("Content-Disposition: form-data; name=\"" + key +
					"\"\r\n\r\n" + (String)parameter);
			sb.append("\r\n" + "--" + boundary + "\r\n");
		}
		return sb.toString();
	}
	@SuppressWarnings("deprecation")
	public static String encodeUrl(Bundle parameters) 
	{
		if (parameters == null) 
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) 
		{
			Object parameter = parameters.get(key);
			if (!(parameter instanceof String))
			{
				continue;
			}
			if (first) first = false; else sb.append("&");
			sb.append(URLEncoder.encode(key) + "=" +
					URLEncoder.encode(parameters.getString(key)));
		}
		return sb.toString();
	}
	@SuppressWarnings("deprecation")
	public static Bundle decodeUrl(String s)
	{
		Bundle params = new Bundle();
		if (s != null) 
		{
			String array[] = s.split("&");
			for (String parameter : array) 
			{
				String v[] = parameter.split("=");
				if (v.length == 2) 
				{
					params.putString(URLDecoder.decode(v[0]),
							URLDecoder.decode(v[1]));
				}
			}
		}
		return params;
	}
	public static Bundle parseUrl(String url) 
	{
		url = url.replace("fbconnect", "http");
		try 
		{
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} 
		catch (MalformedURLException e) 
		{
			return new Bundle();
		}
	}
	@SuppressWarnings("deprecation")
	public static String openUrl(String url, String method, Bundle params) throws MalformedURLException, IOException 
	{
		String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
		String endLine = "\r\n";
		OutputStream os;
		if (method.equals("GET")) 
		{
			url = url + "?" + encodeUrl(params);
		}
		Util.logd("Facebook-Util", method + " URL: " + url);
		HttpURLConnection conn =
				(HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("User-Agent", System.getProperties().
				getProperty("http.agent") + " FacebookAndroidSDK");
		if (!method.equals("GET")) 
		{
			Bundle dataparams = new Bundle();
			for (String key : params.keySet()) 
			{
				Object parameter = params.get(key);
				if (parameter instanceof byte[])
				{
					dataparams.putByteArray(key, (byte[])parameter);
				}
			}
			if (!params.containsKey("method")) 
			{
				params.putString("method", method);
			}
			if (params.containsKey("access_token")) 
			{
				String decoded_token =
						URLDecoder.decode(params.getString("access_token"));
				params.putString("access_token", decoded_token);
			}
			conn.setRequestMethod("POST");
			conn.setRequestProperty(
					"Content-Type",
					"multipart/form-data;boundary="+strBoundary);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			os = new BufferedOutputStream(conn.getOutputStream());
			os.write(("--" + strBoundary +endLine).getBytes());
			os.write((encodePostBody(params, strBoundary)).getBytes());
			os.write((endLine + "--" + strBoundary + endLine).getBytes());
			if (!dataparams.isEmpty())
			{
				for (String key: dataparams.keySet())
				{
					os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine).getBytes());
					os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
					os.write(dataparams.getByteArray(key));
					os.write((endLine + "--" + strBoundary + endLine).getBytes());

				}
			}
			os.flush();
		}
		String response = "";
		try 
		{
			response = read(conn.getInputStream());
		} 
		catch (FileNotFoundException e) 
		{
			response = read(conn.getErrorStream());
		}
		return response;
	}
	private static String read(InputStream in) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) 
		{
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
	public static void clearCookies(Context context)
	{
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr =
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
	public static JSONObject parseJson(String response) throws JSONException, FacebookError 
	{
		if (response.equals("false"))
		{
			throw new FacebookError("request failed");
		}
		if (response.equals("true")) 
		{
			response = "{value : true}";
		}
		JSONObject json = new JSONObject(response);
		if (json.has("error")) 
		{
			JSONObject error = json.getJSONObject("error");
			throw new FacebookError(
					error.getString("message"), error.getString("type"), 0);
		}
		if (json.has("error_code") && json.has("error_msg")) 
		{
			throw new FacebookError(json.getString("error_msg"), "",
					Integer.parseInt(json.getString("error_code")));
		}
		if (json.has("error_code")) 
		{
			throw new FacebookError("request failed", "",
					Integer.parseInt(json.getString("error_code")));
		}
		if (json.has("error_msg")) 
		{
			throw new FacebookError(json.getString("error_msg"));
		}
		if (json.has("error_reason"))
		{
			throw new FacebookError(json.getString("error_reason"));
		}
		return json;
	}
	public static void showAlert(Context context, String title, String text) 
	{
		Builder alertBuilder = new Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		alertBuilder.create().show();
	}
	public static void logd(String tag, String msg) 
	{
		if (ENABLE_LOG) 
		{
			Log.d(tag, msg);
		}
	}
}