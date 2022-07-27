/**
 * 
 */
package registrations.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="user_loggedIn_loc")
public class UserLoggedInLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(name="globalid", length=10485760)
	private String globalId;

	@Column(name="login_lat")
	private Long latitude;
	
	@Column(name="login_lang")
	private Long longitude;

	private Timestamp longedInTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the globalId
	 */
	public String getGlobalId() {
		return globalId;
	}

	/**
	 * @param globalId the globalId to set
	 */
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	/**
	 * @return the latitude
	 */
	public Long getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Long getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the longedInTime
	 */
	public Timestamp getLongedInTime() {
		return longedInTime;
	}

	/**
	 * @param longedInTime the longedInTime to set
	 */
	public void setLongedInTime(Timestamp longedInTime) {
		this.longedInTime = longedInTime;
	}
	
	
}

