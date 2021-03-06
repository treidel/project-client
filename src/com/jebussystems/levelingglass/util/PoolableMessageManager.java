package com.jebussystems.levelingglass.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.ObjectPool;

public class PoolableMessageManager
{
	// /////////////////////////////////////////////////////////////////////////
	// constants
	// /////////////////////////////////////////////////////////////////////////

	private static final String TAG = "until.poolablemessagemanager";

	// /////////////////////////////////////////////////////////////////////////
	// types
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// class variables
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// object variables
	// /////////////////////////////////////////////////////////////////////////

	private Map<Class<?>, ObjectPool<? extends PoolableMessage>> poolMap = new HashMap<Class<?>, ObjectPool<? extends PoolableMessage>>();

	// /////////////////////////////////////////////////////////////////////////
	// static initialization
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// constructors
	// /////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////
	// public methods
	// /////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public <T extends PoolableMessage> T allocateMessage(Class<T> messageClass)
	{
		ObjectPool<? extends PoolableMessage> pool = this.poolMap
		        .get(messageClass);
		try
		{
			return (T) pool.borrowObject();
		}
		catch (Exception e)
		{
			LogWrapper.wtf(TAG, e);
			return null;
		}
	}

	public <T extends PoolableMessage> void registerPool(Class<T> messageClass,
	        ObjectPool<T> pool)
	{
		this.poolMap.put(messageClass, pool);
	}

	// /////////////////////////////////////////////////////////////////////////
	// inner classes
	// /////////////////////////////////////////////////////////////////////////

}
