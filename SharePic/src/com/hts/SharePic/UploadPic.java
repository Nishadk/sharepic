package com.hts.SharePic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
public class UploadPic extends Activity 
{
	public static String strImagegallerypath="";
	private static int RESULT_LOAD_IMAGE = 1;
	byte[] byteArray;	
	Button btnUpload;
	Bitmap BtmpMimg;
	ImageView imgvDisp;
	ProgressBar pbar;
	EditText txtSubject;
	@Override
	public void onBackPressed() 
	{		
		super.onBackPressed();
		UploadPic.this.finish();
		Intent intntback=new Intent(UploadPic.this,SharePicMain.class);
		startActivity(intntback);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_layout);
		pbar= (ProgressBar)findViewById(R.id.progressBar1);
		btnUpload = (Button) findViewById(R.id.btnUpload);	
		Intent itntLoadPic = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(itntLoadPic, RESULT_LOAD_IMAGE);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) 
		{
			final Uri selectedImage = data.getData();
			String[] strFilePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					strFilePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(strFilePathColumn[0]);
			final String strPicturePath = cursor.getString(columnIndex);
			cursor.close();		
			Log.i("NISHAD******","path="+strPicturePath);
			imgvDisp = (ImageView) findViewById(R.id.imgvDisp);
			imgvDisp.setImageBitmap(BitmapFactory.decodeFile(strPicturePath));					
			btnUpload.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					strImagegallerypath="";
					strImagegallerypath=strPicturePath;
					if(!strImagegallerypath.equals(""))
					{
						txtSubject=(EditText)findViewById(R.id.edittextsubject);					
						uploadAsync myPicupload=new uploadAsync();
						myPicupload.execute();
					}else
					{
						Toast.makeText(getApplicationContext(),"No such image file",Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null == data) 
		{
			UploadPic.this.finish();
			Intent intntback=new Intent(UploadPic.this,SharePicMain.class);
			startActivity(intntback);
			Log.i("NISHAD11111", "requestcanceled");
		}
	}	
	class uploadAsync extends AsyncTask<Void,Void,Void>
	{
		String strSubject="";
		@Override
		protected void onPostExecute(Void result)
		{			
			super.onPostExecute(result);
			pbar.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(),"Uploaded..", Toast.LENGTH_LONG).show();
		}
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();			
			strSubject=txtSubject.getText().toString();
			pbar.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) 
		{
			Mail m = new Mail("htssharepic@gmail.com", "Htsqatar123"); 
			String[] toArr = {"lusaka198walnut@m.facebook.com"}; 
			m.setTo(toArr); 
			m.setFrom("htssharepic@gmail.com"); 
			if(!strSubject.equals(""))
			{
				m.setSubject(strSubject); 
			}else 
			{
				m.setSubject("SharePic"); 
			}			
			m.setBody("SharePic"); 				 
			try 
			{ 
				m.addAttachment(strImagegallerypath); 
				m.send();
				if(m.send()) 
				{ 			 
				}
			} catch(Exception e) 
			{
				Log.e("MailApp", "Could not send email", e); 
			} 
			return null;
		}


	}

}
