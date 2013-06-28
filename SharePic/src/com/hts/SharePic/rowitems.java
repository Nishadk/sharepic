package com.hts.SharePic;

public class rowitems 
{
	public final String commentsanddate;
	public final String imageurl;
	public rowitems(String commentsanddate, String imageurl) 
	{
		this.commentsanddate = commentsanddate;
		this.imageurl = imageurl;	
	}
	@Override
	public String toString() 
	{
		return  commentsanddate +"IMG"+ imageurl;
	}
}
