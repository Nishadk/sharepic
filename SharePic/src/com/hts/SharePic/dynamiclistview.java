package com.hts.SharePic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class dynamiclistview extends Activity
{	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();		
		Intent backtomain=new Intent(dynamiclistview.this,SharePicMain.class);
		startActivity(backtomain);	
		dynamiclistview.this.finish();
	}
	ScrollView sv;
	LinearLayout ll;
	TextView txtvMore;
	int innewvalue;
	String strCaption="Show more";
	public static ArrayList<String> Listimageurl=new ArrayList<String>();
	public static ArrayList<String> ListcommentsDateall=new ArrayList<String>();
	public void loadlist(ArrayList<String> Listimages,ArrayList<String> ListcommntsDate) 
	{
		//super();
		for(String img:Listimages)
		{
			Listimageurl.add(img);
		}    
		for(String CMT:ListcommntsDate)
		{
			ListcommentsDateall.add(CMT);
		} 
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		sv = new ScrollView(getApplicationContext());
		ll = new LinearLayout(getApplicationContext());
		ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_bg));
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll); 
		dynamicview(0);
	}
	public void dynamicview(int count)
	{	  		
		int inLimit= count+2;
		for(int i=count;i<inLimit;i++)
		{        
			if(i==Listimageurl.size())
			{
				strCaption="NO More data";
				break; 
			}
			Bitmap image=converttobitmap(Listimageurl.get(i).toString());
			String strComments=ListcommentsDateall.get(i).toString(); 			   
			final TextView txtvcommnt = new TextView(this);
			txtvcommnt.setText(strComments);
			txtvcommnt.setTextColor(getResources().getColor(R.color.black));
			ll.addView(txtvcommnt);    
			final ImageView imgfullview = new ImageView(this);
			imgfullview.setImageBitmap(image);
			imgfullview.setOnClickListener(new OnClickListener() 
			{			
				@Override
				public void onClick(View v) 
				{
					Bitmap bitmap = ((BitmapDrawable)imgfullview.getDrawable()).getBitmap();
					String strDate=txtvcommnt.getText().toString();										
					postFullview(bitmap,strDate);
				}
			});
			ll.addView(imgfullview);	             
			this.setContentView(sv); 	            
			innewvalue=i;
		}
		txtvMore=new TextView(this);
		@SuppressWarnings("deprecation")
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		txtvMore.setLayoutParams(lp);
		txtvMore.setGravity(Gravity.CENTER_HORIZONTAL);
		txtvMore.setText(strCaption);
		txtvMore.setTextColor(getResources().getColor(R.color.blue));
		ll.addView(txtvMore);
		this.setContentView(sv); 
		txtvMore.setOnClickListener(new OnClickListener() 
		{	       	  
			@Override
			public void onClick(View v) 
			{					
				if(strCaption.equals("Show more"))
				{
					txtvMore.setVisibility(View.GONE);
					dynamicview(innewvalue+1);
				}

			}
		});
	}
	public void postFullview(Bitmap imgFullview,String strDate)
	{
		AlertDialog.Builder adBuilder=new AlertDialog.Builder(dynamiclistview.this);
		adBuilder.setCancelable(false);
		final ImageView imgvdisplay = new ImageView(dynamiclistview.this);	
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		imgvdisplay.setLayoutParams(lp);
		imgvdisplay.setImageBitmap(imgFullview);
		adBuilder.setView(imgvdisplay);	
		String strMdte=strDate.substring(strDate.length()-19, strDate.length()-1);
		adBuilder.setTitle("Posted on:"+strMdte);
		adBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{				
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();											
			}
		});				
		adBuilder.show();
	}
	private Bitmap converttobitmap(String strUrl)
	{
		URL url = null;
		Bitmap bmp = null;
		try
		{
			url = new URL(strUrl);
			bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return bmp;
	}
}