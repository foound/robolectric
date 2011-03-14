package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.graphics.*;

import java.io.*;
import java.util.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.internal.*;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(Bitmap.class)
public class ShadowBitmap {
    @RealObject private Bitmap realBitmap;

    private int width;
    private int height;
    private Bitmap.Config config;
    private boolean mutable;
    private String description = "";
    private int loadedFromResourceId = -1;
    
    private static Bitmap lastCreatedBitmap;
    
    public static Stack<Bitmap> recycledBitmaps = new Stack<Bitmap>();
    private boolean recycled = false;

    @Implementation
    public boolean compress(Bitmap.CompressFormat format, int quality, OutputStream stream) {
        try {
        	DataOutputStream dataOut = new DataOutputStream(stream);
        	dataOut.writeInt(width);
        	dataOut.writeInt(height);
        	dataOut.writeUTF(format.toString());
        	dataOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Implementation
    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap scaledBitmap = Robolectric.newInstanceOf(Bitmap.class);
        ShadowBitmap shadowBitmap = shadowOf(scaledBitmap);
        shadowBitmap.appendDescription("Bitmap (" + width + " x " + height + ")");
        shadowBitmap.setWidth(width);
        shadowBitmap.setHeight(height);
        lastCreatedBitmap = scaledBitmap;
        shadowBitmap.setConfig(config);
        return scaledBitmap;
    }
    
    @Implementation
    public static Bitmap createBitmap(Bitmap bitmap) {
        ShadowBitmap shadowBitmap = shadowOf(bitmap);
        shadowBitmap.appendDescription(" created from Bitmap object");
        return bitmap;   	
    }

    @Implementation
    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Bitmap scaledBitmap = Robolectric.newInstanceOf(Bitmap.class);
        ShadowBitmap shadowBitmap = shadowOf(scaledBitmap);
        shadowBitmap.appendDescription(shadowOf(src).getDescription());
        shadowBitmap.appendDescription(" scaled to " + dstWidth + " x " + dstHeight);
        if (filter) {
            shadowBitmap.appendDescription(" with filter " + filter);
        }
        shadowBitmap.setWidth(dstWidth);
        shadowBitmap.setHeight(dstHeight);
        lastCreatedBitmap = scaledBitmap;
        return scaledBitmap;
    }
    
    @Implementation
    public void recycle() {
    	recycled = true;
      	recycledBitmaps.push(realBitmap);

    }

    public static Bitmap lastCreatedBitmap() {
    	return lastCreatedBitmap;
    }
    
    @Implementation
    public final boolean isRecycled() {
    	return recycled;
    }
    
    @Implementation
    public Bitmap copy(Bitmap.Config config, boolean isMutable) {
    	ShadowBitmap shadowBitmap = shadowOf(realBitmap);
    	shadowBitmap.setConfig(config);
    	shadowBitmap.setMutable(isMutable);
		return realBitmap;    	
    }
    
    @Implementation
    public final Bitmap.Config getConfig() {
		return config;  	
    }
    
    public void setConfig(Bitmap.Config config) {
    	this.config = config;
    }
    
    @Implementation
    public final boolean isMutable() {
    	return mutable;
    }
    
    public void setMutable(boolean mutable) {
    	this.mutable = mutable;
    }
    
    public void appendDescription(String s) {
        description += s;
    }

    public void setDescription(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public static Bitmap create(String name) {
        Bitmap bitmap = Robolectric.newInstanceOf(Bitmap.class);
        shadowOf(bitmap).appendDescription(name);
        return bitmap;
    }

    public void setLoadedFromResourceId(int loadedFromResourceId) {
        this.loadedFromResourceId = loadedFromResourceId;
    }

    public int getLoadedFromResourceId() {
        if (loadedFromResourceId == -1) {
            throw new IllegalStateException("not loaded from a resource");
        }
        return loadedFromResourceId;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Implementation
    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Implementation
    public int getHeight() {
        return height;
    }

    @Override @Implementation
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != ShadowBitmap.class) return false;

        ShadowBitmap that = shadowOf((Bitmap) o);

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override @Implementation
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override @Implementation
    public String toString() {
        return "ShadowBitmap{" +
                "description='" + description + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
    
    public Bitmap getRealBitmap() {
    	return realBitmap;
    }
}
