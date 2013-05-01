package com.example.maimutaproject1;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Test {
	
	// TODO distinguer DMS et Training Program
	
	private int testNumber;
	
	// PROPRE A DMS
	private int numberOfChoices;
	
	// delay of exposition of the main view
	private double phase1Delay;

	// delay between the moment when the main view disapears and the choices appear
	private double delayBetweenPhase1And2;
	
	// delay between the moment when the view appears and the user choose the good response
	private double phase2Delay;
	
	private double numberOfMistakes;
	
	
	// PROPRE A TRAINING PROGRAM
	private double testLevel;
	
	private double delay;
	
	
	private int testType;
	
	// uses for dms test
	public Test(int testNumber, int numberOfChoices, double phase1Delay, double delayBetweenPhase1And2, double phase2Delay, double numberOfMistakes, int testType)
	{
		this.testNumber = testNumber;
		this.numberOfChoices = numberOfChoices;
		this.phase1Delay = phase1Delay;
		this.delayBetweenPhase1And2 = delayBetweenPhase1And2;
		this.phase2Delay = phase2Delay;
		this.numberOfMistakes = numberOfMistakes;
		this.testType = testType;
	}
	
	public Test(int testNumber, double testLevel, double delay, int testType)
	{
		this.testNumber = testNumber;
		this.testLevel = testLevel;
		this.delay = delay;
		this.testType = testType;
	}

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public int getNumberOfChoices() {
		return numberOfChoices;
	}

	public void setNumberOfChoices(int numberOfChoices) {
		this.numberOfChoices = numberOfChoices;
	}

	public double getPhase1Delay() {
		return phase1Delay;
	}

	public void setPhase1Delay(double phase1Delay) {
		this.phase1Delay = phase1Delay;
	}

	public double getDelayBetweenPhase1And2() {
		return delayBetweenPhase1And2;
	}

	public void setDelayBetweenPhase1And2(double delayBetweenPhase1And2) {
		this.delayBetweenPhase1And2 = delayBetweenPhase1And2;
	}

	public double getPhase2Delay() {
		return phase2Delay;
	}

	public void setPhase2Delay(double phase2Delay) {
		this.phase2Delay = phase2Delay;
	}
	
	@Override
	public String toString()
	{
		String res = "";
		
		NumberFormat formatter = new DecimalFormat("0.00");
		
		if (this.testType == SystemUtils.DMS_TEST || this.testType == SystemUtils.DNMS_TEST)
		{
			res = res + "N° essai : "+ testNumber + "\r\n";
			res = res + "Nombre de choix : "+ numberOfChoices + "\r\n";
			res = res + "Délai phase 1 : "+ formatter.format(phase1Delay) + " ms\r\n";
			res = res + "Délai entre phase 1 et 2 : "+ formatter.format(delayBetweenPhase1And2) + " ms\r\n";
			res = res + "Délai phase 2 (temps de réponse) : "+ formatter.format(phase2Delay) + " ms\r\n \r\n";
			//res = res + "Nombre d'erreurs : "+ numberOfMistakes + "\r\n \r\n";
		}
		else if (this.testType == SystemUtils.TRAINING_PROGRAM_TEST)
		{
			res = res + "Niveau n° : "+ testLevel + "\r\n";
			res = res + "Temps de réponse : "+ formatter.format(delay) + " ms \r\n \r\n";
		}
		
		
		
		return res;
	}

	public double getNumberOfMistakes() {
		return numberOfMistakes;
	}

	public void setNumberOfMistakes(double numberOfMistakes) {
		this.numberOfMistakes = numberOfMistakes;
	}

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public double getTestLevel() {
		return testLevel;
	}

	public void setTestLevel(double testLevel) {
		this.testLevel = testLevel;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
	

}
