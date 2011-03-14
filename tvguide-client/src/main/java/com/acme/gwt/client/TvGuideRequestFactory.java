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

import java.util.List;

import com.acme.gwt.data.TvViewer;
import com.acme.gwt.server.InjectingServiceLocator;
import com.acme.gwt.shared.TvGuideRequest;
import com.acme.gwt.shared.TvShowProxy;
import com.acme.gwt.shared.TvViewerProxy;
import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.requestfactory.shared.Service;

/**
 * RequestFactory stub for the TvGuide server calls
 *
 * @author colin
 */
public interface TvGuideRequestFactory extends RequestFactory {
	AuthorizationRequest makeLoginRequest();

	TvGuideRequest makeGuideRequest();

	TvSetupRequest makeSetupRequest();

	TvGuideRequest reqGuide();

	@Service(value = TvViewer.class)
	interface AuthorizationRequest extends RequestContext {
		//replace with controller
		Request<TvViewerProxy> authenticate(String email, String digest);

		@Service(value = TvViewer.Favorites.class, locator = InjectingServiceLocator.class)
		interface Favorites {
			InstanceRequest<TvViewerProxy, List<TvShowProxy>> call();
		}
	}

	/**
	 * This RequestContext will be used to inform the server about how a particular locale is set up.
	 * This will probably not be used by most users, and it is possible that we want to deny access to
	 * this for certain types of users (i.e. users who are not logged in, or are not admins).
	 *
	 * @author colin
	 * @todo No methods yet, I am assuming we will get to this
	 */
	@Service(TvSetupRequest.SetupReqImpl.class)
	interface TvSetupRequest extends RequestContext {

		public class SetupReqImpl {
		}
	}
}
