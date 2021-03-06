package com.example.timemanagement.statistics;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Class to draw the piechart
 * @author Marcus
 *
 */
public class View_PieChart extends View {
	public static final int WAIT = 0;
	 public static final int IS_READY_TO_DRAW = 1;
	 public static final int IS_DRAW = 2;
	 private static final float START_INC = 30;
	 private Paint mBagpaints = new Paint();
	 private Paint mLinePaints = new Paint();

	 private int mWidth;
	 private int mHeight;
	 private int mGapTop;
	 private int mGapBottm;
	 private int mBgcolor;
	 private int mGapleft;
	 private int mGapright;
	 private int mState = WAIT;
	 private float mStart;
	 private float mSweep;
	 private int mMaxConnection;
	 private List<PieDetailsItem> mdataArray;

	 public View_PieChart(Context context) {
	  super(context);	  
	 }

	 public View_PieChart(Context context, AttributeSet attr) {
	  super(context, attr);	  
	 }

	 	/**
	 	 * Draws the pie chart
	 	 */
	 @Override
	 protected void onDraw(Canvas canvas) {
	  super.onDraw(canvas);
	  if (mState != IS_READY_TO_DRAW) {
	   return;
	  }
	  canvas.drawColor(mBgcolor);
		if (mMaxConnection > 0) {
			mBagpaints.setAntiAlias(true);
			mBagpaints.setStyle(Paint.Style.FILL);
			mBagpaints.setColor(0x88FF0000);
			mBagpaints.setStrokeWidth(0.0f);
			mLinePaints.setAntiAlias(true);
			mLinePaints.setColor(0xff000000);
			mLinePaints.setStrokeWidth(3.0f);
			mLinePaints.setStyle(Paint.Style.STROKE);
			RectF mOvals = new RectF(mGapleft, mGapTop, mWidth - mGapright,
					mHeight - mGapBottm);
			mStart = START_INC;
			PieDetailsItem item;
			for (int i = 0; i < mdataArray.size(); i++) {
				item = mdataArray.get(i);
				mBagpaints.setColor(item.color);
				mSweep = 360 * ((float) item.count / (float) mMaxConnection);
				canvas.drawArc(mOvals, mStart, mSweep, true, mBagpaints);
				canvas.drawArc(mOvals, mStart, mSweep, true, mLinePaints);
				mStart = mStart + mSweep;
			}
		}

	  mState = IS_DRAW;
	 }
	 
	 /**
	  * Sets geometry of the chart
	  * @param width
	  * @param height
	  * @param gapleft
	  * @param gapright
	  * @param gaptop
	  * @param gapbottom
	  * @param overlayid
	  */
	 public void setGeometry(int width, int height, int gapleft, int gapright,
	   int gaptop, int gapbottom, int overlayid) {

	  mWidth = width;
	  mHeight = height;
	  mGapleft = gapleft;
	  mGapright = gapright;
	  mGapBottm = gapbottom;
	  mGapTop = gaptop;

	 }
	 
	 /**
	  * Sets the color
	  * @param bgcolor Color of the Pie Chart
	  */
	 public void setSkinparams(int bgcolor) {
	  	  mBgcolor = bgcolor;
	 }

	 	/**
	 	 * Set the data of the chart
	 	 * @param data This is each item and its properties
	 	 * @param maxconnection This is the total of what is being represented
	 	 */
	 public void setData(List<PieDetailsItem> data, int maxconnection) {
	  mdataArray = data;
	  mMaxConnection = maxconnection;	  
	  mState = IS_READY_TO_DRAW;
	 }

	 public void setState(int state) {
	  mState = state;
	 }

	 	/**
	 	 * Get the color values to be drawn
	 	 * @param index Which item
	 	 * @return Returns an int that describes the color
	 	 */
	 public int getColorValues(int index) {
	  if (mdataArray == null) {
	   return 0;
	  }

	  else if (index < 0)
	   return mdataArray.get(0).color;
	  else if (index > mdataArray.size())
	   return mdataArray.get(mdataArray.size() - 1).color;
	  else
	   return mdataArray.get(mdataArray.size() - 1).color;

	 }
}
