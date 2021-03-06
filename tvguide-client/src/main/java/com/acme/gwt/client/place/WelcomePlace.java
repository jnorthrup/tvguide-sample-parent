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
package com.acme.gwt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * 'Home' place, brings the user to the default page in the app. No prefix, no token.
 * 
 * @author colin
 *
 */
public class WelcomePlace extends Place {

	@Prefix("")
	public static class Tokenizer implements PlaceTokenizer<WelcomePlace> {
		@Override
		public WelcomePlace getPlace(String token) {
			return new WelcomePlace();
		}
		@Override
		public String getToken(WelcomePlace place) {
			return "";
		}
	}
}
