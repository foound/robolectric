package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.net.*;

import java.io.*;

import com.xtremelabs.robolectric.internal.*;

@Implements(Uri.class)
public class ShadowUri {
	@RealObject Uri uri;
    private String s;

    @Implementation
    public static Uri fromFile(File file) {
    	Uri res = newInstanceOf(Uri.class);
    	((ShadowUri)shadowOf_(res)).s = "file://" + file.getAbsolutePath();
    	return res;
    }
    
    @Implementation
    public static Uri parse(String s) {
    	Uri res = newInstanceOf(Uri.class);
    	((ShadowUri)shadowOf_(res)).s = s;
    	return res;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return this.s.equals(((ShadowUri)obj).s);
    }
    
    
}
