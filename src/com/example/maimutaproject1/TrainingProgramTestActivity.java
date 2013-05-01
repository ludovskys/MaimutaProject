package com.example.maimutaproject1;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TrainingProgramTestActivity extends Activity {
	
	CustomDrawableView customDrawableViewTrainingProgram;
	
	Chronometer chronometer;
	
	RelativeLayout relativeLayout;
	
	int level;
	
	int x , y, width, height;
	
	boolean touchInsideBox;
	
	MediaPlayer mpLetsGo;
	
	double totalSeconds, trialStart, trialEnd, trialDelay;
	
	int numberOfTrials;
	
	int delayUntilTrialStart, delayUntilFirstTrialStart;
	
	int state;
	
	String res;
	
	ArrayList<Test> arrayListTests;
	
	private SharedPreferences.Editor editor;
	private SharedPreferences settings;
	
	MediaPlayer mpHereWego, mpMarioDies;
	
	Timer t;
	TimerTask task;
	
	long chronometerTimeWhenStopped;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training_program_test);
		
		state = SystemUtils.STATE_PAUSE;
		
		settings = getSharedPreferences(SystemUtils.PREFS_TEST, 0);
        editor = settings.edit();
		
		numberOfTrials = 0;
		
		chronometerTimeWhenStopped = 0;
		
		delayUntilFirstTrialStart = 1000;
		
		//customDrawableViewTrainingProgram = (CustomDrawableView)findViewById(R.id.customDrawableViewTrainingProgram);
		customDrawableViewTrainingProgram = new CustomDrawableView(this);
		chronometer = (Chronometer)findViewById(R.id.chronometer);
		
		touchInsideBox = false;
		
		arrayListTests = new ArrayList<Test>();
		
		relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
		
		// touch listener of the main view
		customDrawableViewTrainingProgram.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				touchInsideBox = true;

				return false;
			}
			
		});
		
		// touch listener of the screen
		relativeLayout.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				Handler h=new Handler();
	
				h.postDelayed(new Runnable(){

					@Override
					public void run() {
						
						// if the test is running
						if (state == SystemUtils.STATE_PLAY)
						{
							trialEnd = totalSeconds;
							
							trialDelay = (trialEnd - trialStart) * 1000.00;
							
							Test t = new Test(numberOfTrials, level, trialDelay, SystemUtils.TRAINING_PROGRAM_TEST);
							arrayListTests.add(t);
							
							// if the box was touched
							if (touchInsideBox)
							{
								level++;
								initNewTrial();
							}
							else
							{	
								// stop the trial
								mpMarioDies.start();
								buildResult();
								//showAlertDialogRes(); 
								
								state = SystemUtils.STATE_FINISH;
							}
							
							touchInsideBox = false;
						}
						
						
					}
					
				},10);
				
				
				
				return false;
			}
			
		});
		
		t=new Timer();
		task=new TimerTask() 
		{
			double i=0;
			public void run() 
			{
				totalSeconds = i;
				if (state == SystemUtils.STATE_PLAY)
				{
					i = i + 0.01;
				}
			}
		};
		
		chronometer.start();
		
		level = 1;
		
		initNewTrial();
		
		initSounds();
		
		mpHereWego.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.training_program_test, menu);
		return true;
	}
	
	public void initNewTrial()
	{
		int screenWidth = SystemUtils.getScreenWidth(this);
		int screenHeight = SystemUtils.getScreenHeight(this);
		
		// we remove the current view
		relativeLayout.removeView(customDrawableViewTrainingProgram);
		
		RelativeLayout.LayoutParams layoutParams;
		
		Random r = new Random();
		
		switch (level)
		{
			case 1:	
				height = (int) (screenHeight*0.8); width = (int) (screenWidth*0.8);
				
				break;
			case 2:
				height = screenWidth/2; width = screenWidth/2;

			    break;
			case 3:
				height = screenWidth/3; width = screenWidth/3;

			    break;  
			case 4:
				height = screenWidth/4; width = screenWidth/4;

			    break;
			case 5:
				height = screenWidth/5; width = screenWidth/5;
				
			    break;
			    
			case 6:
				height = screenWidth/6; width = screenWidth/6;
				
			    break;
			default:
				height = screenWidth/7; width = screenWidth/7;
			    
				break;
		}
		
		x = r.nextInt(screenWidth - width); y = r.nextInt(screenHeight - height -40); 
		
		customDrawableViewTrainingProgram.setCustomView(new CustomDrawableView(getApplicationContext(), 0, 0, width, height));

		layoutParams = new RelativeLayout.LayoutParams(width,height);
		// set position
	    layoutParams.setMargins(x,y,0,0);
	    
	    customDrawableViewTrainingProgram.setLayoutParams(layoutParams);
		
		numberOfTrials++;
		
		Handler h=new Handler();
		
		delayUntilTrialStart = 0;

		if (numberOfTrials == 1)
		{
			delayUntilTrialStart = delayUntilFirstTrialStart;
			
			t.schedule(task,0,10);
		}
		
		h.postDelayed(new Runnable() 
		{
			public void run() 
			{
				state = SystemUtils.STATE_PLAY;
				trialStart = totalSeconds;
				// we add the new one
				relativeLayout.addView(customDrawableViewTrainingProgram);
			}
		},delayUntilTrialStart); // TODO change

	}
	
	private void initSounds()
	{
		// initilisation of the sound "let's go"
		mpHereWego = MediaPlayer.create(getApplicationContext(), R.raw.drm64_mario5); 
		mpHereWego.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpHereWego.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				
			}
			
		});
		
		mpMarioDies = MediaPlayer.create(getApplicationContext(), R.raw.smb_mariodie); 
		mpMarioDies.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpMarioDies.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				
			}
			
		});
	}
	
	private void buildResult()
	{
		String resTest = "";
		
		for (Test t : arrayListTests)
		{
			//Log.d("info", t.toString());
			resTest = resTest + t.toString();
		}
		
		//totalSeconds = totalSeconds - delayUntilFirstTrialStart/1000;
		
		NumberFormat formatter = new DecimalFormat("0.00");
		
		res = "Training Program \r\n" +
				"Nom du testeur : "+settings.getString("userName", "Sans nom") + "\r\n "+
				"Echec au niveau n° "+level+" \r\n" +
				"Temps total : "+ formatter.format(totalSeconds) +" secondes \r\n \r\n"+ resTest;
		
		final boolean isOnline = SystemUtils.isOnline(this);
		
		Handler h=new Handler();
		
		h.post(new Runnable() 
		{
			public void run() 
			{
				Date d = Calendar.getInstance().getTime();
				
				SimpleDateFormat formatDateJourTitle = new SimpleDateFormat("dd_MM_yyyy_kk_mm_ss");
				
				SimpleDateFormat formatDateJourTitle2 = new SimpleDateFormat("dd/MM/yyyy à kk:mm:ss");
				
				String dateToString = formatDateJourTitle.format(d); 
				
				String dateToString2 = formatDateJourTitle2.format(d); 

				String title = "TrainingProgram_"+dateToString;
				
				String title2 = "Training Program : "+dateToString2;
				
				ProcessExecuterModule processExecuterModule = new ProcessExecuterModule();
				
				if (isOnline)
				{
					String sendResultByEmail = (settings.getBoolean("sendResultByEmail", false)) ? "0" : "1";
					
					Log.d("info", "online");
					processExecuterModule.runSendTestData(TrainingProgramTestActivity.this,title,res,sendResultByEmail,title2);
				}
				else
				{
					showToastDataSent("Envoi des données impossible : pas de connexion à Internet",false);
				}
				
				
			}
		});
	}

	private void showAlertDialogRes()
	{
		chronometer.stop();
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle(R.string.resultats);
		alertDialog.setMessage(res);
		
		Log.d("info", "res : "+res);
		
		alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		    	  finish();
		      }
		});
		
		alertDialog.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	state = SystemUtils.STATE_PAUSE;
	    	
	    	chronometerTimeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
	    	chronometer.stop();
	    	
	    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle("");
			alertDialog.setMessage(getString(R.string.confirm_cancel));
			
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Arrêter", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  chronometer.setBase(SystemClock.elapsedRealtime());
			    	  chronometerTimeWhenStopped = 0;
			    	  
			    	  finish();
			      }
			});
			
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  state = SystemUtils.STATE_PLAY;
			    	  
			    	  chronometer.setBase(SystemClock.elapsedRealtime() + chronometerTimeWhenStopped);
			    	  chronometer.start();
			      }
			});

			alertDialog.show(); 
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	// function called in SendDataTask
	public void showToastDataSent(final String content, boolean success)
	{
		if (success)
		{
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d("info",content);
					Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
				}
				
			});
		}
		else
		{
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d("info",content);
					Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
				}
				
			});
			
			Date d = Calendar.getInstance().getTime();
			
			SimpleDateFormat formatDateJourTitle = new SimpleDateFormat("dd_MM_yyyy_kk_mm_ss");
			
			SimpleDateFormat formatDateJourTitle2 = new SimpleDateFormat("dd/MM/yyyy à kk:mm:ss");
			
			String dateToString = formatDateJourTitle.format(d); 
			
			String dateToString2 = formatDateJourTitle2.format(d); 

			String title = "TrainingProgram_"+dateToString;
			
			String title2 = "Training Program : "+dateToString2;
			
			int numberOfWaitingDatas = settings.getInt("numberOfWaitingDatas", 0);
			
			numberOfWaitingDatas++;
								
			editor.putInt("numberOfWaitingDatas", numberOfWaitingDatas);
			
			editor.putBoolean("dataToSend", true);
			editor.putString("fileTitle"+numberOfWaitingDatas, title);
			editor.putString("fileTitle2"+numberOfWaitingDatas, title2);
			editor.putString("testData"+numberOfWaitingDatas, res);
			
			editor.commit();
		}
		
		
		finish();
		
	}

}
