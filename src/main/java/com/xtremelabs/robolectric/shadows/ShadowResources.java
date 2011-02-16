package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;

import java.io.*;
import java.util.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.internal.*;
import com.xtremelabs.robolectric.res.*;

/**
 * Shadow of {@code Resources} that simulates the loading of resources
 *
 * @see com.xtremelabs.robolectric.RobolectricTestRunner#RobolectricTestRunner(Class, String, String)
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(Resources.class)
public class ShadowResources {
    static Resources bind(Resources resources, ResourceLoader resourceLoader) {
        ShadowResources shadowResources = shadowOf(resources);
        if (shadowResources.resourceLoader != null) throw new RuntimeException("ResourceLoader already set!");
        shadowResources.resourceLoader = resourceLoader;
        return resources;
    }

    @RealObject Resources realResources;
    private ResourceLoader resourceLoader;

    @Implementation
    public int getColor(int id) throws Resources.NotFoundException {
        return resourceLoader.getColorValue(id);
    }

    @Implementation
    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        return configuration;
    }

    @Implementation
    public String getString(int id) throws Resources.NotFoundException {
        return resourceLoader.getStringValue(id);
    }
    
    @Implementation
    public String getQuantityString(int id, int quantity, Object ... args) {
    	String s = id + " " + (quantity == 1? "singular": "plural");
    	if (args != null) {
    		for (Object o: args) {
    			s += " " + o.toString();
    		}
    	}
    	return s;
    }

    @Implementation
    public String getString(int id, Object... formatArgs) throws Resources.NotFoundException {
        String raw = getString(id);
        return String.format(Locale.ENGLISH, raw, formatArgs);
    }

    @Implementation
    public InputStream openRawResource(int id) throws Resources.NotFoundException {
        return resourceLoader.getRawValue(id);
    }

    @Implementation
    public String[] getStringArray(int id) throws Resources.NotFoundException {
        String[] arrayValue = resourceLoader.getStringArrayValue(id);
        if (arrayValue == null) {
            throw new Resources.NotFoundException();
        }
        return arrayValue;
    }

    @Implementation
    public CharSequence[] getTextArray(int id) throws Resources.NotFoundException {
        return getStringArray(id);
    }

    @Implementation
    public CharSequence getText(int id) throws Resources.NotFoundException {
        return getString(id);
    }

    @Implementation
    public DisplayMetrics getDisplayMetrics() {
        return new DisplayMetrics();
    }

    @Implementation
    public Drawable getDrawable(int drawableResourceId) throws Resources.NotFoundException {
        return new BitmapDrawable(BitmapFactory.decodeResource(realResources, drawableResourceId));
    }

    @Implementation
    public float getDimension(int id) throws Resources.NotFoundException {
        // todo: get this value from the xml resources and scale it by display metrics [xw 20101011]
        if (resourceLoader.dimensions.containsKey(id)) {
            return resourceLoader.dimensions.get(id);
        }
        return id - 0x7f000000;
    }

    @Implementation
    public int getDimensionPixelSize(int id) throws Resources.NotFoundException {
        // The int value returned from here is probably going to be handed to TextView.setTextSize(),
        // which takes a float. Avoid int-to-float conversion errors by returning a value generated from this
        // resource ID but which isn't too big (resource values in R.java are all greater than 0x7f000000).

        return (int) getDimension(id);
    }

    @Implementation
    public int getDimensionPixelOffset(int id) throws Resources.NotFoundException {
        return (int) getDimension(id);
    }

    @Implementation
    public AssetManager getAssets() {
        return ShadowAssetManager.bind(Robolectric.newInstanceOf(AssetManager.class), resourceLoader);
    }

    @Implementation
    public final android.content.res.Resources.Theme newTheme() {
        return newInstanceOf(Resources.Theme.class);
    }

    /**
     * Non-Android accessor that sets the value to be returned by {@link #getDimension(int)}
     *
     * @param id    ID to set the dimension for
     * @param value value to be returned
     */
    public void setDimension(int id, int value) {
        resourceLoader.dimensions.put(id, value);
    }

    @Implements(Resources.Theme.class)
    public static class ShadowTheme {
        @Implementation
        public TypedArray obtainStyledAttributes(int[] attrs) {
            return obtainStyledAttributes(0, attrs);
        }

        @Implementation
        public TypedArray obtainStyledAttributes(int resid, int[] attrs) throws android.content.res.Resources.NotFoundException {
            return obtainStyledAttributes(null, attrs, 0, 0);
        }

        @Implementation
        public TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            return newInstanceOf(TypedArray.class);
        }
    }
}
