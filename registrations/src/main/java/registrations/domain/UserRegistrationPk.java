/**
 * 
 */
package registrations.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author anees-ur-rehman
 *
 */
@Data
@Embeddable
@EqualsAndHashCode
public class UserRegistrationPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Id
	@Column(name="globalid", length=10485760)
	private String globalId;
	
//	@Id
	@Column(name="security_id", nullable = false, unique = true, length=10485760	)
	private String securityID;

//	@Id
	@Column(name="cellPhone", nullable = false, unique = true,length=10485760)
	private String phoneNumber;

	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public String getSecurityID() {
		return securityID;
	}

	public void setSecurityID(String securityID) {
		this.securityID = securityID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
