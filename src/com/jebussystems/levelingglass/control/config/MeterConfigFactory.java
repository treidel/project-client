package com.jebussystems.levelingglass.control.config;

import com.jebussystems.levelingglass.control.MeterType;
import com.jebussystems.levelingglass.util.LogWrapper;

public class MeterConfigFactory
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final String TAG = "meterconfig.factory";

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// class variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// object variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// static initialization
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// public method implementations
	// /////////////////////////////////////////////////////////////////////////

	public static MeterConfig createMeterConfig(MeterType meterType, int channel)
	{
		LogWrapper.v(TAG, "MeterConfigFactory::createMeterConfig enter",
		        "meterType=", meterType, "channel=", channel);

		MeterConfig config = null;

		switch (meterType)
		{
			case DIGITALPEAK:
				config = new DigitalPeakMeterConfig(channel);
				break;
			case PPM:
				config = new PPMMeterConfig(channel);
				break;
			case VU:
				config = new VUMeterConfig(channel);
				break;
			case NONE:
				config = new NoneMeterConfig(channel);
				break;
			default:
				LogWrapper.wtf(TAG, "unknown meter type=", meterType);
				break;
		}

		LogWrapper.v(TAG, "MeterConfigFactory::createMeterConfig exit config=",
		        config);
		return config;
	}

}
