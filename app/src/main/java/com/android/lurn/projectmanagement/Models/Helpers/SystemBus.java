package com.android.lurn.projectmanagement.Models.Helpers;

import com.squareup.otto.Bus;

/**
 * Created by Emmett on 16/03/2016.
 */
public class SystemBus {

    private static final Bus BUS = new Bus();
    public static Bus instance() {
        return BUS;
    }
}
