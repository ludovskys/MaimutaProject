package com.example.maimutaproject1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

// parameters activity of a dms test
public class BeforeDMSOrDNMSTestActivity extends Activity {
	
	EditText editTextMaxNbTrials, editTextMaxTime,
				editTextDelayPhase1,editTextDelayBetweenPhase1And2;
	
	TextView textViewDmsParams;
	
	Button buttonStartDMSTest;
	
	CheckBox checkBoxOtherChance;
	
	int testType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_before_dms_or_dnms_test);
		
		Bundle infos = getIntent().getExtras();
		
		testType = infos.getInt("testType", 0);
		
		editTextMaxNbTrials = (EditText) findViewById(R.id.editTextMaxNbTrials);
		editTextMaxTime = (EditText) findViewById(R.id.editTextMaxTime);
		editTextDelayPhase1 = (EditText) findViewById(R.id.editTextDelayPhase1);
		editTextDelayBetweenPhase1And2 = (EditText) findViewById(R.id.editTextDelayBetweenPhase1And2);
		
		textViewDmsParams = (TextView) findViewById(R.id.textViewDmsParams);
		
		if (testType == SystemUtils.DMS_TEST)
		{
			textViewDmsParams.setText(R.string.dms_params);
		}
		else if (testType == SystemUtils.DNMS_TEST)
		{
			textViewDmsParams.setText(R.string.dnms_params);
		}
		
		checkBoxOtherChance = (CheckBox) findViewById(R.id.checkBoxOtherChance);
		
		// when we give the focus to the text view "max trial"
		editTextMaxNbTrials.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				// if we have the focus
				if (hasFocus)
				{
					// we change the text view "max time" and we set 0
					// reason : we CANT set a max number of tests AND a maximum time of tests, it's unlogical
					editTextMaxTime.setText("0");
				}
				
			}
		});
		
		// same think for this textview
		editTextMaxTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus)
				{
					editTextMaxNbTrials.setText("0");
				}
				
			}
		});
		
		buttonStartDMSTest = (Button) findViewById(R.id.buttonStartDMSTest);
		
		// FOR TEST
		editTextMaxNbTrials.setText("4");
		editTextDelayPhase1.setText("1000");
		editTextDelayBetweenPhase1And2.setText("1000");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brefore_dmstest, menu);
		return true;
	}
	
	// click on the start button
	public void buttonStartDMSTestClicked(View v)
	{
		// parameters checking
		boolean editTextMaxNbTrialsIsFilled = !editTextMaxNbTrials.getText().toString().equalsIgnoreCase("");
		boolean editTextMaxTimeIsFilled = !editTextMaxTime.getText().toString().equalsIgnoreCase("");
		boolean editTextDelayPhase1IsFilled = !editTextDelayPhase1.getText().toString().equalsIgnoreCase("");
		boolean editTextDelayBetweenPhase1And2IsFilled = !editTextDelayBetweenPhase1And2.getText().toString().equalsIgnoreCase("");

		// if one of several edittext wasnt fill
		if (!editTextMaxNbTrialsIsFilled || !editTextMaxTimeIsFilled || 
				 !editTextDelayPhase1IsFilled
				|| !editTextDelayBetweenPhase1And2IsFilled)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle(R.string.error);
			alertDialog.setMessage(getString(R.string.fill_input_text));
			
			alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {

			      }
			});
			
			// error
			alertDialog.show();
		}
		// if we set the max number of trials and the max time of tests with 0 (cant happen but it's better to check)
		else if (editTextMaxNbTrials.getText().toString().equalsIgnoreCase("0") && 
				editTextMaxTime.getText().toString().equalsIgnoreCase("0") )
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle(R.string.error);
			alertDialog.setMessage(getString(R.string.fill_input_text_max));
			
			alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {

			      }
			});
			
			alertDialog.show();
		}
		// if the delay for the phase 1 of the dms test is equal to 0
		else if (editTextDelayPhase1.getText().toString().equalsIgnoreCase("0"))
		{
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setTitle(R.string.error);
			alertDialog.setMessage(getString(R.string.fill_input_text_delay));
			
			alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {

			      }
			});
			
			alertDialog.show();
		}
		else
		{		
			Intent i = null;
			
			if (testType == SystemUtils.DMS_TEST)
			{
				// rdy to go dms test
				i = new Intent(BeforeDMSOrDNMSTestActivity.this, DMSTestActivity.class);
			}
			else if (testType == SystemUtils.DNMS_TEST)
			{
				// rdy to go dnms test
				i = new Intent(BeforeDMSOrDNMSTestActivity.this, DNMSTestActivity.class);
			}
			else
			{
				// error
			}
			
			// maximum time of trials
			int maxTime = Integer.parseInt(editTextMaxTime.getText().toString());
			if (maxTime != 0)
			{
				// we pass this parameter to the next activity
				i.putExtra("maxTime", maxTime);
			}
			
			// number maximum of trials
			int nbMaxTrials = Integer.parseInt(editTextMaxNbTrials.getText().toString());
			if (nbMaxTrials != 0)
			{
				// we pass this parameter to the next activity
				i.putExtra("nbMaxTrials", nbMaxTrials);
			}
			
			int phase1Delay = Integer.parseInt(editTextDelayPhase1.getText().toString());
			
			i.putExtra("phase1Delay", phase1Delay);
			
			int delayBetweenPhase1And2 = Integer.parseInt(editTextDelayBetweenPhase1And2.getText().toString());
			
			i.putExtra("delayBetweenPhase1And2", delayBetweenPhase1And2);
			
			i.putExtra("checkBoxOtherChance", checkBoxOtherChance.isChecked());
			
			startActivity(i);
		}
		
	}
}

