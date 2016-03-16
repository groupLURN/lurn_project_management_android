package com.android.lurn.projectmanagement.Models.Events;

/**
 * Created by Emmett on 16/03/2016.
 */
public class PostSuccessEvent {
    private Object result;

    public PostSuccessEvent(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }
}
