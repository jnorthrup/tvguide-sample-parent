package com.acme.gwt.client;

import com.acme.gwt.shared.TvViewerProxy;
import com.acme.gwt.shared.util.Md5;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: jim
 * Date: 3/13/11
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
class PrematureLoaderOptimization implements RunAsyncCallback {
	EventBus eventBus = GWT.create(SimpleEventBus.class);
	final TvGuideRequestFactory rf = GWT.create(TvGuideRequestFactory.class);
	{
		rf.initialize(eventBus);
	}

	private final PasswordTextBox passwordTextBox;
	private final TextBox email;
	private final RunAsyncCallback successCallback;

	public PrematureLoaderOptimization(PasswordTextBox passwordTextBox,
			TextBox email, RunAsyncCallback runAsyncCallback) {
		this.passwordTextBox = passwordTextBox;
		this.email = email;
		this.successCallback = runAsyncCallback;
	}

	@Override
	public void onFailure(Throwable reason) {
		//todo: review for a purpose
	}

	@Override
	public void onSuccess() {
		String text = passwordTextBox.getText();
		String digest = Md5.md5Hex(text);
		Request<TvViewerProxy> authenticate = rf.makeLoginRequest()
				.authenticate(email.getText(), digest);
		authenticate.with("geo", "name", "favoriteShows.name",
				"favoriteShows.description").to(new GateKeeper()).fire(
				new Receiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						GWT.runAsync(successCallback);
					}
				});
	}
}
