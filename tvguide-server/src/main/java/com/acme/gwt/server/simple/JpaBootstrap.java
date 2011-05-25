package com.acme.gwt.server.simple;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

/**

 * User: jim
 * Date: 3/11/11
 * Time: 1:28 AM

 */
public class JpaBootstrap {
	@Inject
	PersistService service;

	public JpaBootstrap() {
		service.start();
	}
}
