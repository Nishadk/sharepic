package com.hts.SharePic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoadPic extends ListActivity
{	
	private String mAccessToken = "457548647601301|kWtjZuefJFr9zFETWAZepe7EA8k";	
	ArrayList<String> strMArListDBData=new ArrayList<String>();
	ArrayList<String> strMArlstSavedDate=new ArrayList<String>();
	ArrayList<String> strMArlstSavedComments=new ArrayList<String>();
	public ArrayList<String> strMArlstSavedCommentsDate=new ArrayList<String>();
	public ArrayList<String> strMArlstSavedImageurls=new ArrayList<String>();
	Database objdb;
	ListView lstvMain;
	String strMessage = null;
	String strDate = null;
	String strUrl;
	String FILENAME = "AndroidSSO_data";
	String[] strarrMsgCmnt = new String[50];
	String[] strarrMsgDate = new String[50];
	String[] strarrPicSrc = new String[50];
	Toast tstToast;	
	TextView txtvDbData;
	public static String strTest;
	public static int inI=0; 
	public String TAG_CMNT = "comment";
	public String TAG_DATE = "date";
	public String TAG_PIC = "pic";		
	//private PendingIntent pendiitntIntent;
	//private Timer mytimer;
	//private AlarmManager alarmManager;
	public ProgressBar progressLoad;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		Log.i(SharePicMain.TAG,"LoadPic : timerworkstart");
		setContentView(R.layout.list_retrieve);
		if(android.os.Build.VERSION.SDK_INT>=9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}		
		if(isInternetOn()==true)
		{
			progressLoad=(ProgressBar)findViewById(R.id.progressBarLoad);
			Log.i(SharePicMain.TAG,"LoadPic : is net on==true");			
			LoadPic.this.UpdateScreen();
		}
		if(isInternetOn() == false)
		{	
			Toast.makeText(getApplicationContext(), "Check Internet connection..", Toast.LENGTH_LONG).show();
			
		}							
	}
	@Override
	public void onBackPressed()
	{	
		LoadPic.this.finish();
		Intent intnt=new Intent(LoadPic.this,SharePicMain.class);
		startActivity(intnt);	
	} 
	/*private void RetrieveDBoffline()
	{
		String strPreferenceprevalue="";
		String strSharedvalue="";
		SharedPreferences msharedpreference=getSharedPreferences("Mpref", MODE_PRIVATE);
		if(msharedpreference.contains("post1"))
		{
			strSharedvalue=msharedpreference.getString("post1","");
		}
		if(!strSharedvalue.equals(strPreferenceprevalue))
		{
			strMArListDBData.clear();
			objdb=new Database(getApplicationContext());
			objdb.openToread();
			Cursor cMcursor=objdb.retriveall();
			cMcursor.moveToLast();
			while(!cMcursor.isBeforeFirst())
			{
				strMArlstSavedDate.add(cMcursor.getString(1).toString());
				strMArlstSavedComments.add(cMcursor.getString(2).toString());
				strMArlstSavedImageurls.add(cMcursor.getString(3).toString());
				strMArlstSavedCommentsDate.add(cMcursor.getString(2).toString()+""+cMcursor.getString(1).toString());	
				cMcursor.moveToPrevious();
			}
			objdb.close();		
		}else 
		{
			Toast.makeText(getApplicationContext(),"Need Intentet Connection",Toast.LENGTH_LONG).show();
		}
	}*/
	@SuppressWarnings("static-access")
	public final boolean isInternetOn()
	{
		ConnectivityManager connec =(ConnectivityManager)getSystemService(LoadPic.this.CONNECTIVITY_SERVICE);	
		if ( connec.getNetworkInfo(0).getState()==NetworkInfo.State.CONNECTED ||
				connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
				connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
				connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) 
		{
			return true;
		} else if ( connec.getNetworkInfo(0).getState() ==
				NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState()== NetworkInfo.State.DISCONNECTED) 
		{
			return false;
		}
		return false;
	}
	private Runnable Timer_Tick = new Runnable()
	{
		public void run() 
		{

			Log.i(SharePicMain.TAG,"LoadPic : Timer_Tick");
			Myasynchrounous asyn=new Myasynchrounous();
			asyn.execute(getApplicationContext());	
		}
	};
	public void UpdateScreen() 
	{
		Log.i(SharePicMain.TAG,"LoadPic : UpdateScreen()");
		this.runOnUiThread(Timer_Tick);	
		Log.i(SharePicMain.TAG,"LoadPic : runOnUiThread(Timer_Tick)");
	}
	@Override
	protected void onStop() 
	{
		super.onStop();	
		strMArlstSavedDate.clear();
		strMArlstSavedComments.clear();
		strMArlstSavedCommentsDate.clear();
		strMArlstSavedImageurls.clear();
	}
	public String getAccessToken() 
	{
		return mAccessToken;
	}
	/*class IconicAdapter extends ArrayAdapter<String> 
	{
		IconicAdapter() 
		{
			super(LoadPic.this, R.layout.listadapter,R.id.txtvComment,strMArlstSavedCommentsDate);
		}
		@Override
		public View getView(int position, View convertView,	ViewGroup parent)
		{
			View row=super.getView(position, convertView, parent);
			ImageView icon=(ImageView)row.findViewById(R.id.image);
			ImageView fullview=(ImageView)row.findViewById(R.id.imageFullview);
			strUrl=strMArlstSavedImageurls.get(position).toString();			
			Bitmap bmp=converttobitmap(strUrl);
			icon.setImageBitmap(bmp);
			fullview.setImageBitmap(bmp);
			return(row);
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
	}*/
	class Myasynchrounous extends AsyncTask<Context, Integer, String>
	{	
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();			
			progressLoad.setVisibility(View.VISIBLE);
		}
		@Override
		protected String doInBackground(Context... params) 
		{	
			Log.i(SharePicMain.TAG,"LoadPic : AsyncTask(Do in background)");
			Myasynchrounous.this.getCommunityInformation();
			return null;
		}	
		@Override
		protected void onPostExecute(String result)
		{		
			super.onPostExecute(result);
			//dynamiclistview myadapter=new dynamiclistview(strMArlstSavedImageurls,strMArlstSavedCommentsDate);	
			Log.i("NISHAD","OKKKKKKKKKKKKKKKKKKKKKKK11111");
			dynamiclistview objdynamic=new dynamiclistview();
			objdynamic.loadlist(strMArlstSavedImageurls, strMArlstSavedCommentsDate);
			if(strMArlstSavedImageurls.size()>3)
			{
			Intent myintent=new Intent(LoadPic.this,dynamiclistview.class);
		    startActivity(myintent);
		    LoadPic.this.finish();
			}else 
			{
				Toast.makeText(getApplicationContext(), "Network Problem Please try again",Toast.LENGTH_LONG).show();
			}
			/*ListView lstvMyview = getListView();
			IconicAdapter myadapter=new IconicAdapter(getApplicationContext(),strMArlstSavedImageurls,strMArlstSavedCommentsDate);			
			setListAdapter(myadapter);*/	
			Log.i("NISHAD","OKKKKKKKKKKKKKKKKKKKKKKK11111");
			/*Loaddynamically t = new Loaddynamically(getApplicationContext(),myadapter,strMArlstSavedImageurls,strMArlstSavedCommentsDate,progressLoad,0);
			t.execute();
			//progressLoad.setVisibility(View.GONE);
			Log.i("NISHAD","OKKKKKKKKKKKKKKKKKKKKKKK22222");		
			lstvMyview.setOnItemClickListener(new OnItemClickListener() 
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int inPosition,	long arg3) 
				{
					URL urlPic = null;
					Bitmap bmpUrlImg = null;
					String strName = ((TextView) view.findViewById(R.id.txtvComment)).getText().toString();
					Log.i(SharePicMain.TAG,"LoadPic : String to put = "+strName);
					String strImage = strMArlstSavedImageurls.get(inPosition).toString();	
					String strpostdate = strMArlstSavedDate.get(inPosition).toString();
					Log.i("NISHAD","image="+strImage+"post="+strpostdate);
					try 
					{
						urlPic = new URL(strImage);
						bmpUrlImg = BitmapFactory.decodeStream(urlPic.openConnection().getInputStream());
						add(bmpUrlImg,strpostdate);
					}
					catch (MalformedURLException e) 
					{						
						e.printStackTrace();
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}						
				}
			});	*/
		}
		/*private void add(Bitmap bmp,String date) 
		{
			AlertDialog.Builder adBuilder=new AlertDialog.Builder(LoadPic.this);
			adBuilder.setCancelable(false);
			final EditText etxtCurrencyvalue = new EditText(this);	
			final ImageView imgvdisplay = new ImageView(LoadPic.this);	
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			imgvdisplay.setLayoutParams(lp);
			imgvdisplay.setImageBitmap(bmp);
			//etxtCurrencyvalue.setLayoutParams(lp);
			adBuilder.setView(imgvdisplay);	
			//String[] strMdte=date.split(":");
			//adBuilder.setTitle("Posted on:"+strMdte[1]+":"+strMdte[2]);
			String strMdte=date;
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
		}*/
		@SuppressWarnings("unused")
		private void retrieveDB()
		{
			String strPreferenceprevalue="";
			String strSharedvalue="";
			SharedPreferences msharedpreference=getSharedPreferences("Mpref", MODE_PRIVATE);
			if(msharedpreference.contains("post1"))
			{
				strSharedvalue=msharedpreference.getString("post1","");
			}
			if(!strSharedvalue.equals(strPreferenceprevalue))
			{
				Log.i(SharePicMain.TAG,"LoadPic : INSIDE Retrv DB");
				strMArListDBData.clear();
				objdb=new Database(getApplicationContext());
				objdb.openToread();
				Cursor cMcursor=objdb.retriveall();
				cMcursor.moveToLast();
				while(!cMcursor.isBeforeFirst())
				{
					String strDbData="";
					strDbData=cMcursor.getString(1).toString()+"nichu"+cMcursor.getString(2).toString()+"nichu"+cMcursor.getString(3).toString();
					strMArListDBData.add(strDbData);
					cMcursor.moveToPrevious();
				}		
				objdb.close();
				Log.i(SharePicMain.TAG,"LoadPic : ArrayListDb data="+strMArListDBData);
				for(int inN=1;inN<=strMArListDBData.size();inN++)
				{
					int inEvaluate=0;
					String strTempDbvalue="",strTempvalue="";String[] strTempvalues=new String[3];
					strTempvalue=strMArListDBData.get(inN-1);
					strTempvalues=strTempvalue.split("nichu");
					strTempDbvalue=strTempvalues[0]+strTempvalues[1]+strTempvalues[2];
					for(int inP=1;inP<=strMArlstSavedDate.size();inP++)
					{
						String strSavedarlsValue="";
						strSavedarlsValue=strMArlstSavedDate.get(inP-1)+strMArlstSavedComments.get(inP-1)+strMArlstSavedImageurls.get(inP-1);						
						if(!strTempDbvalue.equals(strSavedarlsValue))
						{	
							Log.i(SharePicMain.TAG,"LoadPic : SavescreenToarrlst notEquals");
							inEvaluate++;							
						}						
					}
					int size=strMArlstSavedDate.size();
					if(inEvaluate==size)
					{
						String strMDate="";String strMcomment="";String strMImageurl="";
						strMDate=strTempvalues[0].toString();
						strMcomment=strTempvalues[1].toString();
						strMImageurl=strTempvalues[2].toString();
						strMArlstSavedDate.add(strMDate);
						strMArlstSavedComments.add(strMcomment);
						strMArlstSavedImageurls.add(strMImageurl);
						strMArlstSavedCommentsDate.add(strMcomment+""+strMDate);
					}
				}	
			}
		} 
		@SuppressLint("SimpleDateFormat")
		public void getCommunityInformation() 
		{
			strMArlstSavedCommentsDate.clear();  			
			strMArlstSavedImageurls.clear();			
			JSONArray jArr = null;
			JSONArray jArr2 = null;
			String jsonuser = null;
			String fql = "SELECT updated_time, message, attachment FROM stream WHERE source_id =228398190627075";
			Bundle parameters = new Bundle();
			parameters.putString("format", "json");
			parameters.putString("query", fql);
			parameters.putString("method", "fql.query");
			parameters.putString("access_token",getAccessToken());
			JSONObject JOmessage=null;
			JSONObject JOmessage2=null;
			try 
			{
				jsonuser = "{\"data\":" + SharePicMain.facebook.request(parameters) + "}";
				JSONObject JObj = new JSONObject(jsonuser);
				jArr = JObj.getJSONArray("data");			
				Log.i("NISHAD*******","jArraysize="+jArr.length());
				for(int inI=0;inI<jArr.length();inI++)
				{
					String strSrc = "";	
					Log.i("NISHAD******","Count="+inI);
					JOmessage = jArr.getJSONObject(inI);
					strMessage = JOmessage.getString("message");				
					strDate = JOmessage.getString("updated_time");
					long lngTimeStamp = Long.valueOf(strDate);
					Date dteGetDate = new Date(lngTimeStamp * 1000L);
					SimpleDateFormat sdfTimeFormatter = new SimpleDateFormat("dd-MM-yyyy  hh:mm aa");
					String strGetTime = sdfTimeFormatter.format(dteGetDate);	
					Log.i(SharePicMain.TAG,"LoadPic : DATE="+strGetTime);					
					JSONObject JOattachment = JOmessage.getJSONObject("attachment");			
					/*if(!JOattachment.getJSONArray("media").isNull(inI))
					{*/
					if(JOattachment.has("media"))
					{
						jArr2 = JOattachment.getJSONArray("media");
						JOmessage2 = jArr2.getJSONObject(0);
						strSrc = JOmessage2.getString("src");	
					}	
					/*String[] strPic = new String[jArr.length()];
					String[] strCmd = new String[jArr.length()];
					String[] strDte = new String[jArr.length()];
					strCmd[inI] = (strMessage);
					strDte[inI] = ("\n"+strGetTime+"\n");
					strPic[inI]=strSrc;*/		
					/*strMArlstSavedDate.add(strDte[inI]);
					strMArlstSavedCommentsDate.add(strCmd[inI]+""+strDte[inI]);
					strMArlstSavedImageurls.add(strPic[inI]);*/		
					strMArlstSavedDate.add("\n"+strGetTime+"\n");
					strMArlstSavedCommentsDate.add(strMessage+""+"\n"+strGetTime+"\n");
					strMArlstSavedImageurls.add(strSrc);
				}				
			}
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}

		}

	}

}