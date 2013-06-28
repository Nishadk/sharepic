package com.hts.SharePic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import android.util.Log;

import com.hts.SharePic.AsyncFacebookRunner.RequestListener;

public abstract class BaseRequestListener implements RequestListener 
{
	public void onFacebookError(FacebookError ex, final Object state) 
	{
		Log.e("Facebook", ex.getMessage());
		ex.printStackTrace();
	}
	public void onFileNotFoundException(FileNotFoundException ex,final Object state) 
	{
		Log.e("Facebook", ex.getMessage());
		ex.printStackTrace();
	}
	public void onIOException(IOException ex, final Object state) 
	{
		Log.e("Facebook", ex.getMessage());
		ex.printStackTrace();
	}
	public void onMalformedURLException(MalformedURLException ex,final Object state) 
	{
		Log.e("Facebook", ex.getMessage());
		ex.printStackTrace();
	}
}
