package com.samizic.practice.ImageGallery.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class Downloader
{
	private static Downloader _downloader = null;

	private Downloader(){}

	public static Downloader getInstance()
	{
		if(_downloader == null)
		{
			_downloader = new Downloader();
		}
		return _downloader;
	}

	public void downloadImage(ImageView imgView, String url)
	{
		ImageDownloaderTask downloadTask = new ImageDownloaderTask(imgView, url);
		downloadTask.execute();
	}

	private Drawable doImageDownload(String urlString) throws IOException
	{
		URL url = new URL(urlString);
		InputStream inputStream = (InputStream) url.getContent();
		Drawable drawable = Drawable.createFromStream(inputStream, "src name");

		return drawable;
	}

	private class ImageDownloaderTask extends AsyncTask<Void, Void, Drawable>
	{
		private WeakReference<ImageView> _imageViewReference;
		private String _url;

		public ImageDownloaderTask(ImageView imageView, String url)
		{
			_imageViewReference = new WeakReference<ImageView>(imageView);
			_url = url;
		}

		@Override
		protected Drawable doInBackground(Void... params)
		{
			Drawable drawable = null;
			try
			{
				drawable = doImageDownload(_url);
			}
			catch (IOException e)
			{
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}

			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable drawable)
		{
			ImageView imageView = _imageViewReference.get();

			if (imageView != null)
			{
				if(drawable != null)
				{
					imageView.setImageDrawable(drawable);
				}
			}
		}
	}

}
