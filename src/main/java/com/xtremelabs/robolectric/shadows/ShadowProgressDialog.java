package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.app.*;
import android.content.*;

import com.xtremelabs.robolectric.internal.*;

@Implements(ProgressDialog.class)
public class ShadowProgressDialog {
	public static final String TAG = ShadowProgressDialog.class.getSimpleName();
	
	@RealObject ProgressDialog pd;
	public CharSequence title;
	public CharSequence message;
	public boolean indeterminate;
	public boolean cancelable;
	public DialogInterface.OnCancelListener cancelListener;
	private boolean dismissed;
	
	public static ShadowProgressDialog last = null;
	
	@Implementation
	public static ProgressDialog show (Context context, CharSequence title, CharSequence message) {
		return show(context, title, message, false);
	}
	
	@Implementation
	public static ProgressDialog show (Context context, CharSequence title, CharSequence message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false, null);
	}
	
	@Implementation
	public static ProgressDialog show (Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable, null);
	}
	
	@Implementation
	public static ProgressDialog show (Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
		ProgressDialog res = newInstanceOf(ProgressDialog.class);
		ShadowProgressDialog shadow = shadowOf_(res);
		
		shadow.pd = res;
		shadow.title = title;
		shadow.message = message;
		shadow.indeterminate = indeterminate;
		shadow.cancelable = cancelable;
		shadow.cancelListener = cancelListener;
		
		last = shadow;
		
		return res;
	}
	
	public void triggerCancel() {
		if (cancelListener != null) cancelListener.onCancel(this.pd);
	}

	public static void reset() {
		last = null;
	}
	
	public static ShadowProgressDialog getLastProgressDialog() {
		return last;
	}
	
	@Implementation
	public void dismiss() {
		dismissed = true;
	}
	
	public boolean isDismissed() {
		return dismissed;
	}
}
