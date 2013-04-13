package com.example.maimutaproject1;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

// class who draw the figures
public class CustomDrawableView extends View 
{
	private ShapeDrawable mDrawable;
	
	// for the dms test : if isTheMainView is true, it means that the current figure is the figure showed in phase 1
	private boolean isTheMainView;
	// for the dms test : if isLikeTheMainView is true, it means that the current figure is the same as the figure showed in phae 1
	private boolean isLikeTheMainView;
	
	public CustomDrawableView(Context context) 
	{
		super(context);
		int x = 0;
		int y =  0;
		int width = 100;
		int height = 100;
		mDrawable = new ShapeDrawable(new OvalShape());
		mDrawable.getPaint().setColor(0xff74AC23);
		mDrawable.setBounds(x, y, x + width, y + height);
	}
	
	public CustomDrawableView(Context context, int x, int y, int width, int height)
	{
		super(context);
		
		isTheMainView = false;
		isLikeTheMainView = false;
		
		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xff74AC23);
		mDrawable.setBounds(x, y, x + width, y + height);
	}
	
	// constructor called when a customdrawableview is create with the xml
	public CustomDrawableView(Context context, AttributeSet attributeSet)
	{
		super(context,attributeSet);
		// coordonnées x y
		
		isTheMainView = false;
		isLikeTheMainView = false;
		
		int x = 0;
		int y =  0;
		int width = 0;
		int height = 0;
		
		final String xmlns="http://schemas.android.com/apk/res/android";
        
        String sHeight = "", sWidth = "";
        // recovery of the height define in the xml file
        sHeight = attributeSet.getAttributeValue(xmlns, "layout_height");
        // the default value is "xxx.0dip" : we remove .0dip because we want an int
        sHeight = sHeight.replace(".0dip", "");
        
        Log.d("info", "sHeight : "+sHeight);

        height = Integer.parseInt(sHeight);
        
        // recovery of the width define in the xml file
        sWidth = attributeSet.getAttributeValue(xmlns, "layout_width");
        sWidth = sWidth.replace(".0dip", "");
        width = Integer.parseInt(sWidth);
		
		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(0xff74AC23);
		mDrawable.setBounds(x, y, x + width, y + height);
	}
	
	// create a figure with a random color
	public CustomDrawableView(Context context, IntColor intColor, int width, int height) 
	{
		super(context);
		
		int x = 0;
		int y =  0;
		
		// Shape available : PathShape, RectShape, ArcShape, OvalShape, RoundRectShape
		Random r = new Random();
		int randomNumber = r.nextInt(4);
		
		// in function of the random number, we create either a ArcShape, or a RoundRectShape, or a RectShape or a OvalShape
		if (randomNumber == 0) 
		{ 
			mDrawable = new ShapeDrawable(new ArcShape(r.nextInt(360),r.nextInt(360)));
		}
		else if (randomNumber == 1) 
		{ 
			float[] roundedCorner = new float[] { r.nextInt(400), r.nextInt(400), r.nextInt(400), r.nextInt(400), 
					r.nextInt(400), r.nextInt(400), r.nextInt(400), r.nextInt(400) };
			
			mDrawable = new ShapeDrawable(new RoundRectShape(roundedCorner, null, null));
		}
		else if (randomNumber == 2)
		{
			// rectangle figure
			mDrawable = new ShapeDrawable(new RectShape());
		}
		else if (randomNumber == 3)
		{
			// round figure
			mDrawable = new ShapeDrawable(new OvalShape());
		}
		
		mDrawable.getPaint().setColor(intColor.chooseRandomColor());
		mDrawable.setBounds(x, y, x + width, y + height);
	}
	
	private ShapeDrawable getShapeDrawable()
	{
		return mDrawable;
	}
	
	public boolean getIsTheMainView()
	{
		return isTheMainView;
	}
	
	public boolean getIsLikeTheMainView()
	{
		return isLikeTheMainView;
	}
	
	public void setShapeDrawable(ShapeDrawable s)
	{
		mDrawable = s;
	}
	
	public void setIsTheMainView(boolean b)
	{
		isTheMainView = b;
	}
	
	public void setIsLikeTheMainView(boolean b)
	{
		isLikeTheMainView = b;
	}
	
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		mDrawable.draw(canvas);
	}
	
	// function who change the color of the current figure
	public void changeRandomColor(IntColor intColor)
	{
		mDrawable.getPaint().setColor(intColor.chooseRandomColor());
	}
	
	public void setCustomView(CustomDrawableView v)
	{
		mDrawable = v.getShapeDrawable();
	}
	
}
