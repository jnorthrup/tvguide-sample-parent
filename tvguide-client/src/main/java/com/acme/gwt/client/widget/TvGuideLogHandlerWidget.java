package com.acme.gwt.client.widget;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * Very simple Widget that will self attach itself to the gwt-logging root logger.
 * 
 * @author HughesA
 */
@Singleton
public class TvGuideLogHandlerWidget extends Composite implements HasWidgets {

	private static TvGuideLogHandlerWidgetUiBinder uiBinder = GWT.create(TvGuideLogHandlerWidgetUiBinder.class);

	interface TvGuideLogHandlerWidgetUiBinder extends
			UiBinder<Widget, TvGuideLogHandlerWidget> {
	}
	
	@UiField
	VerticalPanel verticalPanel;
	
	public TvGuideLogHandlerWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		//attach this logger to gwt-logger as a log handler.
		Logger.getLogger("").addHandler(new HasWidgetsLogHandler(this));
	}

	public TvGuideLogHandlerWidget(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void add(Widget w) {
		//maximum 5 logs.
		while (verticalPanel.getWidgetCount() > 4) {
			verticalPanel.remove(4);
		}
		verticalPanel.insert(w, 0);
	}

	@Override
	public void clear() {
		verticalPanel.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return verticalPanel.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return verticalPanel.remove(w);
	}

}
