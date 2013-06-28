package com.hts.SharePic;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SharePicMain extends Activity
{
	public static String TAG = "hts";
	private static String APP_ID = "474678172554379";
	static Facebook facebook = new Facebook(APP_ID);	
	Button btnUpload;
	Button btnRetrieve;
	/** Called when the activity is first created.*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		btnUpload = (Button) findViewById(R.id.btnUpload);
		btnRetrieve = (Button) findViewById(R.id.btnRetrieve);
	}
	public void shareClick(View clickView)
	{
		if(clickView == btnUpload)
		{
			SharePicMain.this.finish();
			Intent itntUploadPic = new Intent(this,UploadPic.class);
			startActivity(itntUploadPic);
		}
		else if(clickView == btnRetrieve)
		{
			SharePicMain.this.finish();
			Intent itntRetrievePic = new Intent(this,LoadPic.class);
			startActivity(itntRetrievePic);
		}
	}
}