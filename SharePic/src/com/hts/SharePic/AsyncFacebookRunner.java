package com.hts.SharePic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import android.content.Context;
import android.os.Bundle;

public class AsyncFacebookRunner 
{
	Facebook clsFb;
	private Object objState;

	public AsyncFacebookRunner(Facebook fb) 
	{
		this.clsFb = fb;
	}

	public void logout(final Context context,
			final RequestListener listener,
			final Object state) 
	{
		objState=state;
		new Thread()
		{
			@Override 
			public void run() 
			{
				try 
				{
					String strResponse = clsFb.logout(context);
					if (strResponse.length() == 0 || strResponse.equals("false"))
					{
						listener.onFacebookError(new FacebookError(
								"auth.expireSession failed"), state);
						return;
					}
					listener.onComplete(strResponse, state);
				} 
				catch (FileNotFoundException e) 
				{
					listener.onFileNotFoundException(e, state);
				} 
				catch (MalformedURLException e)
				{
					listener.onMalformedURLException(e, state);
				}
				catch (IOException e) 
				{
					listener.onIOException(e, state);
				}
			}
		}.start();
	}

	public void logout(final Context context, final RequestListener listener) 
	{
		logout(context, listener, /* state */ objState);
	}

	public void request(Bundle parameters,
			RequestListener listener,
			final Object state)
	{
		request(null, parameters, "GET", listener, state);
	}

	public void request(Bundle parameters, RequestListener listener)
	{
		request(null, parameters, "GET", listener, /* state */ null);
	}

	public void request(String graphPath,
			RequestListener listener,
			final Object state)
	{
		request(graphPath, new Bundle(), "GET", listener, state);
	}

	public void request(String graphPath, RequestListener listener)
	{
		request(graphPath, new Bundle(), "GET", listener, /* state */ null);
	}

	public void request(String graphPath,
			Bundle parameters,
			RequestListener listener,
			final Object state) 
	{
		request(graphPath, parameters, "GET", listener, state);
	}

	public void request(String graphPath,
			Bundle parameters,
			RequestListener listener) 
	{
		request(graphPath, parameters, "GET", listener, /* state */ null);
	}

	public void request(final String graphPath,final Bundle parameters,
			final String httpMethod,
			final RequestListener listener,
			final Object state) 
	{
		new Thread() 
		{
			@Override 
			public void run() 
			{
				try 
				{
					String resp = clsFb.request(graphPath, parameters, httpMethod);
					listener.onComplete(resp, state);
				}
				catch (FileNotFoundException e) 
				{
					listener.onFileNotFoundException(e, state);
				}
				catch (MalformedURLException e) 
				{
					listener.onMalformedURLException(e, state);
				}
				catch (IOException e) 
				{
					listener.onIOException(e, state);
				}
			}
		}.start();
	}
	
	public static interface RequestListener 
	{
		public void onComplete(String strResponse, Object state);
		public void onIOException(IOException e, Object state);
		public void onFileNotFoundException(FileNotFoundException e,
				Object state);
		public void onMalformedURLException(MalformedURLException e,
				Object state);
		public void onFacebookError(FacebookError e, Object state);
	}
}
