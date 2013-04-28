package com.example.maimutaproject1;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static int CONTEXT_MENU_CHOOSE_TYPE_CHOICE_1 = 1;
	private static int CONTEXT_MENU_CHOOSE_TYPE_CHOICE_2 = 2;
	private static int CONTEXT_MENU_CHOOSE_TYPE_CHOICE_3 = 3;
	
	Button buttonStartTest;
	
	EditText editTextName;
	
	MediaPlayer mpIFeelIt, mpThankYou;
	
	private SharedPreferences.Editor editor;
	private SharedPreferences settings;
	
	CheckBox checkBoxSendResultByEmail;
	
	AlertDialog alertDialogNameMissing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editTextName = (EditText)findViewById(R.id.editTextName);
		buttonStartTest = (Button)findViewById(R.id.buttonStartTest);
		registerForContextMenu(buttonStartTest);
		
		checkBoxSendResultByEmail = (CheckBox)findViewById(R.id.checkBoxSendResultByEmail);
		
		alertDialogNameMissing = new AlertDialog.Builder(this).create();
		
		// listener of the "start the test" button
		buttonStartTest.setOnClickListener(new OnClickListener(){

			@Override 
			public void onClick(View v) {
				// we open the context menu
				if (editTextName.getText().toString().equalsIgnoreCase(""))
				{				
					alertDialogNameMissing.setTitle("Erreur");
					alertDialogNameMissing.setMessage("Veuillez entrer votre prénom");
					
					alertDialogNameMissing.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {

					      }
					});
					
					alertDialogNameMissing.show();
				}
				else
				{
					openContextMenu(v);
				}
			}
			
		});
		
		initSounds();

	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		settings = getSharedPreferences(SystemUtils.PREFS_TEST, 0);
        editor = settings.edit();
        
        // the checkbox will be check if the user check it in the last session
        checkBoxSendResultByEmail.setChecked(settings.getBoolean("sendResultByEmail", false));
		
		// if there are data to send
        if (settings.getBoolean("dataToSend", false) && SystemUtils.isOnline(this))
        {
        	sendWaitingDatas();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override  
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	{  
		super.onCreateContextMenu(menu, v, menuInfo);  
		menu.setHeaderTitle(R.string.context_menu_choose_type);  
		menu.add(0, CONTEXT_MENU_CHOOSE_TYPE_CHOICE_1, 0, R.string.context_menu_choose_type_choice_1);  
		menu.add(0, CONTEXT_MENU_CHOOSE_TYPE_CHOICE_2, 0, R.string.context_menu_choose_type_choice_2);  
		menu.add(0, CONTEXT_MENU_CHOOSE_TYPE_CHOICE_3, 0, R.string.context_menu_choose_type_choice_3); 
	} 
	
	private void initSounds()
	{
		mpIFeelIt = MediaPlayer.create(getApplicationContext(), R.raw.i_feel_it); 
		mpIFeelIt.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpIFeelIt.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
		});
		// lecture du son
		//mpIFeelIt.start();
		
		//stopSounds(mpIFeelIt,5000);
		
		// permet d'initialiser un son
		mpThankYou = MediaPlayer.create(getApplicationContext(), R.raw.sm64_thank_you); 
		mpThankYou.setOnPreparedListener(new OnPreparedListener() 
		{
            public void onPrepared(MediaPlayer mp) {
                
            }
        });
		mpThankYou.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener()
		{

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
		}); 
		
		
	}
	
	@Override  
	public boolean onContextItemSelected(MenuItem item) 
	{  
		boolean res = false;
		if (!editTextName.getText().toString().equalsIgnoreCase(""))
		{
			editor.putString("userName", editTextName.getText().toString());
			editor.commit();
			
			if(item.getItemId() == CONTEXT_MENU_CHOOSE_TYPE_CHOICE_1)
			{
				// training program
				
				Intent i = new Intent(MainActivity.this, BeforeTrainingProgramTestActivity.class);
				startActivity(i);
				res = true;
			}
			else if(item.getItemId() == CONTEXT_MENU_CHOOSE_TYPE_CHOICE_2)
			{
				// dms
				
				Intent i = new Intent(MainActivity.this, BeforeDMSOrDNMSTestActivity.class);
				i.putExtra("testType", SystemUtils.DMS_TEST);
				startActivity(i);
				res = true;
			}
			else if(item.getItemId() == CONTEXT_MENU_CHOOSE_TYPE_CHOICE_3)
			{
				// dnms
				
				Intent i = new Intent(MainActivity.this, BeforeDMSOrDNMSTestActivity.class);
				i.putExtra("testType", SystemUtils.DNMS_TEST);
				startActivity(i);
				res = true;
			}
			else 
			{
				res = false;
			}
		}
		else
		{	
			res = false;
		}

		return res; 
	}
	
	// when the checkbox is clicked
	public void onCheckboxSendEmailClicked(View v)
	{
		if (checkBoxSendResultByEmail.isChecked())
		{
			// we want to send the result by email
			editor.putBoolean("sendResultByEmail", true);
		}
		else
		{
			editor.putBoolean("sendResultByEmail", false);
		}
		
		editor.commit();
	}
	
	// function who send the waiting datas
	public void sendWaitingDatas()
	{
		ProcessExecuterModule processExecuterModule = new ProcessExecuterModule();
		
		String sendResultByEmail = (settings.getBoolean("sendResultByEmail", false)) ? "0" : "1";
		
		int numberOfWaitingDatas = settings.getInt("numberOfWaitingDatas", 0);
		
		while (numberOfWaitingDatas > 0)
		{
			processExecuterModule.runSendTestData(MainActivity.this,settings.getString("fileTitle"+numberOfWaitingDatas, ""),
					settings.getString("testData"+numberOfWaitingDatas, ""),sendResultByEmail,
					settings.getString("fileTitle2"+numberOfWaitingDatas, ""));
			
			numberOfWaitingDatas--; 
		}
		
		editor.putInt("numberOfWaitingDatas", 0);
		editor.putBoolean("dataToSend", false);
		
		editor.commit();  
	} 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			mpThankYou.start();
		}
		
	    return super.onKeyDown(keyCode, event);
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
