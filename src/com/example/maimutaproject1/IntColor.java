package com.example.maimutaproject1;

import java.util.ArrayList;
import java.util.Random;

// class who contains the hexadecimals values of colors use for the figure
public class IntColor {
	
	public static int BLUE = 0xFF0000FF;
	public static int GREEN = 0xFF00FF00;
	public static int AERO = 0xFF7CB9E8;
	public static int ALMOND = 0xFFEFDECD;
	public static int AMBER = 0xFFFFBF00;
	public static int ARMY_GREEN = 0xFF87A96B;
	public static int AUBURN = 0xFFA52A2A;
	public static int BARBIE_PINK = 0xFFE0218A;
	public static int BITTER_LEMON = 0xFFCAE00D;
	public static int BAZAAR = 0xFF98777B;
	public static int BISTRE_BROWN = 0xFF967117;
	public static int BOSTON_UNIVERSITY_RED = 0xFFCC0000;
	public static int BRONZE = 0xFFCD7F32;

	private ArrayList<Integer> colorList;
	
	public IntColor()
	{
		colorList = new ArrayList<Integer>();
		
		colorList.add(BLUE);
		colorList.add(GREEN);
		colorList.add(AERO);
		colorList.add(ALMOND);
		colorList.add(AMBER);
		colorList.add(ARMY_GREEN);
		colorList.add(AUBURN);
		colorList.add(BARBIE_PINK);
		colorList.add(BITTER_LEMON);
		colorList.add(BAZAAR);
		colorList.add(BISTRE_BROWN);
		colorList.add(BOSTON_UNIVERSITY_RED);
		colorList.add(BRONZE);
	}
	
	// choose a color among the list
	public int chooseRandomColor()
	{
		Random r = new Random();
		int colorListSize = colorList.size();
		// choose a random number
		int randomNumber = r.nextInt(colorListSize);
		// get the color
		int randomColor = colorList.get(randomNumber);
		// remove it from the list (to avoid that 2 colors are the same)
		colorList.remove(randomNumber);
		
		return randomColor;
	}
}
