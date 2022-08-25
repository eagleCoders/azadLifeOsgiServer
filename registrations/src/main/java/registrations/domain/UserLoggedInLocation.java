/**
 * 
 */
package registrations.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
	@GeneratedValue(generator="azadPaymentuserLoggedInUser", strategy=GenerationType.AUTO)
	@SequenceGenerator(name="azadPaymentuserLoggedInUser", sequenceName="ap_userloggedInSeq")
	private Long id;
	
	@Column(name="globalid", length=10485760)
	private String globalId;

	@Column(name="login_lat")
	private Double latitude;
	
	@Column(name="login_lang")
	private Double longitude;

	private Timestamp longedInTime;
	
	private LocalDate loginDate;
	
	private LocalDate logoutDate;


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
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
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

	/**
	 * @return the loginDate
	 */
	public LocalDate getLoginDate() {
		return loginDate;
	}

	/**
	 * @param loginDate the loginDate to set
	 */
	public void setLoginDate(LocalDate loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * @return the logoutDate
	 */
	public LocalDate getLogoutDate() {
		return logoutDate;
	}

	/**
	 * @param logoutDate the logoutDate to set
	 */
	public void setLogoutDate(LocalDate logoutDate) {
		this.logoutDate = logoutDate;
	}
	
	
}

