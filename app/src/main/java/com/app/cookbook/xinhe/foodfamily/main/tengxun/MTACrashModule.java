package com.app.cookbook.xinhe.foodfamily.main.tengxun;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatTrackLog;

import android.content.Context;
import android.util.Log;

public class MTACrashModule {
	public static void initMtaCrashModule(Context app){
		StatCrashReporter crashReporter = StatCrashReporter.getStatCrashReporter(app);
		crashReporter.setEnableInstantReporting(true);
		crashReporter.setJavaCrashHandlerStatus(true);
		crashReporter.setJniNativeCrashStatus(true);
		
		StatTrackLog.log("init module");
		StatConfig.setCrashKeyValue("myTag", "myValue");
		
		crashReporter.addCrashCallback(new StatCrashCallback() {

			@Override
			public void onJniNativeCrash(String tombstoneString) { 

				Log.d("app", "MTA StatCrashCallback onJniNativeCrash:\n" + tombstoneString);
			}

			@Override
			public void onJavaCrash(Thread thread, Throwable ex) {
				Log.d("app", "MTA StatCrashCallback onJavaCrash:\n", ex);
			}
		});
	}
}
