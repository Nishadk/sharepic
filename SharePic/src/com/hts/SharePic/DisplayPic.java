package com.hts.SharePic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayPic extends Activity
{
	private static final String TAG_CMNT = "comment";
	public String TAG_PIC = "pic";
	URL urlPic = null;
	Bitmap bmpUrlImg = null;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_item);
		Intent itntIn = getIntent();
		String strPic = itntIn.getStringExtra(TAG_PIC);
		ImageView imgvFbImg = (ImageView) findViewById(R.id.imgvDisplay);
		try 
		{
			urlPic = new URL(strPic);
			bmpUrlImg = BitmapFactory.decodeStream(urlPic.openConnection().getInputStream());
			imgvFbImg.setImageBitmap(bmpUrlImg);
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		String strName = itntIn.getStringExtra(TAG_CMNT);
		TextView txtvName = (TextView) findViewById(R.id.txtvComment);
		txtvName.setText(strName);
	}
}
