package com.example.maimutaproject1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class BeforeTrainingProgramTestActivity extends Activity {
	
	Button buttonStartTrainingProgramTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_before_training_program_test);
		
		buttonStartTrainingProgramTest = (Button) findViewById(R.id.buttonStartTrainingProgramTest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.before_training_program_test, menu);
		return true;
	}

	public void buttonStartTrainingProgramTestClicked(View v)
	{
		Intent i = new Intent(BeforeTrainingProgramTestActivity.this, TrainingProgramTestActivity.class);
		
		// go to the test activity
		startActivity(i);
	}

}
