/**
 * 
 */
package registrations.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import registrations.domain.types.UserType;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "user_types")
@NoArgsConstructor
@Data
public class UserTypeChangeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="azadPaymentuserTypeSeq", strategy=GenerationType.AUTO)
	@SequenceGenerator(name="azadPaymentuserTypeSeq", sequenceName="ap_userTypeSeq")
	@Column(name="user_typeid", updatable = false, nullable =  false)
	private Integer userTypeid;
	
	@Column(name="globalId", length=10485760)
	private String globalId;
	
	@Column(name="user_type")
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
//	Levels are 0, 1, 2, 3, 4, 5, 6
	@Column(name="user_level")
	private Integer userTypeLevel;

	public Integer getUserTypeid() {
		return userTypeid;
	}

	public void setUserTypeid(Integer userTypeid) {
		this.userTypeid = userTypeid;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getUserTypeLevel() {
		return userTypeLevel;
	}

	public void setUserTypeLevel(Integer userTypeLevel) {
		this.userTypeLevel = userTypeLevel;
	}
	
	

	
}
