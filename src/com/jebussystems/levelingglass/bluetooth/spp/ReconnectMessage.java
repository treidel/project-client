package com.jebussystems.levelingglass.bluetooth.spp;

import android.bluetooth.BluetoothDevice;

import com.jebussystems.levelingglass.util.LogWrapper;

public class ReconnectMessage implements Runnable
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final String TAG = "bluetooth.spp.reconnectmessage";

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// class variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// object variables
	// /////////////////////////////////////////////////////////////////////////

	private final SPPManager manager;
	private BluetoothDevice device = null;

	// /////////////////////////////////////////////////////////////////////////
	// static initialization
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	public ReconnectMessage(SPPManager manager)
	{
		LogWrapper.v(TAG, "ReconnectMessage::ReconnectMessage enter", "this=",
		        this);
		this.manager = manager;
		LogWrapper.v(TAG, "ReconnectMessage::ReconnectMessage exit");
	}

	// /////////////////////////////////////////////////////////////////////////
	// public methods
	// /////////////////////////////////////////////////////////////////////////

	public void init(BluetoothDevice device)
	{
		this.device = device;
	}

	public void release()
	{
		this.device = null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Runnable implementation
	// /////////////////////////////////////////////////////////////////////////

	@Override
	public void run()
	{
		LogWrapper.v(TAG, "ReconnectMessage::process enter", "this=", this);
		// fire the state machine event
		manager.getStateMachineInstance().evaluate(SPPManager.Event.TIMER,
		        device);
		LogWrapper.v(TAG, "ReconnectMessage::run exit");
	}

	// /////////////////////////////////////////////////////////////////////////
	// inner classes
	// /////////////////////////////////////////////////////////////////////////

}
