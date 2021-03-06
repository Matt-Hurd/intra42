package com.paulvarry.intra42;

import android.content.Context;

public interface BaseItem {

    /**
     * Usual use like primary text on a listView
     *
     * @return The name (title) of the item.
     */
    String getName();

    /**
     * Usual use like subtitle on a list view.
     *
     * @return The sub title.
     */
    String getSub();

    /**
     * Declare here the method to open this elem n a activity.
     *
     * @return A boolean to notice if opening is success.
     */
    boolean openIt(Context context);
}
