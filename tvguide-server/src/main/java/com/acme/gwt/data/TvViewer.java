package com.acme.gwt.data;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Version;

import com.acme.gwt.data.AuthenticationCallFactory.AuthenticationCall;
import com.acme.gwt.server.AuthenticatedViewerProvider;
import com.acme.gwt.shared.defs.Geo;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Due to details in how this type is injected when code asks for the current user, this cannot
 * be created by calling injector.getInstance(TvViewer.class), but must be constructed the old
 * fashioned way. A 'create profile' method or the like would be the appropriate place to do this.
 * 

 * User: jim
 * Date: 3/10/11
 * Time: 8:50 PM

 */
@Entity
//@NamedQuery(name = TvViewer.SIMPLE_AUTH, query = "select vp from TvViewer vp where vp.email=:email and vp.digest=:digest")
public class TvViewer implements HasVersionAndId {

	static final String SIMPLE_AUTH = "simpleAuth";
	private Long id;
	private Geo geo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	private Integer version;

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	private String email;
	private String digest;
	private String salt;

	private List<TvShow> favorites = new LinkedList<TvShow>();

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
	@OrderColumn(name = "rank")
	public List<TvShow> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<TvShow> favorites) {
		this.favorites = favorites;
	}

	@Column(unique = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Enumerated(EnumType.STRING)
	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	@Inject
	static Provider<EntityManager> emProvider;

	// todo: @Finder (namedQuery = SIMPLE_AUTH)static TvViewer findTvViewerByEmailAndDigest(String email,String digest){}

	//handwritten finder
	public static TvViewer findTvViewerByEmailAndDigest(String email,
			String digest) {
		TvViewer singleResult = null;
		try {
			singleResult = emProvider
					.get()
					.createQuery(
							"select vp from TvViewer vp where vp.email=:email and vp.digest=:digest",
							TvViewer.class).setParameter("email", email)
					.setParameter("digest", digest).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace(); //todo: verify for a fit
		} finally {
		}
		return singleResult;
	}

	public static class AuthCallable implements AuthenticationCall {
		@Assisted("email")
		String email;
		@Assisted("digest")
		String digest;
		@Inject
		Provider<AuthenticatedViewerProvider> currentUserProvider;
		@AssistedInject
		public AuthCallable() {
			// TODO Auto-generated constructor stub
		}
		@AssistedInject
		public AuthCallable(@Assisted("email")
		String email, @Assisted("digest")
		String digest) {
			this.email = email;
			this.digest = digest;
		}
		@Override
		public TvViewer call() throws Exception {
			TvViewer currentUser;
			if (email == null) {//deauth call
				currentUser = null;
			} else {
				currentUser = findTvViewerByEmailAndDigest(email, digest);
			}
			//save the current user
			currentUserProvider.get().setCurrentViewer(currentUser);

			// if an email was provided, but no user was found, blow an error
			if (email != null && currentUser == null) {
				//lookup failed, throw ex
				//TODO consider just returning null and letting client interpret that as failure
				throw new RuntimeException("Failed login attempt.");
			}
			return currentUser;
		}

	}
}