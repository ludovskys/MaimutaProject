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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DNMSTestActivity extends Activity {
	
	RelativeLayout relativeLayoutMain, relativeLayoutChoices;
	
	TextView textViewTrialsWon, textViewTrialsLost;
	
	int nbMaxTrials, maxTime, numberOfTrials, trialsWon, trialsLost, numberOfChoices, numberOfMistakes, numberOfAllMistakes;
	
	int testMode, state;
	
	long chronometerTimeWhenStopped;
	
	// delayBeforePhase1 corresponds to the delay between the beginning of a test and when the first image is displayed
	// phase1Delay corresponds to the delay for which the first image is displayed
	// delayBetweenPhase1And2 corresponds to the delay between the moment when the first image disapears and the other appears
	double delayBeforePhase1, phase1Delay, delayBetweenPhase1And2, phase2Start, phase2End, phase2Delay;
	
	double totalSeconds;
	
	String res;
	
	// will contain the available custom views
	ArrayList<CustomDrawableView> listCustomDrawableViewChoices, listCustomDrawableViewMain;
	
	MediaPlayer mpLetsGo, mpTrialWon, mpTrialLost;
	
	boolean giveAnotherChance;
	
	Date dateTestStart;
	
	ArrayList<Test> arrayListTests;
	
	private SharedPreferences.Editor editor;
	private SharedPreferences settings;
	
	boolean testIsFinished;
	
	Timer t;
	TimerTask task;
	
	Chronometer chronometer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dmstest);
		
		settings = getSharedPreferences(SystemUtils.PREFS_TEST, 0);
        editor = settings.edit();
        
        testIsFinished = false;
        
        
        
        chronometerTimeWhenStopped = 0;
		
		Bundle infos = getIntent().getExtras();

		maxTime = infos.getInt("maxTime", 0);
		// display duration of the phase 1 (main view)
		phase1Delay = infos.getInt("phase1Delay", 0);
		delayBetweenPhase1And2 = infos.getInt("delayBetweenPhase1And2", 0);
		
		// false if we go to the next test if the first answer is not good
		//giveAnotherChance = false;
		giveAnotherChance = infos.getBoolean("checkBoxOtherChance", false);
		
		// if the maximum time (in minutes) of the test is set
		if (maxTime != 0)
		{
			testMode = SystemUtils.MAX_TIME_MODE;
		}
		
		nbMaxTrials = infos.getInt("nbMaxTrials", 0);
		
		// if the maximum number of trials is set
		if (nbMaxTrials != 0)
		{
			testMode = SystemUtils.NUMBER_MAX_TRIALS_MODE;
		}
		
		arrayListTests = new ArrayList<Test>();
		
		delayBeforePhase1 = 2000;

		final Handler h=new Handler();
		
		// initialisation of the sounds
		initSounds();

		relativeLayoutMain = (RelativeLayout)findViewById(R.id.relativeLayoutMain);
		relativeLayoutChoices = (RelativeLayout)findViewById(R.id.relativeLayoutChoices);

		listCustomDrawableViewChoices = new ArrayList<CustomDrawableView>();
		listCustomDrawableViewMain = new ArrayList<CustomDrawableView>();
		
		// TODO : parametre before
		for (int i = 0; i < 4; i++)
		{
			CustomDrawableView c = new CustomDrawableView(this);
			relativeLayoutChoices.addView(c);
			listCustomDrawableViewChoices.add(c);
		}
		
		for (int i = 0; i < 1; i++)
		{
			CustomDrawableView c = new CustomDrawableView(this);
			c.setIsTheMainView(true);
			c.setIsLikeTheMainView(false);
			relativeLayoutMain.addView(c);
			listCustomDrawableViewMain.add(c);
		}
		
		textViewTrialsWon = (TextView)findViewById(R.id.textViewTrialsWon);
		textViewTrialsLost = (TextView)findViewById(R.id.textViewTrialsLost);
		
		chronometer = (Chronometer)findViewById(R.id.chronometer);
		
		numberOfAllMistakes = 0;
		numberOfTrials = 0;
		trialsWon = 0;
		trialsLost = 0;
		
		
		
		numberOfChoices = listCustomDrawableViewChoices.size();
		
		// current time
		dateTestStart = Calendar.getInstance().getTime();

		h.post(new Runnable() 
		{
			public void run() 
			{
				// hide the views
				for (CustomDrawableView c : listCustomDrawableViewMain)
				{
					c.setVisibility(View.GONE);
				}
				
				for (CustomDrawableView c : listCustomDrawableViewChoices)
				{
					c.setVisibility(View.GONE);
				}
				
				// changes of the color and form of the view
				initNewTrial();
			}
		});

		for (CustomDrawableView view : listCustomDrawableViewMain)
		{
			// touch listener of the main figure
			view.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					CustomDrawableView view = (CustomDrawableView)v;
					
					if (view.getIsTheMainView())
					{
						
					}
					else
					{
						
					}
					
					return false;
				}
				
			});
		}
		
		
		// for all the choices figure
		for (CustomDrawableView view : listCustomDrawableViewChoices)
		{
			// touch listener of the figure
			view.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					// cast
					final CustomDrawableView view = (CustomDrawableView)v;

					Handler h=new Handler();
					
					h.post(new Runnable() 
					{
						public void run() 
						{
							// if the view chosen is not like the main view, we win
							if (!view.getIsLikeTheMainView())
							{
								phase2End = totalSeconds;
								
								phase2Delay = (phase2End - phase2Start) * 1000.00;

								trialsWon++;
								//textViewTrialsWon.setText("Essaies gagnées : "+trialsWon);
								
								mpTrialWon.start();
								
								// hide the views
								for (CustomDrawableView c : listCustomDrawableViewMain)
								{
									c.setVisibility(View.GONE);
								}
								
								for (CustomDrawableView c : listCustomDrawableViewChoices)
								{
									c.setVisibility(View.GONE);
								}
								
								Test t = new Test(numberOfTrials, numberOfChoices, phase1Delay, delayBetweenPhase1And2, phase2Delay, numberOfMistakes, SystemUtils.DNMS_TEST);
								
								arrayListTests.add(t);
								
								// new trial
								initNewTrial();
							}
							// if we lose
							else
							{
								trialsLost++;
								//textViewTrialsLost.setText("Nombre d'erreur : "+trialsLost);
								
								// playing the sound
								mpTrialLost.start();
								
								// we move to the next trial or we let the user try to choose the good answer ?
								
								// if the user can have another chance after choosing a bad choice
								if (giveAnotherChance)
								{
									// we hide the view who has been chosen
									view.setVisibility(View.GONE);
									
									numberOfMistakes++;
									numberOfAllMistakes++;
								}
								else
								{
									// end of the phase 2
									phase2End = totalSeconds;
									
									// converting into seconds
									phase2Delay = (phase2End - phase2Start) * 1000;
									
									numberOfMistakes++;
									numberOfAllMistakes++;
									
									Test t = new Test(numberOfTrials, numberOfChoices, phase1Delay, delayBetweenPhase1And2, phase2Delay, numberOfMistakes,SystemUtils.DNMS_TEST);
									
									// we add the current test to the list
									arrayListTests.add(t);
									
									// hide all the views
									for (CustomDrawableView c : listCustomDrawableViewMain)
									{
										c.setVisibility(View.GONE);
									}
									
									for (CustomDrawableView c : listCustomDrawableViewChoices)
									{
										c.setVisibility(View.GONE);
									}
									
									// start a new trial
									initNewTrial();
								}
							}
							
							
						}
					});
					
					return false;
				}
				
			});
		}
		
		t=new Timer();
		// The TimerTask class represents a task to run at a specified time. This task is run repeatetly
		task=new TimerTask() 
		{
			double i=0;

			public void run() 
			{
				h.post(new Runnable() 
				{
					public void run() 
					{		
						totalSeconds = i;
					}
				});
				
				// if the test is in progress
				if (state == SystemUtils.STATE_PLAY)
				{
					i = i + 0.01;
				}
				
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dmstest, menu);
		return true; 
	}
	
	private void initSounds()
	{
		// initilisation of the sound "let's go"
		mpLetsGo = MediaPlayer.create(getApplicationContext(), R.raw.sm64_lets_go); 
		mpLetsGo.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpLetsGo.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				
			}
			
		});
		
		// initialisation of the sound "1 up", when we won a trial
		mpTrialWon = MediaPlayer.create(getApplicationContext(), R.raw.smb_1_up); 
		mpTrialWon.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpTrialWon.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				
			}
			
		});
		
		// initialisation of the sound  when we lost a trial
		mpTrialLost = MediaPlayer.create(getApplicationContext(), R.raw.smb_bowserfalls); 
		mpTrialLost.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpTrialLost.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				
			}
			
		});
	}
	
	// function who initialize a new trial
	private void initNewTrial()
	{
		state = SystemUtils.STATE_PLAY;
		
		numberOfMistakes = 0;
		
		RelativeLayout.LayoutParams layoutParams;
		
		int screenWidth = SystemUtils.getScreenWidth(this);
		int screenHeight = SystemUtils.getScreenHeight(this);
		
		// if we set a maximum of tests before the test
		if (testMode == SystemUtils.MAX_TIME_MODE)
		{
			// seconds between now and the beginning of the test
			//long seconds = (dateNow.getTime()-dateTestStart.getTime())/1000;
			
			// if we reach the max time (max time * 60 because max time is in minutes)
			if (totalSeconds > maxTime * 60)
			{
				// the test is finished
				testIsFinished = true;
				
				buildResult();
				
				state = SystemUtils.STATE_FINISH;

				// we show the res
				showAlertDialogRes();
				
				return;
			}
		}
		// if we set a maximum of trials before the test
		else if (testMode == SystemUtils.NUMBER_MAX_TRIALS_MODE)
		{
			// if we reach the number max of trials
			if (nbMaxTrials == numberOfTrials)
			{
				testIsFinished = true;
				buildResult();
				
				state = SystemUtils.STATE_FINISH;
				
				showAlertDialogRes();
				
				return;
			}
		}
		
		IntColor intColor = new IntColor();
		
		int width = screenWidth/7, height= screenWidth/7;
		
		CustomDrawableView main = listCustomDrawableViewMain.get(0);
		
		// update of the main view
		main.setCustomView(new CustomDrawableView(getApplicationContext(), intColor, width, height));
		// width and height
		layoutParams = new RelativeLayout.LayoutParams(width, height);
		// set position
		layoutParams.setMargins(screenWidth/2-width/2,screenHeight/8,0,0);
		main.setLayoutParams(layoutParams);
		
		listCustomDrawableViewMain.set(0, main);
		
		int numberOfChoices = listCustomDrawableViewChoices.size();
		
		Random r = new Random();
		int randomNumber = r.nextInt(numberOfChoices);
		
		// loop of the choices view
		for (int i= 0; i < numberOfChoices; i++)
		{
			CustomDrawableView view = listCustomDrawableViewChoices.get(i);
			// one of these view wont be like the main view
			if (randomNumber == i)
			{
				// we generate a new view
				CustomDrawableView newView = new CustomDrawableView(getApplicationContext(), intColor, width, height);
	
				view.setCustomView(newView);				
				view.setIsLikeTheMainView(false);
				view.setIsTheMainView(false);
			}
			else
			{
				// this one will be like the main view !
				view.setCustomView(listCustomDrawableViewMain.get(0));
				view.setIsLikeTheMainView(true);
				view.setIsTheMainView(false);
				
			}
			
			// width and height
			layoutParams = new RelativeLayout.LayoutParams(width, height);
			// set position
		    layoutParams.setMargins((screenWidth/4)*i+(width/2),0,0,0);
		    view.setLayoutParams(layoutParams);
			
			// update in the list
			listCustomDrawableViewChoices.set(i, view);
		}
		
		Handler h=new Handler();
		
		// thread which will be launch after a delay 
		h.postDelayed(new Runnable() 
		{
			public void run() 
			{
				// the main view will be displays after a delay
				for (CustomDrawableView c : listCustomDrawableViewMain)
				{
					c.setVisibility(View.VISIBLE);
				}
			}
		},(int)delayBeforePhase1);
		
		
		h.postDelayed(new Runnable() 
		{
			public void run() 
			{
				// the main view will be displays after a delay
				for (CustomDrawableView c : listCustomDrawableViewMain)
				{
					c.setVisibility(View.GONE);
				}
			}
		},(int)(delayBeforePhase1 + phase1Delay));
		
		h.postDelayed(new Runnable() 
		{
			
			public void run() 
			{
				phase2Start = totalSeconds;
				for (CustomDrawableView view : listCustomDrawableViewChoices)
				{
					// the choices will be displays after a delay
					view.setVisibility(View.VISIBLE);
				}
			}
		},(int)(delayBeforePhase1 + phase1Delay + delayBetweenPhase1And2));
		
		numberOfTrials++;
		
		// if it's the first test
		if (numberOfTrials == 1)
		{
			mpLetsGo.start();
			
			// launching of the timer
			t.schedule(task,0,10);
		}

		chronometer.start();
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// if the button "back" is pressed
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	// pause to the test
	    	state = SystemUtils.STATE_PAUSE;
	    	
	    	// we retrieve the value of the chronometer
	    	chronometerTimeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
	    	chronometer.stop();
	    	
	    	// we open a alert box
	    	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle("");
			alertDialog.setMessage(getString(R.string.confirm_cancel));
			
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Arrêter", new DialogInterface.OnClickListener() {
				// if we choose to stop the test
				public void onClick(DialogInterface dialog, int which) {
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometerTimeWhenStopped = 0;
						  
					finish();
				}
			});
			
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
				// if we choose to continue the test
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
	
	private void buildResult()
	{
		String resTest = "";
		
		for (Test t : arrayListTests)
		{
			//Log.d("info", t.toString());
			resTest = resTest + t.toString();
		}
		
		// if we let the user try another possibility after a mistake
		if (giveAnotherChance)
		{
			// no need to calculate the pourcentage of winning		
		}
		else
		{
			double winningPourcentage; 
			
			NumberFormat formatter = new DecimalFormat("0.00");
			
			// if no errors at all
			if (numberOfAllMistakes == 0)
			{
				winningPourcentage = 100;
			}
			else
			{
				winningPourcentage = ((numberOfAllMistakes * 100.0 / numberOfTrials * 100.0) / 100.00);
				winningPourcentage = 100 - winningPourcentage;
			}
			
			res = "DNMS Test \r\n" +
					"Nombre de tests : "+numberOfTrials+" \r\n" +
					"Temps total : "+ formatter.format(totalSeconds) +" secondes \r\n" +
					"Pourcentage de réussite : "+winningPourcentage+"% \r\n \r\n"+ resTest;
		}
		
		Handler h=new Handler();
		
		// if we currently have an internet connection
		final boolean isOnline = SystemUtils.isOnline(this);
		
		h.post(new Runnable() 
		{
			public void run() 
			{
				Date d = Calendar.getInstance().getTime();
				
				SimpleDateFormat formatDateJourTitle = new SimpleDateFormat("dd_MM_yyyy_kk_mm_ss");
				
				SimpleDateFormat formatDateJourTitle2 = new SimpleDateFormat("dd/MM/yyyy à kk:mm:ss");
				
				String dateToString = formatDateJourTitle.format(d); 
				
				String dateToString2 = formatDateJourTitle2.format(d); 

				String title = "DNMSTest_"+dateToString; 
				
				String title2 = "DNMS Test : "+dateToString2;
				
				ProcessExecuterModule processExecuterModule = new ProcessExecuterModule();
				
				// if we have a connection to send the result
				if (isOnline)
				{
					String sendResultByEmail = (settings.getBoolean("sendResultByEmail", false)) ? "0" : "1";
					
					Log.d("info", "online");
					// send datas
					// TODO
					processExecuterModule.runSendTestData(DNMSTestActivity.this,title,res,sendResultByEmail,title2);
				}
				else
				{
					showToastDataSent("Envoi des données impossible : pas de connexion à Internet");
					
					// TODO : save the result in the app
					/*
					Log.d("info", "nicht online");
					
					editor.putString("fileTitle1", title);
					editor.putString("testData1", res);
					
					editor.commit();*/
				}
				
				
			}
		});
		
	}
	
	// alertdialog shown when the test has finished
	private void showAlertDialogRes()
	{
		chronometer.stop();
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		
		alertDialog.setTitle(R.string.resultats);
		alertDialog.setMessage(res);
		
		alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		    	  finish();
		      }
		});
		
		alertDialog.show();
	}
	
	// function called in SendDataTask
	public void showToastDataSent(final String content)
	{
		Handler h=new Handler();
		
		h.postDelayed(new Runnable() 
		{
			public void run() 
			{
				Log.d("info",content);
				Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
			}
		},1000);
		
	}

}
