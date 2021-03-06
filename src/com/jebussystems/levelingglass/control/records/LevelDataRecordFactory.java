package com.jebussystems.levelingglass.control.records;

import com.jebussystems.levelingglass.control.MeterType;
import com.jebussystems.levelingglass.util.LogWrapper;

public class LevelDataRecordFactory
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final String TAG = "leveldatarecord.factory";

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

	public static LevelDataRecord createLevelDataRecord(MeterType meterType,
	        int channel)
	{
		LogWrapper.v(TAG,
		        "LevelDataRecordFactory::createLevelDataRecord enter",
		        "meterType=", meterType, "channel=", channel);

		LevelDataRecord record = null;

		switch (meterType)
		{
			case DIGITALPEAK:
				record = new DigitalPeakDataRecord(channel);
				break;
			case PPM:
				record = new PPMDataRecord(channel);
				break;
			case VU:
				record = new VUDataRecord(channel);
				break;
			case NONE:
				// do nothing
				break;
			default:
				LogWrapper.wtf(TAG, "unknown meter type=", meterType);
				break;
		}

		LogWrapper.v(TAG,
		        "LevelDataRecordFactory::createLevelDataRecord exit record=",
		        record);
		return record;
	}

}
