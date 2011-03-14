/**
 *  Copyright 2011 Colin Alworth
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.acme.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author colin
 */
public class TvGuide implements EntryPoint {

  public void onModuleLoad() {

    //code from jnorthrup's fork, will be moved to a presenter soon enough
    EventBus eventBus = GWT.create(SimpleEventBus.class);
    final TvGuideRequestFactory rf = GWT.create(TvGuideRequestFactory.class);
    rf.initialize(eventBus);

    new DialogBox() {
      {
        final TextBox email = new TextBox() {
          {
            setText("you@example.com");
          }
        };
        final PasswordTextBox passwordTextBox = new PasswordTextBox();
        setText("please log in");
        setWidget(new VerticalPanel() {
          {
            add(new HorizontalPanel() {
              {
                add(new Label("email"));
                add(email);
              }
            });
            add(new HorizontalPanel() {
              {
                add(new Label("Password"));
                add(passwordTextBox);
              }
            });
            add(new Button("OK!!") {
              {
                addClickHandler(new ClickHandler() {
                  @Override
                  public void onClick(ClickEvent event) {

                    GWT.runAsync(new PrematureLoaderOptimization
                        (passwordTextBox, rf, email, new RunAsyncCallback() {
                          @Override
                          public void onFailure(Throwable reason) {
                          }

                          @Override
                          public void onSuccess() {
                            hide();
                            removeFromParent();
                          }
                        }));
                  }
                });
              }
            });
          }
        });
        center();
        show();
      }
    };
  }

}
