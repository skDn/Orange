package com.hmkcode.android.gcm.event;

public interface EventBusListener<T> {

	void onEvent(T event);
}
