package com.acme.gwt.data;

import javax.persistence.EntityManager;

import com.acme.gwt.server.InjectingLocator;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class StaticLocator<T extends HasIdAndVersion> extends InjectingLocator<T> {
	@Inject
	EntityManager em;
	@Inject
	Class<T> clazz;
	@Transactional
	public void persist(T t) {
		em.persist(t);
	}

	@Transactional
	T merge(T merge) {
		T t = em.merge(merge);
		em.flush();
		return t;
	}

	@Transactional
	void rm(T t) {
		Long id = t.getId();
		em.detach(t);
		T t1 = em.find(clazz, id);
		em.remove(t1);
		em.flush();
	}
}
