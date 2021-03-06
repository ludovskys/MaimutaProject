package com.example.maimutaproject1;

// class which run tasks
public class ProcessExecuterModule {
	
	public void runSendTestData(DMSTestActivity a, String title, String res, String sendResultByEmail, String title2, String xmlResults)
	{
		SendDataTask sendDataTask = new SendDataTask(a);
		sendDataTask.execute(title,res,sendResultByEmail,title2,xmlResults);
	}
	
	public void runSendTestData(TrainingProgramTestActivity a, String title, String res, String sendResultByEmail, String title2,String xmlResults)
	{
		SendDataTask sendDataTask = new SendDataTask(a);
		sendDataTask.execute(title,res,sendResultByEmail,title2,xmlResults);
	}
	
	public void runSendTestData(DNMSTestActivity a, String title, String res, String sendResultByEmail, String title2, String xmlResults)
	{
		SendDataTask sendDataTask = new SendDataTask(a);
		sendDataTask.execute(title,res,sendResultByEmail,title2,xmlResults);
	}
	
	public void runSendTestData(MainActivity a, String title, String res, String sendResultByEmail, String title2 ,String xmlResults)
	{
		SendDataTask sendDataTask = new SendDataTask(a);
		sendDataTask.execute(title,res,sendResultByEmail,title2,xmlResults);
	}

}
