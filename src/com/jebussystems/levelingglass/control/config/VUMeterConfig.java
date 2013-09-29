package com.jebussystems.levelingglass.control.config;

import com.jebussystems.levelingglass.control.MeterType;

public class VUMeterConfig extends MeterConfig implements TrimConfig
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final float TRIM_INCREMENT = 0.1f;

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// class variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// object variables
	// /////////////////////////////////////////////////////////////////////////

	private float trim = 0.0f;

	// /////////////////////////////////////////////////////////////////////////
	// static initialization
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	VUMeterConfig(int channel)
	{
		super(channel);
	}

	protected VUMeterConfig()
	{

	}

	// /////////////////////////////////////////////////////////////////////////
	// MeterConfig implementation
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public MeterType getMeterType()
	{
		return MeterType.VU;
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
