package com.acme.gwt.data;

import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**

 * User: jim
 * Date: 3/10/11
 * Time: 7:27 PM

 */
@Embeddable
public class TvAirTime {

	private Date startDate;

	private Integer duration;

	@Temporal(TemporalType.TIME)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
