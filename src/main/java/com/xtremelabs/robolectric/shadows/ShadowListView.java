package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;

import android.view.*;
import android.widget.*;

import java.util.*;

import com.xtremelabs.robolectric.internal.*;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(ListView.class)
public class ShadowListView extends ShadowAdapterView {
    @RealObject private ListView realListView;

    private boolean itemsCanFocus;
    private List<View> headerViews = null;
    private List<View> footerViews = null;

    @Implementation
    public void setItemsCanFocus(boolean itemsCanFocus) {
        this.itemsCanFocus = itemsCanFocus;
    }

    @Implementation
    @Override
    public boolean performItemClick(View view, int position, long id) {
        AdapterView.OnItemClickListener onItemClickListener = getOnItemClickListener();
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(realListView, view, position, id);
            return true;
        }
        return false;
    }

    @Implementation
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Implementation
    public void addHeaderView(View headerView) {
    	if (footerViews == null || headerViews == null)
    		ensureAdapterNotSet("header");
    	headerViews = new ArrayList<View>();
    	footerViews = new ArrayList<View>();
        headerViews.add(headerView);
    }

    @Implementation
    public void addFooterView(View footerView, Object data, boolean isSelectable) {
    	if (footerViews == null || headerViews == null)
    		ensureAdapterNotSet("footer");
    	headerViews = new ArrayList<View>();
    	footerViews = new ArrayList<View>();
        footerViews.add(footerView);
    }
    
    @Implementation public boolean removeHeaderView(View headerView) {
    	return headerViews.remove(headerView);
    }
    
    @Implementation public boolean removeFooterView(View footerView) {
    	return footerViews.remove(footerView);
    }

    @Implementation
    public void addFooterView(View footerView) {
        addFooterView(footerView, null, false);
    }


    public boolean performItemClick(int position) {
        return realListView.performItemClick(realListView.getChildAt(position), position, realListView.getItemIdAtPosition(position));
    }

    public int findIndexOfItemContainingText(String targetText) {
        for (int i = 0; i < realListView.getChildCount(); i++) {
            View childView = realListView.getChildAt(i);
            String innerText = shadowOf(childView).innerText();
            if (innerText.contains(targetText)) {
                return i;
            }
        }
        return -1;
    }

    public View findItemContainingText(String targetText) {
        int itemIndex = findIndexOfItemContainingText(targetText);
        if (itemIndex == -1) {
            return null;
        }
        return realListView.getChildAt(itemIndex);
    }

    public void clickFirstItemContainingText(String targetText) {
        int itemIndex = findIndexOfItemContainingText(targetText);
        if (itemIndex == -1) {
            throw new IllegalArgumentException("No item found containing text \"" + targetText + "\"");
        }
        performItemClick(itemIndex);
    }

    private void ensureAdapterNotSet(String view) {
        if (adapter != null) {
            throw new IllegalStateException("Cannot add " + view + " view to list -- setAdapter has already been called");
        }
    }

    public boolean isItemsCanFocus() {
        return itemsCanFocus;
    }

    public List<View> getHeaderViews() {
        return headerViews;
    }

    public void setHeaderViews(List<View> headerViews) {
        this.headerViews = headerViews;
    }

    public List<View> getFooterViews() {
        return footerViews;
    }

    public void setFooterViews(List<View> footerViews) {
        this.footerViews = footerViews;
    }
}
