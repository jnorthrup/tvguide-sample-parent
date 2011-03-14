package com.acme.gwt.shared;

import java.util.List;

import com.acme.gwt.data.TvViewer;
import com.acme.gwt.server.InjectingLocator;
import com.acme.gwt.shared.defs.Geo;
import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.ProxyFor;

/**
 * Created by IntelliJ IDEA.
 * User: jim
 * Date: 3/10/11
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
@ProxyFor(value = TvViewer.class, locator = InjectingLocator.class)
public interface TvViewerProxy extends EntityProxy {
	Long getId();

	Integer getVersion();

	void setId(Long id);

	void setVersion(Integer version);

	List<TvShowProxy> getFavorites();

	void setFavorites(List<TvShowProxy> favorites);

	String getEmail();

	void setEmail(String email);

	String getDigest();

	void setDigest(String digest);

	String getSalt();

	void setSalt(String salt);

	Geo getGeo();

	void setGeo(Geo geo);

	public EntityProxyId<TvViewerProxy> stableId();

}
