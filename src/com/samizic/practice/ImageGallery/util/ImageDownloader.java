package com.samizic.practice.ImageGallery.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageDownloader
{
	private static final int ERROR = 0;
	private static final int OK = 1;

	private final ConnectivityManager connectivityManager;

	private ImageDownloader _downloader;
	private Thread _downloadTask;
	private MessageHandler _handler;

	private ImageDownloader(Context context)
	{
		Context applicationContext = context.getApplicationContext();
		connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public ImageDownloader getInstance(Context context)
	{
		 if(_downloader == null)
		 {
			 _downloader = new ImageDownloader(context);
		 }
		return _downloader;
	}

	public void load(String url, ImageView imageView)
	{
		_handler = new MessageHandler(imageView);

		_downloadTask = new Thread()
		{
			@Override
			public void run()
			{
				Message msg = new Message();
				msg.what = OK;
				_handler.sendMessage(msg);
			}
		};
	}


	private boolean isConnected()
	{
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	private class MessageHandler extends Handler
	{
		private ImageView _imageView;

		public MessageHandler(ImageView imageView)
		{
			_imageView = imageView;
		}

		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == OK)
			{
				Drawable drawable = new BitmapDrawable();
				_imageView.setImageDrawable(drawable);
			}
			else
			{
				 // display some kind of error
			}
		}
	}
}
