package com.acme.gwt.data;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**

 * User: jim
 * Date: 3/10/11
 * Time: 7:40 PM

 */
public @Entity
class TvScheduledEpisode implements HasVersionAndId {

	private Long id;

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

	private TvEpisode tvEpisode;
	private TvChannel tvChannel;
	private TvAirTime block;

	@ManyToOne(cascade = CascadeType.ALL)
	public TvEpisode getTvEpisode() {
		return tvEpisode;
	}

	public void setTvEpisode(TvEpisode tvEpisode) {
		this.tvEpisode = tvEpisode;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public TvChannel getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(TvChannel tvChannel) {
		this.tvChannel = tvChannel;
	}

	@Embedded
	public TvAirTime getBlock() {
		return block;
	}

	public void setBlock(TvAirTime block) {
		this.block = block;
	}
}
