package com.xtremelabs.robolectric.shadows;

import android.database.*;

import com.xtremelabs.robolectric.internal.*;

@Implements(AbstractCursor.class)
public class ShadowAbstractCursor {
    @RealObject private AbstractCursor realAbstractCursor;

    private int currentRowNumber = -1;

    @Implementation
    public boolean moveToFirst() {
        currentRowNumber = 0;
        return realAbstractCursor.getCount() > 0;
    }

    @Implementation
    public int getPosition() {
        return currentRowNumber;
    }

    @Implementation
    public boolean moveToNext() {
        if (currentRowNumber >= realAbstractCursor.getCount() - 1) {
            return false;
        }
        currentRowNumber++;
        return true;
    }
}
