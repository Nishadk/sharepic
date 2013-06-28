package com.hts.SharePic;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


public class Loaddynamically extends AsyncTask<Void,Void,List<rowitems>>
{
	/* private final int mFrom;
    private final int mTo;*/
	private final Context mContext;
	private final IconicAdapter mAdapter;
	private boolean mReachedLastPage;
	public static int inItemscount;
	private static int inTotalitems;
	LoadPic objmyLoadclass;
	ArrayList<String> Listimageurl=new ArrayList<String>();
	ArrayList<String> ListcommentsDateall=new ArrayList<String>();
	ProgressBar myprogressLoad;
	public Loaddynamically(Context context, IconicAdapter adapter,ArrayList<String> Listimage,ArrayList<String> ListcommentDate,ProgressBar progressLoad,int in)
	{    
		myprogressLoad=progressLoad;    	
		inItemscount=in;
		Log.i("NISHAD%%%%%loaddynamiclly","itemsCount="+inItemscount);
		mContext = context;
		mAdapter = adapter;
		mReachedLastPage = false;
		objmyLoadclass=new LoadPic();
		/*inTotalitems=objmyLoadclass.strMArlstSavedCommentsDate.size();*/
		inTotalitems=ListcommentDate.size();
		Log.i("NISHAD************","Size="+inTotalitems);
		for(String img:Listimage)
		{
			Listimageurl.add(img);
		}
		for(String CMT:ListcommentDate)
		{
			ListcommentsDateall.add(CMT);
		}
	}
	public Loaddynamically(Context context, IconicAdapter adapter,ArrayList<String> Listimage,ArrayList<String> ListcommentDate,int in)
	{  
		inItemscount=in;   
		Log.i("NISHAD@@@@@111","itemsCount="+inItemscount);
		mContext = context;
		mAdapter = adapter;
		mReachedLastPage = false;
		objmyLoadclass=new LoadPic();
		/*inTotalitems=objmyLoadclass.strMArlstSavedCommentsDate.size();*/
		inTotalitems=ListcommentDate.size();
		Log.i("NISHAD************","Size="+inTotalitems);
		for(String img:Listimage)
		{
			Listimageurl.add(img);
		}
		for(String CMT:ListcommentDate)
		{
			ListcommentsDateall.add(CMT);
		}
	}
	@Override
	protected List<rowitems> doInBackground(Void... voids)
	{
		List<rowitems> list = new ArrayList<rowitems>();
		try
		{  	
			for (int i =inItemscount; i < inTotalitems+1 ; i++) 
			{
				//Log.i("NISHAD","DYNAMICLLy frloop:="+);
				if(inItemscount==inTotalitems-1)
				{
					mReachedLastPage = true;
					break;
				}
				// String strcommentsDate ="test";objmyLoadclass.strMArlstSavedCommentsDate.get(inItemscount).toString();
				/*String imageurl =objmyLoadclass.strMArlstSavedImageurls.get(inItemscount).toString();*/
				String strcommentsDate =ListcommentsDateall.get(i).toString();
				String imageurl =Listimageurl.get(i).toString();
				list.add(new rowitems(strcommentsDate, imageurl));   
				inItemscount=inItemscount+1;
			} 
			Log.i("NISHAD@@@@@","itemsCount="+inItemscount);
		}
		catch (Exception e)
		{
			Log.e("EA_DEMO","Error fetching product list",e);
		}
		return list;
	}
	@Override
	protected void onPostExecute(List<rowitems> products) 
	{
		super.onPostExecute(products);      
		for (rowitems p : products)
		{
			mAdapter.add(p);
		}
		myprogressLoad.setVisibility(View.GONE);
		mAdapter.notifyDataSetChanged();
		if (mReachedLastPage)
		{       	 
			mAdapter.notifyNoMoreItems();
		}
	}
}

