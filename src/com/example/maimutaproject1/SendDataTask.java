package com.example.maimutaproject1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.util.Log;

// class which send data asynchronous
class SendDataTask extends AsyncTask<String, Void, String> {
	
	private MainActivity mainActivity;
	private DMSTestActivity dmsTestActivity;
	private DNMSTestActivity dnmsTestActivity;
	private TrainingProgramTestActivity trainingProgramTestActivity;
	
	private int testType;
	
	private String fileTitle;
	
	private boolean problemSendingDatas;

    public SendDataTask(DMSTestActivity a)
    {
    	dmsTestActivity = a;
    	testType = SystemUtils.DMS_TEST;
    }
    
    public SendDataTask(TrainingProgramTestActivity a)
    {
    	trainingProgramTestActivity = a;
    	testType = SystemUtils.TRAINING_PROGRAM_TEST;
    }
    
    public SendDataTask(DNMSTestActivity a)
    {
    	dnmsTestActivity = a;
    	testType = SystemUtils.DNMS_TEST;
    } 
    
    public SendDataTask(MainActivity a)
    {
    	mainActivity = a;
    	testType = 0;
    } 

	@Override
	protected String doInBackground(String... params) {
		
		WebServicesConfig wS = new WebServicesConfig();
		
		String res = "";
		
		problemSendingDatas = false;

		try 
		{		
			fileTitle = URLEncoder.encode(params[0], "UTF-8");
			
			String data = URLEncoder.encode("fileTitle", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
			data += "&" + URLEncoder.encode("testData", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
			data += "&" + URLEncoder.encode("sendResultByEmail", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

			// Send data
			URL url = new URL(wS.getUrl()+"?f=createTest");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) 
			{
				res = res + line;
			}
			wr.close();
			rd.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
			problemSendingDatas = true;
			
			if (testType == SystemUtils.DMS_TEST)
			{
				dmsTestActivity.showToastDataSent("Problème lors de l'envoi des données",false);
			}
			else if (testType == SystemUtils.TRAINING_PROGRAM_TEST)
			{
				trainingProgramTestActivity.showToastDataSent("Problème lors de l'envoi des données",false);
			}
			else if (testType == SystemUtils.DNMS_TEST)
			{
				dnmsTestActivity.showToastDataSent("Problème lors de l'envoi des données",false);
			}
			else
			{
				mainActivity.showToastDataSent("Problème lors de l'envoi des données",false);
			}
		}
		
		return res;
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if (!problemSendingDatas)
		{
			System.out.println("Done");
			System.out.println("Result : "+result);
			
			// we notifiate the current activity that the datas has been sent successfully
			if (testType == SystemUtils.DMS_TEST)
			{
				dmsTestActivity.showToastDataSent("Données bien envoyées",true);
			}
			else if (testType == SystemUtils.TRAINING_PROGRAM_TEST)
			{
				trainingProgramTestActivity.showToastDataSent("Données bien envoyées",true);
			}
			else if (testType == SystemUtils.DNMS_TEST)
			{
				dnmsTestActivity.showToastDataSent("Données bien envoyées",true);
			}
			else
			{
				mainActivity.showToastDataSent("Données bien envoyées. Nom du fichier : "+fileTitle,true);
			}
		}
		
    }
 }


