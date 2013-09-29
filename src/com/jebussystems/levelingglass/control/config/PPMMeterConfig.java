package com.jebussystems.levelingglass.control.config;

import com.jebussystems.levelingglass.control.MeterType;

public class PPMMeterConfig extends MeterConfig implements HoldTimeConfig,
        TrimConfig
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final float TRIM_INCREMENT = 1.0f;

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// class variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// object variables
	// /////////////////////////////////////////////////////////////////////////

	private Integer holdtime;
	private float trim = 0.0f;

	// /////////////////////////////////////////////////////////////////////////
	// static initialization
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	PPMMeterConfig(int channel)
	{
		super(channel);
	}

	protected PPMMeterConfig()
	{

	}

	// /////////////////////////////////////////////////////////////////////////
	// MeterConfig implementation
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public MeterType getMeterType()
	{
		return MeterType.PPM;
	}

	// /////////////////////////////////////////////////////////////////////////
	// HoldTimeConfig implementation
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public Integer getHoldtime()
	{
		return holdtime;
	}

	@Override
	public void setHoldtime(Integer holdtime)
	{
		this.holdtime = holdtime;
	}

	// /////////////////////////////////////////////////////////////////////////
	// TrimConfig implementation
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public float getTrim()
	{
		return trim;
	}

	@Override
	public void setTrim(float trim)
	{
		this.trim = trim;
	}

	@Override
	public void addTrimIncrement()
	{
		this.trim += TRIM_INCREMENT;
	}

	@Override
	public void subtractTrimIncrement()
	{
		this.trim -= TRIM_INCREMENT;
	}

}
