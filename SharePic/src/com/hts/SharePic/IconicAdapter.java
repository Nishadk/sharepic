package com.hts.SharePic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class IconicAdapter extends ArrayAdapter<rowitems> 
{
	
	ArrayList<String> Listimageurl=new ArrayList<String>();
	ArrayList<String> ListcommentsDateall=new ArrayList<String>();
	IconicAdapter(Context context,ArrayList<String> Listimages,ArrayList<String> ListcommntsDate) 
	{
		super(context,android.R.layout.simple_expandable_list_item_2);
		for(String img:Listimages)
		{
			Listimageurl.add(img);
		}    
		for(String CMT:ListcommntsDate)
		{
			ListcommentsDateall.add(CMT);
		} 
	}
	public void notifyNoMoreItems()
	{
		Toast.makeText(getContext(), "Download complete..", 1).show();
		 /*mHasMoreItems = false;*/
	    //mFooter.setText("No more Items");
	}
	@Override
	public View getView(int position, View convertView,	ViewGroup parent)
	{
		/*View row=super.getView(position, convertView, parent);
		ImageView icon=(ImageView)row.findViewById(R.id.image);
		ImageView fullview=(ImageView)row.findViewById(R.id.imageFullview);
		strUrl=strMArlstSavedImageurls.get(position).toString();			
		Bitmap bmp=converttobitmap(strUrl);
		icon.setImageBitmap(bmp);
		fullview.setImageBitmap(bmp);
		return(row);*/
		/*if(position == getCount() - 1 && mHasMoreItems)
		{
			Log.i("NISHAD","INSIDEEEEEEEEEEEEEE");
			int inCount=Loaddynamically.inItemscount;
			Log.i("NISHAD","INSIDEEEEEEEE="+getCount());
			Loaddynamically t = new Loaddynamically(getContext(),this,Listimageurl,ListcommentsDateall,inCount);
	            t.execute();
			
		}*/
		if(convertView == null)
		{
			Log.i("NISHAD","INSIDEE222");
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter,parent,false);
			TextView t1 = (TextView) convertView.findViewById(R.id.txtvComment);
			ImageView icon=(ImageView) convertView.findViewById(R.id.image);
			ImageView fullview=(ImageView)convertView.findViewById(R.id.imageFullview);
			convertView.setTag(new Holder(t1,icon,fullview));
		}
		rowitems p = getItem(position);
		Holder h = (Holder) convertView.getTag();
		h.t1.setText(p.commentsanddate);
		String strUrl=p.imageurl;	
		Log.i("NISHADimmmg","imgurl="+strUrl);
		Bitmap bmp=converttobitmap(strUrl);			
		h.icon.setImageBitmap(bmp);
		h.fullview.setImageBitmap(bmp);
		/*h.t2.setText("Made by "+p.company);
	        h.t3.setText("Delivered to "+p.address+" in "+p.city);*/
		return convertView;
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
	private static class Holder
	{
		public final TextView t1;
		public final ImageView icon;
		public final ImageView fullview;
		private Holder(TextView t1, ImageView icon, ImageView fullview)
		{
			this.t1 = t1;
			this.icon = icon;
			this.fullview = fullview;
		}
	}
}
