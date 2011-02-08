package com.xtremelabs.robolectric.shadows;

import static org.junit.Assert.*;

import android.content.*;
import android.content.SharedPreferences.Editor;
import android.preference.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.tester.android.content.*;

@RunWith(WithTestDefaultsRunner.class)
public class PreferenceManagerTest {
    @Test
    public void shouldProvideDefaultSharedPreferences() throws Exception {
        Map<String, HashMap<String, Object>> content = Robolectric.getShadowApplication().getSharedPreferenceMap();

        TestSharedPreferences testPrefs = new TestSharedPreferences(content, "__default__", Context.MODE_PRIVATE);
        Editor editor = testPrefs.edit();
        editor.putInt("foobar", 13);
        editor.commit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Robolectric.application);

        assertNotNull(prefs);
        assertEquals(13, prefs.getInt("foobar", 0));
    }

}
