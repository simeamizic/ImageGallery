package com.samizic.practice.ImageGallery.activity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.samizic.practice.ImageGallery.R;
import com.samizic.practice.ImageGallery.util.Downloader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home extends Activity
{
	private Button _button;
	private ImageView _imageView;
	private ProgressBar _progressDialog;
	private final Handler _handler = new Handler();
	private Runnable _delayedImageLoadingTask;
	private List<String> _imageUrls = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);

        initViewComponents();

		JSONObject json = readDataFromJson();
		loadImageUrlsFromJson(json);


		String url = "http://3.bp.blogspot.com/-l1mzUO5W_OQ/TzRiloBeV9I/AAAAAAAAAMc/rZbLSPMCpko/s1600/android_boot_image_black2-774483.png";
		loadImageFromWeb(url);
		hideLoadingBar();
	}

	private void loadImageUrlsFromJson(JSONObject json)
	{
		try
		{
			JSONArray imageUrlArray = json.getJSONArray("images");

			StringBuilder stringBuilder = new StringBuilder();
            int length = imageUrlArray.length();

			for(int i=0; i<length; i++)
			{
				JSONObject item = imageUrlArray.getJSONObject(i);
				String uri = item.getString("uri");
				String set = item.getString("set");
				stringBuilder.append(uri).append(set);

				_imageUrls.add(stringBuilder.toString());
				stringBuilder.delete(0, stringBuilder.length()-1);
			}

		} catch (JSONException e)
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}


	}

	private JSONObject readDataFromJson()
	{
		AssetManager assetManager = getResources().getAssets();
		InputStream inStream = null;
		try
		{
			inStream = assetManager.open("mobile_advert.json");
			InputStreamReader inStreamReader = new InputStreamReader(inStream);
			BufferedReader bufferedReader = new BufferedReader(inStreamReader);
			StringBuilder stringBuilder = new StringBuilder();

			String line;
			while( (line = bufferedReader.readLine()) != null)
			{
				stringBuilder.append(line);
			}

			String inputString = stringBuilder.toString();
			JSONObject json = new JSONObject(inputString);
			return json;
		}
		catch (Exception e)
		{
			if(e.getClass().equals(IOException.class))
			{
				Toast.makeText(this, "Erorr opening file", Toast.LENGTH_LONG);
			}
			else if(e.getClass().equals(JSONException.class))
			{
				Toast.makeText(this, "Erorr opening file", Toast.LENGTH_LONG);
			}
            e.printStackTrace();
			return null;
		}
	}

	private void initViewComponents()
	{
		_progressDialog = (ProgressBar)	findViewById(R.id.progressBar);
		_imageView 		= (ImageView)	findViewById(R.id.imageView);
		_button 		= (Button)		findViewById(R.id.button);
		_imageView.setImageDrawable(getResources().getDrawable(R.drawable.noimageavailable));
	}

	private void loadImageFromWeb(String url)
	{
		Downloader downloader = Downloader.getInstance();
		downloader.downloadImage(_imageView, url);
	}


	public void closeActivity(View view)
	{
		finish();
	}

	private void hideLoadingBar()
	{
		if(_progressDialog != null && _progressDialog.getVisibility() == View.VISIBLE)
		{
			_progressDialog.setVisibility(View.GONE);
		}
	}

	private void showLoadingBar()
	{
		if(_progressDialog != null && (_progressDialog.getVisibility() == View.INVISIBLE || _progressDialog.getVisibility() == View.GONE))
		{
			_progressDialog.setVisibility(View.VISIBLE);
		}
	}

	private void onImageDownloaded(Drawable drawable)
	{
		hideLoadingBar();

		if(drawable != null)
		{
			_imageView.setImageDrawable(drawable);
		}
		else
		{
			Toast.makeText(this, "Error loading image", Toast.LENGTH_LONG);
		}
	}


}
