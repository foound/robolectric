package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.graphics.*;

import java.io.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.internal.*;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(Bitmap.class)
public class ShadowBitmap {
    @RealObject private Bitmap realBitmap;

    private int width;
    private int height;
    private String description = "";
    private int loadedFromResourceId = -1;

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
        return scaledBitmap;
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
        return scaledBitmap;
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
}
