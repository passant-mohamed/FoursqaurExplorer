package com.wasalny.demo;
 
import android.app.Application;
import android.os.Handler;

import com.wasalny.demo.database.DatabaseManager;

public class AppController extends Application {
	
	private static final int THREAD_POOL_SIZE = 3;
 
	private static AppController instance;
	private Handler mHandler;
	 
	
	public static AppController getInstance() {
		return instance;
	}

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		mHandler = new Handler();
	
		DatabaseManager.getInstance().initializeTables();
		
		
		
		
	}

	 
	
}
