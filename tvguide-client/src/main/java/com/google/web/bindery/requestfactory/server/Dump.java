package com.google.web.bindery.requestfactory.server;

import javax.persistence.Basic;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * User: jim
 * Date: 5/9/11
 * Time: 3:03 PM
 */
class TvGuidePersistentUnit {
	@XStreamAsAttribute
	String name;
	@XStreamAsAttribute
	@XStreamAlias("transaction-type")
	public String transaction$2dtype;
	public Class provider;
	@XStreamImplicit(itemFieldName = "class")
	public List<Class> classes;
	Properties properties;
}

@XStreamAlias("persistence")
class TvGuideModel {
	@XStreamAlias("persistence-unit")
	public TvGuidePersistentUnit persistence$2dunit;
}

@SuppressWarnings({"JpaQlInspection"})
public class Dump {
	public static final EntityManager ENTITY_MANAGER = Persistence
			.createEntityManagerFactory(null).createEntityManager();
	final EntityManager entityManager = ENTITY_MANAGER;
	private List<Class> classes;

	private XStream xstream;

	public Dump() throws IOException {
		TvGuideModel twModel = init();
		go(twModel);
	}

	private void go(TvGuideModel twModel) throws IOException {
		mapDirectResults(twModel, getXstream());
		mapAggregates(twModel, getXstream());
		mapOneAry(twModel, getXstream());

		try {
			getXstream().toXML(collectDirectResults(classes), System.err);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private TvGuideModel init() {
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream("META-INF/persistence.xml");
		setXstream(new XStream(new DomDriver()));

		getXstream()
				.processAnnotations(
						new Class<?>[]{TvGuideModel.class,
								TvGuidePersistentUnit.class});
		getXstream().alias("persistence", TvGuideModel.class);

		TvGuideModel twModel = (TvGuideModel) getXstream().fromXML(
				resourceAsStream);
		getXstream().toXML(twModel, System.out);

		classes = twModel.persistence$2dunit.classes;
		getXstream().processAnnotations(
				(Class[]) classes.toArray(new Class[classes.size()]));
		return twModel;
	}

	private void mapDirectResults(TvGuideModel o, XStream xstream) {
		for (final Class clazz : classes) {

			EntityManager entityManager = ENTITY_MANAGER;

			//create generic fetch
			String s = "select o from " + clazz.getCanonicalName() + " o ";

			//create mapping to initial fetch (limitted usefulness)
			for (Object entity : entityManager.createQuery(s, clazz)
					.getResultList()) {
				Class<?> aClass = entity.getClass();
				if (!clazz.equals(aClass)) {
					System.err.println("mapping: " + clazz + " -> " + aClass);
					xstream.addDefaultImplementation(clazz, aClass);
					break;
				}
			}
		}
	}

	Collection collectDirectResults(List<Class> entityClasses) {

		Collection arr = new ArrayList();
		for (Class entityClass : entityClasses) {

			try {
				arr.addAll(entityManager.createQuery(
						"from " + entityClass.getCanonicalName())
						.getResultList());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return arr;
	}

	void mapAggregates(TvGuideModel o, XStream xstream) {
		final Set<Class<? extends Annotation>> aggregateAnnotations = new HashSet<Class<? extends Annotation>>(
				Arrays.asList(OneToMany.class, ManyToMany.class));

		for (final Class<?> clazz : classes) {

			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().startsWith("get")) {
					Set<Class<? extends Annotation>> annotationsFound;
					annotationsFound = new HashSet<Class<? extends Annotation>>();
					for (Annotation annotation : method.getAnnotations()) {
						annotationsFound.add(annotation.annotationType());
					}

					annotationsFound.retainAll(aggregateAnnotations);
					if (!annotationsFound.isEmpty()) {

						Type genericReturnType = method.getGenericReturnType();
						if (genericReturnType instanceof ParameterizedType) {
							ParameterizedType returnType = (ParameterizedType) genericReturnType;
							Class actualClass = (Class) returnType
									.getActualTypeArguments()[0];
							String s1 = method.toGenericString();
							final String s = "from " + clazz.getCanonicalName()
									+ " x where x." + convertGetterName(method)
									+ " is not empty ";
							System.err.println("running " + s);
							try {
								List<?> resultList = entityManager.createQuery(
										s, clazz).getResultList();
								for (Object ent : resultList) {
									Object invoke = method.invoke(ent);
									if (null != invoke) {
										if (invoke instanceof Collection) {
											Collection iterable = (Collection) invoke;
											Iterator iterator = iterable
													.iterator();
											if (iterator.hasNext()) {
												Object next = iterator.next();
												if (null != next) {
													Class<?> synthClass = next
															.getClass();
													if (!clazz
															.equals(synthClass)) {
														mapClass(xstream,
																actualClass,
																synthClass);
														System.err
																.println("mapped "
																		+ actualClass
																		+ " -> "
																		+ synthClass);
													}
												}
											}
										}
										break;
									}
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private void mapClass(XStream xstream, Class actualClass,
			Class<?> synthClass) {
		xstream.aliasType(actualClass.getCanonicalName(), synthClass);
		xstream.addDefaultImplementation(actualClass, synthClass);
	}

	private static String convertGetterName(Method method) {
		return Character.toLowerCase((Character) method.getName().charAt(
				"get".length()))
				+ method.getName().substring("get".length() + 1);
	}

	List<Class> mapOneAry(TvGuideModel model, XStream xstream) {
		final Set<Class<? extends Annotation>> aggregateAnnotations = new HashSet<Class<? extends Annotation>>(
				Arrays.asList(OneToOne.class, ManyToOne.class, Basic.class));
		//crawl all aggregate methods and assign
		List<Class> entityClasses = model.persistence$2dunit.classes;
		for (final Class<?> clazz : entityClasses) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().startsWith("get")) {
					Set<Class<? extends Annotation>> annotationsFound;
					annotationsFound = new HashSet<Class<? extends Annotation>>();
					for (Annotation annotation : method.getAnnotations()) {
						annotationsFound.add(annotation.annotationType());
					}

					annotationsFound.retainAll(aggregateAnnotations);
					if (!annotationsFound.isEmpty()) {

						String property = convertGetterName(method);
						final String s = "from " + clazz.getCanonicalName()
								+ " x where x." + property + " is not null";
						System.err.println("running " + s);
						try {
							for (Object ent : entityManager.createQuery(s,
									clazz).getResultList()) {
								Object invoke = method.invoke(ent);
								if (null != invoke) {
									Class<?> synthClass = invoke.getClass();
									Class<?> actualClass = method
											.getReturnType();
									if (actualClass != synthClass) {
										mapClass(xstream, actualClass,
												synthClass);

										System.err.println("mapped "
												+ actualClass + " -> "
												+ synthClass);
										break;
									}
								}
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return entityClasses;
	}

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}
}
