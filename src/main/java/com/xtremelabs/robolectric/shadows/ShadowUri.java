package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.net.*;

import java.io.*;

import com.xtremelabs.robolectric.internal.*;

@Implements(Uri.class)
public class ShadowUri {
	@RealObject Uri uri;
    private File file;

    public static Uri fromFile(File file) {
    	Uri res = newInstanceOf(Uri.class);
    	((ShadowUri)shadowOf_(res)).file = file;
    	return res;
    }
    
    @Override
    public boolean equals(Object obj) {
    	return this.file.equals(((ShadowUri)obj).file);
    }
    
    
}
