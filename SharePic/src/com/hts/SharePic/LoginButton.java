package com.hts.SharePic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import com.hts.SharePic.Facebook.DialogListener;
import com.hts.SharePic.SessionEvents.AuthListener;
import com.hts.SharePic.SessionEvents.LogoutListener;

public class LoginButton extends ImageButton
	{
		private Facebook mFb;
		private Handler mHandler;
		private final SessionListener mSessionListener = new SessionListener();
		private String[] mPermissions;
		private Activity mActivity;
		public LoginButton(Context context)
			{
				super(context);
			}
		public LoginButton(Context context, AttributeSet attrs)
			{
				super(context, attrs);
			}
		public LoginButton(Context context, AttributeSet attrs, int defStyle)
			{
				super(context, attrs, defStyle);
			}
		public void init(final Activity activity, final Facebook fb)
			{
				init(activity, fb, new String[]{});
			}
		public void init(final Activity activity, final Facebook fb, final String[] permissions)
			{
				mActivity = activity;
				mFb = fb;
				mPermissions = permissions;
				mHandler = new Handler();

				setBackgroundColor(Color.TRANSPARENT);
				setAdjustViewBounds(false);
				setImageResource(fb.isSessionValid() ? R.drawable.button_logout : R.drawable.button_login);
				drawableStateChanged();		
				SessionEvents.addAuthListener(mSessionListener);
				SessionEvents.addLogoutListener(mSessionListener);
				setOnClickListener(new ButtonOnClickListener());
			}
		private final class ButtonOnClickListener implements OnClickListener
			{
				public void onClick(View arg0)
					{
						if (mFb.isSessionValid())
							{
								SessionEvents.onLogoutBegin();
								AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
								asyncRunner.logout(getContext(), new LogoutRequestListener());
							}
						else
							{
								mFb.authorize(mActivity, mPermissions, new LoginDialogListener());
							}
					}
			}
		private final class LoginDialogListener implements DialogListener
			{
				public void onComplete(Bundle values)
					{
						SessionEvents.onLoginSuccess();
					}
				public void onFacebookError(FacebookError error)
					{
						SessionEvents.onLoginError(error.getMessage());
					}
				public void onError(DialogError error)
					{
						SessionEvents.onLoginError(error.getMessage());
					}

				public void onCancel()
					{
						SessionEvents.onLoginError("Action Canceled");
					}
			}

		private class LogoutRequestListener extends BaseRequestListener
			{
				public void onComplete(String response, final Object state)
					{
						mHandler.post(new Runnable()
							{
								public void run()
									{
										SessionEvents.onLogoutFinish();
									}
							});
					}
			}

		private class SessionListener implements AuthListener, LogoutListener
		{
				public void onAuthSucceed()
					{
						setImageResource(R.drawable.button_logout);
						SessionStore.save(mFb, getContext());
					}
				public void onAuthFail(String error)
					{
					
					}
				public void onLogoutBegin()
					{
					}

				public void onLogoutFinish()
					{
						SessionStore.clear(getContext());
						setImageResource(R.drawable.button_login);
					}
			}

	}
