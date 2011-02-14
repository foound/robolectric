package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.content.res.*;
import android.graphics.*;
import android.graphics.BitmapFactory.Options;
import android.net.*;

import java.io.*;
import java.util.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.internal.*;
import com.xtremelabs.robolectric.util.*;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(BitmapFactory.class)
public class ShadowBitmapFactory {
    private static Map<String, Point> widthAndHeightMap = new HashMap<String, Point>();
    
    public static Stack<BitmapFactory.Options> recentRunsOptions = new Stack<BitmapFactory.Options>();
    
    @Implementation
    public static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = create("resource:" + getResourceName(id));
        shadowOf(bitmap).setLoadedFromResourceId(id);
        return bitmap;
    }

    private static String getResourceName(int id) {
        return shadowOf(Robolectric.application).getResourceLoader().getNameForId(id);
    }

    @Implementation
    public static Bitmap decodeFile(String pathName) {
        return create("file:" + pathName);
    }

    @Implementation
    public static Bitmap decodeFile(String pathName, BitmapFactory.Options options) {
        return create("file:" + pathName, options);
    }
    
    @Implementation
    public static Bitmap decodeByteArray (byte[] data, int offset, int length) {
    	return decodeByteArray(data, offset, length, new Options());
    }
    
    @Implementation
    public static Bitmap decodeByteArray (byte[] data, int offset, int length, BitmapFactory.Options opts) {
    	DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
    	Bitmap bitmap = Robolectric.newInstanceOf(Bitmap.class);
        ShadowBitmap shadowBitmap = shadowOf(bitmap);

		try {
			int width = dataIn.readInt();
			int height = dataIn.readInt();
			shadowBitmap.setWidth(width);
			shadowBitmap.setHeight(height);
			
			String format = dataIn.readUTF();
			
			opts.outWidth = width;
			opts.outHeight = height;
			
			opts.outMimeType = "image/" + format.toLowerCase();
			
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
    }

    @Implementation
    public static Bitmap decodeStream(InputStream is) {
        return decodeStream(is, null, new BitmapFactory.Options());
    }

    @Implementation
    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        return create(is.toString().replaceFirst("stream for ", ""), opts);
    }

    static Bitmap create(String name) {
        return create(name, new BitmapFactory.Options());
    }

    public static Bitmap create(String name, BitmapFactory.Options options) {
        Bitmap bitmap = Robolectric.newInstanceOf(Bitmap.class);
        ShadowBitmap shadowBitmap = shadowOf(bitmap);
        shadowBitmap.appendDescription("Bitmap for " + name);

        String optionsString = stringify(options);
        if (optionsString.length() > 0) {
            shadowBitmap.appendDescription(" with options ");
            shadowBitmap.appendDescription(optionsString);
        }

        Point widthAndHeight = widthAndHeightMap.get(name);
        if (widthAndHeight == null) {
            widthAndHeight = new Point(100, 100);
        }

        shadowBitmap.setWidth(widthAndHeight.x);
        shadowBitmap.setHeight(widthAndHeight.y);
        options.outWidth = widthAndHeight.x;
        options.outHeight = widthAndHeight.y;
        recentRunsOptions.push(options);
        
        return bitmap;
    }

    public static void provideWidthAndHeightHints(Uri uri, int width, int height) {
        widthAndHeightMap.put(uri.toString(), new Point(width, height));
    }

    public static void provideWidthAndHeightHints(int resourceId, int width, int height) {
        widthAndHeightMap.put("resource:" + getResourceName(resourceId), new Point(width, height));
    }

    public static void provideWidthAndHeightHints(String file, int width, int height) {
        widthAndHeightMap.put("file:" + file, new Point(width, height));
    }

    private static String stringify(BitmapFactory.Options options) {
        List<String> opts = new ArrayList<String>();

        if (options.inJustDecodeBounds) opts.add("inJustDecodeBounds");
        if (options.inSampleSize > 1) opts.add("inSampleSize=" + options.inSampleSize);

        return Join.join(", ", opts);
    }

    public static void reset() {
        widthAndHeightMap.clear();
    }
}
