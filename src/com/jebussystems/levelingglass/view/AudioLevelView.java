package com.jebussystems.levelingglass.view;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jebussystems.levelingglass.R;

public class AudioLevelView extends View
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final String TAG = "view.audiolevel";

	private static final int DEFAULT_CEILING = 0;
	private static final int DEFAULT_FLOOR = -24;
	private static final int DEFAULT_LEVEL = DEFAULT_FLOOR;
	private static final int DEFAULT_NORMAL_COLOR = Color.GREEN;
	private static final int DEFAULT_ERROR_COLOR = Color.RED;
	private static final int DEFAULT_HOLD_COLOR = Color.GRAY;
	private static final int DEFAULT_MARK_COLOR = Color.DKGRAY;

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// variables
	// /////////////////////////////////////////////////////////////////////////

	// variables populated by attribute or at runtime
	private final int floor;
	private final int ceiling;
	private final Set<Integer> marks = new HashSet<Integer>();
	private float level;
	private Float hold;

	// internal paint variables
	private Paint okPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint errorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint holdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint markPaint = new Paint();

	// internally calculated variables populated by onSizeChanged() whenever the
	// view size changes
	private Rect okRange;
	private Rect errorRange;
	private Rect holdRange;

	// internally calculated variables used by the paint routine
	private final Rect okRect = new Rect();
	private final Rect errorRect = new Rect();
	private final Rect holdRect = new Rect();
	private final Collection<Path> markPaths = new LinkedList<Path>();

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	public AudioLevelView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		Log.v(TAG, "AudioLevelView::AudioLevelView enter context=" + context
		        + " attrs=" + attrs);

		// initialize our attributes using the info from the attribute set
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		        R.styleable.AudioLevel, 0, 0);
		try
		{
			int okColor = a.getColor(R.styleable.AudioLevel_ok_color,
			        DEFAULT_NORMAL_COLOR);
			int errorColor = a.getColor(R.styleable.AudioLevel_error_color,
			        DEFAULT_ERROR_COLOR);
			int holdColor = a.getColor(R.styleable.AudioLevel_hold_color,
			        DEFAULT_HOLD_COLOR);
			int markColor = a.getColor(R.styleable.AudioLevel_mark_color,
			        DEFAULT_MARK_COLOR);
			this.floor = a.getInt(R.styleable.AudioLevel_floor, DEFAULT_FLOOR);
			this.ceiling = a.getInt(R.styleable.AudioLevel_ceiling,
			        DEFAULT_CEILING);
			this.level = a
			        .getFloat(R.styleable.AudioLevel_level, DEFAULT_LEVEL);
			this.hold = a.getFloat(R.styleable.AudioLevel_hold, DEFAULT_LEVEL);
			String marks = a.getString(R.styleable.AudioLevel_marks);
			if (null != marks)
			{
				// marks are comma separated integers
				StringTokenizer tokenizer = new StringTokenizer(marks, ",");
				while (true == tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					this.marks.add(Integer.valueOf(token));
				}
			}

			// set the paint to use
			this.okPaint.setColor(okColor);
			this.errorPaint.setColor(errorColor);
			this.holdPaint.setColor(holdColor);
			this.markPaint.setColor(markColor);
			this.markPaint.setStrokeWidth(1f);
			this.markPaint.setStyle(Paint.Style.STROKE);
			this.markPaint.setStrokeJoin(Paint.Join.BEVEL);
		}
		finally
		{
			a.recycle();
		}

		Log.v(TAG, "AudioLevelView::AudioLevelView exit");
	}

	// /////////////////////////////////////////////////////////////////////////
	// public methods
	// //////////////////////////////////////////s///////////////////////////////

	public void setLevel(float level)
	{
		Log.v(TAG, "AudioLevelView::setLevel enter level=" + level);
		if (this.level != level)
		{
			// set the level
			this.level = level;
			// force a recalculation of the dynamic values
			updateDynamicValues();
			// redraw
			invalidate();
		}
		Log.v(TAG, "AudioLevelView::setLevel exit");
	}

	public float getLevel()
	{
		return level;
	}

	public void setHold(Float hold)
	{
		Log.v(TAG, "AudioLevelView::setHold enter hold=" + hold);
		if (this.hold != hold)
		{
			// set the level
			this.hold = hold;
			// force a recalculation of the dynamic values
			updateDynamicValues();
			// redraw
			invalidate();
		}
		Log.v(TAG, "AudioLevelView::setHold exit");
	}

	public Float getHold()
	{
		return hold;
	}

	public int getCeiling()
	{
		return ceiling;
	}

	public int getFloor()
	{
		return floor;
	}

	// /////////////////////////////////////////////////////////////////////////
	// View overrides
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public void onSizeChanged(int width, int height, int oldwidth, int oldheight)
	{
		Log.v(TAG, "AudioLevelView::onSizeChanged enter width=" + width
		        + " height=" + height + " oldwidth=" + oldwidth + " oldheight="
		        + oldheight);
		// calculate the offsets
		int xOffset = getPaddingLeft();
		int yOffset = getPaddingTop();
		int xRange = width - getPaddingLeft() - getPaddingRight();
		int yRange = height - getPaddingTop() - getPaddingBottom();

		// calculate how 'wide' the level space is
		int span = getCeiling() - getFloor();

		// calculate the maximum widths of each band
		int okRange = (int) (Math.abs(getFloor()) * (float) xRange / (float) span);

		// calculate space for the marks
		int yMark = (int) ((float) yRange * 0.15);

		// calculate the ok range
		this.okRange = new Rect(xOffset, yOffset + yMark, xOffset + okRange,
		        yOffset + yRange - yMark);
		this.errorRange = new Rect(this.okRange.right, this.okRange.top,
		        xOffset + xRange, this.okRange.bottom);
		this.holdRange = new Rect(this.okRange.left, this.okRange.top,
		        this.errorRange.right, this.okRange.bottom);

		// clear any existing marks
		this.markPaths.clear();

		// iterate through all marks to draw
		for (int mark : this.marks)
		{
			int markPosition = xOffset
			        + (((mark - getFloor()) * xRange) / span);
			{
				Path topPath = new Path();
				topPath.moveTo(markPosition, yOffset);
				topPath.lineTo(markPosition, yOffset + yMark - 1);
				this.markPaths.add(topPath);
			}
			{
				Path bottomPath = new Path();
				bottomPath.moveTo(markPosition, yOffset + yRange - yMark);
				bottomPath.lineTo(markPosition, yOffset + yRange - 1);
				this.markPaths.add(bottomPath);
			}
		}

		// calculate the drawing rects
		updateDynamicValues();

		Log.v(TAG, "AudioLevelView::onSizeChanged exit");
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		Log.v(TAG, "AudioLevelView::onDraw enter canvas=" + canvas);
		// call the super class
		super.onDraw(canvas);
		// draw the error rect first so that the ok rect will draw over it if
		// necessary
		canvas.drawRect(this.errorRect, this.errorPaint);
		canvas.drawRect(this.okRect, this.okPaint);
		canvas.drawRect(this.holdRect, this.holdPaint);
		for (Path path : this.markPaths)
		{
			canvas.drawPath(path, this.markPaint);
		}
		Log.v(TAG, "AudioLevelView::onDraw exit");
	}

	// /////////////////////////////////////////////////////////////////////////
	// package protected methods
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// protected methods
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////

	private void updateDynamicValues()
	{
		// figure out the range of possible levels
		int span = getCeiling() - getFloor();

		// see if we're above or below the zero level
		if (getLevel() < 0f)
		{
			// no error rectangle needed
			this.errorRect.setEmpty();
			// // calculate the percent of pixels to draw
			float percent = 1.0f - ((float) getLevel() / ((float) getFloor()));
			// handle clipping
			percent = Math.max(percent, 0.0f);
			// calculate the number of pixels we need to draw
			int pixels = (int) ((float) this.okRange.width() * percent);
			// size the drawing rectangle
			this.okRect.set(this.okRange.left, this.okRange.top,
			        this.okRange.left + pixels, this.okRange.bottom);
		}
		else
		{
			// normal rectangle is full
			this.okRect.set(this.okRange);
			// calculate the percent of pixels to draw
			float percent = ((float) getLevel() / ((float) getCeiling()));
			// handle clipping
			percent = Math.min(percent, 1.0f);
			// calculate the number of pixels we need to draw
			int pixels = (int) ((float) this.errorRange.width() * percent);

			// size the drawing rectangle
			this.errorRect.set(this.errorRange.left, this.errorRange.top,
			        this.errorRange.left + pixels, this.errorRange.bottom);
		}
		// if we have a hold then calculate where it should be placed
		if (null != getHold())
		{
			// calculate the percentage point of the hold
			float percent = (getHold() - getFloor()) / span;
			// handle clipping
			percent = Math.min(percent, 1.0f);
			percent = Math.max(percent, 0.0f);
			// calculate the number of pixels in we need to draw
			int pixels = (int) ((float) this.holdRange.width() * percent);
			// size the drawing rectangle
			this.holdRect.set(this.holdRange.left + pixels, this.holdRange.top,
			        this.holdRange.left + pixels + 1, this.holdRange.bottom);
		}
		else
		{
			// clear the rect
			this.holdRect.setEmpty();
		}
	}
}
