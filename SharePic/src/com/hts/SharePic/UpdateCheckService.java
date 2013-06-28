package com.hts.SharePic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class UpdateCheckService extends IntentService
{
	private String mAccessToken = "457548647601301|kWtjZuefJFr9zFETWAZepe7EA8k";
	private Facebook facebook = new Facebook(getResources().getString(R.string.APP_ID));

	ArrayList<String> strMArListImageurlslast=new ArrayList<String>();
	ArrayList<String> strMarListCommentsdatelast=new ArrayList<String>();
	ArrayList<String> strMArListDBData=new ArrayList<String>();
	ArrayList<String> strMArListPrefImageurls=new ArrayList<String>();
	ArrayList<String> strMArListPrefCommentsdate=new ArrayList<String>();
	ArrayList<String> strMArListPrefComments=new ArrayList<String>();
	ArrayList<String> strMArListPrefDate=new ArrayList<String>();
	ArrayList<String> strMArListImageurls=new ArrayList<String>();
	ArrayList<String> strMarListCommentsdate=new ArrayList<String>();
	ArrayList<String> strMArlistComments=new ArrayList<String>();
	ArrayList<String> strMArlistDate=new ArrayList<String>();
	ArrayList<String> strMArlstUpdatedpost=new ArrayList<String>();

	String[] strPic;
	String[] strCmd;
	String[] strDte;
	String strUrl="";	
	String strMessage = null;
	String strDate = null;
	static String strSrc = null;
	public String TAG_CMNT = "comment";
	public String TAG_PIC = "pic";
	Database objdb;	

	public UpdateCheckService() 
	{		
		super("UpdateCheckService");
	}
	@Override
	protected void onHandleIntent(Intent intent) 
	{	
		Log.i(SharePicMain.TAG,"UpdateCheckService : onHandleIntent()");
		strMArListImageurls.clear();
		strMarListCommentsdate.clear();
		strMArlistComments.clear();
		strMArlistDate.clear();
		strMArlstUpdatedpost.clear();
		strMArListPrefImageurls.clear();
		strMArListPrefDate.clear();
		strMArListPrefCommentsdate.clear();
		strMArListPrefComments.clear();
		UpdateCheckService.this.getCommunityInformation();
		UpdateCheckService.this.IteratingToprefernceDB();		
	}
	public String getAccessToken() 
	{
		return mAccessToken;
	}
	private void IteratingToprefernceDB()
	{  
		Log.i(SharePicMain.TAG,"UpdateCheckService : IteratingToprefernceDB()");
		strMArListPrefDate.clear();
		strMArListPrefComments.clear();
		strMArListPrefImageurls.clear();		
		String strPreferenceprevalue="";
		String strSharedvalue="";
		SharedPreferences msharedpreference=getSharedPreferences("Mpref", MODE_PRIVATE);
		if(msharedpreference.contains("post1"))
		{
			strSharedvalue=msharedpreference.getString("post1","");
		}
		Log.i(SharePicMain.TAG,"UpdateCheckService : sharedpost="+strSharedvalue);
		if(strSharedvalue.equals(strPreferenceprevalue))
		{
			UpdateCheckService.this.clearpreference();
			Log.i(SharePicMain.TAG,"UpdateCheckService : ITeratinginsideifloop");			
			for(int inI=1;inI<=strMArlistDate.size();inI++)
			{
				String strValuepost="";
				strValuepost=strMArlistDate.get(inI-1).toString()+"nichu"+strMArlistComments.get(inI-1).toString()
						+"nichu"+strMArListImageurls.get(inI-1);				
				String strKeypost="";
				strKeypost="post"+(inI);
				UpdateCheckService.this.savepreference(strKeypost,strValuepost);			
				String strDate="";
				String strComment="";
				String strImage="";
				strDate=strMArlistDate.get(inI-1).toString();
				strComment=strMArlistComments.get(inI-1).toString();
				strImage=strMArListImageurls.get(inI-1).toString();
				objdb=new Database(getApplicationContext());
				objdb.openTowrite();
				objdb.insert(strDate, strComment, strImage);
				objdb.close();
			}
		}
		else if (!strSharedvalue.equals(strPreferenceprevalue))
		{			     	
			for(int inR=1;inR<=strMArlistDate.size();inR++)
			{
				int inEvaluate=0;
				int inPreferencesize=0;
				String strValuepost="";
				strValuepost=strMArlistDate.get(inR-1).toString()+"nichu"+strMArlistComments.get(inR-1).toString()
						+"nichu"+strMArListImageurls.get(inR-1);	
				for(Map.Entry<String, ?>entry:msharedpreference.getAll().entrySet())
				{	
					inPreferencesize++;					
					String strPreferValue="";
					strPreferValue=entry.getValue().toString();
					if(strPreferValue!="")
					{
						if(!strPreferValue.equals(strValuepost))
						{
							inEvaluate++;
						}
					}
				}
				if(inPreferencesize==inEvaluate)
				{
					//insert  strValuepost to db and new araylist							
					String strDate="";
					String strComment="";
					String strImage="";
					strDate=strMArlistDate.get(inR-1).toString();
					strComment=strMArlistComments.get(inR-1).toString();
					strImage=strMArListImageurls.get(inR-1).toString();
					objdb=new Database(getApplicationContext());
					objdb.openTowrite();
					objdb.insert(strDate,strComment,strImage);
					objdb.close();	
					strMArListPrefDate.add(strDate);
					strMArListPrefComments.add(strComment);
					strMArListPrefImageurls.add(strImage);
				}
			}
			if(!strMArListPrefDate.isEmpty())
			{
				UpdateCheckService.this.clearpreference();
				for(int inI=1;inI<=strMArListPrefDate.size();inI++)
				{
					String strValuepost="";
					strValuepost=strMArListPrefDate.get(inI-1).toString()+"nichu"+strMArListPrefComments.get(inI-1).toString()
							+"nichu"+strMArListPrefImageurls.get(inI-1);				
					String strKeypost="";
					strKeypost="post"+(inI);
					UpdateCheckService.this.savepreference(strKeypost,strValuepost);
				}

			}	

		}

	}	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}
	@SuppressLint("SimpleDateFormat")
	public void getCommunityInformation() 
	{
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
			jsonuser = "{\"data\":" + facebook.request(parameters) + "}";
			JSONObject JObj = new JSONObject(jsonuser);
			jArr = JObj.getJSONArray("data");
			strPic = new String[jArr.length()];
			strCmd = new String[jArr.length()];
			strDte = new String[jArr.length()];				
			for(int inI =0 ; inI<jArr.length(); inI++)
			{
				System.out.println(""+inI);
				JOmessage = jArr.getJSONObject(inI);
				strMessage = JOmessage.getString("message");				
				strDate = JOmessage.getString("updated_time");
				long lngTimeStamp = Long.valueOf(strDate);
				Date dteGetDate = new Date(lngTimeStamp * 1000L);
				SimpleDateFormat sdfTimeFormatter = new SimpleDateFormat("dd-MM-yyyy  hh:mm aa");
				String strGetTime = sdfTimeFormatter.format(dteGetDate);	
				Log.i(SharePicMain.TAG,"UpdateCheckService : DATE="+strGetTime);
				JSONObject JOattachment = JOmessage.getJSONObject("attachment");
				if(JOattachment.getJSONArray("media") != null)
				{
					jArr2 = JOattachment.getJSONArray("media");
					JOmessage2 = jArr2.getJSONObject(0);
					strSrc = JOmessage2.getString("src");	
				}
				strCmd[inI] = ("\t"+strMessage);
				strDte[inI] = ("\n\tDate :"+strGetTime+"\n");
				strPic[inI]=strSrc;	
				strMArListImageurls.add(strPic[inI]);
				strMArlistComments.add(strCmd[inI]);
				strMArlistDate.add(strDte[inI]);
				strMarListCommentsdate.add(strCmd[inI]+""+strDte[inI]);				
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
	private void clearpreference()
	{
		SharedPreferences msharedpreference=getSharedPreferences("Mpref", MODE_PRIVATE);
		SharedPreferences.Editor editor=msharedpreference.edit();
		editor.clear();
		editor.commit();
	}
	private void savepreference(String key,String value)
	{		
		SharedPreferences msharedpreference=getSharedPreferences("Mpref", MODE_PRIVATE);
		SharedPreferences.Editor editor=msharedpreference.edit();
		editor.putString(key, value);
		editor.commit();
	}
	@Override
	public IBinder onBind(Intent intent) 
	{		
		return null;
	}
}