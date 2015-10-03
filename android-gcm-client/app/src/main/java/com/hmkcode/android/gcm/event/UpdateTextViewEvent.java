package com.hmkcode.android.gcm.event;

public class UpdateTextViewEvent implements ApplicationEvent {

	private String text;

	public UpdateTextViewEvent(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
