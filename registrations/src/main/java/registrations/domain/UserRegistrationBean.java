/**
 * 
 */
package registrations.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author anees-ur-rehman
 * 						"insert into user_registration (security_id, publickey, privatekey, globalid, user_name, user_password, user_role, status, createdby, creationdt, email)");
		
 *
 */
@Entity
@Table(name = "user_registration")
//@SequenceGenerator(name="kys_authSeq", sequenceName="kys_authSeq")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserRegistrationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="globalid", length=10485760)
	private String globalId;
	
	@Column(name="security_id", nullable = false, unique = true, length=10485760	)
	private String securityID;
	
	@Column(name="email", nullable = false, unique = true)
	private String email;

	@Column(name="cellPhone", nullable = false, unique = true)
	private String phoneNumber;
	
	@Column(name="publicKey", length=10485760)
	private String publicKey;
	
	@Column(name="privatekey", length=10485760)
	private String privateKey;
	
	@Column(name="certificate", length=10485760)
	private String certificate;

	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_password")
	private String userPassword;
	
	@Column(name="user_role")
	private String userRole;
	
	@Column(name="status")
	private String status;
	
	@Column(name="createdBy")
	private String createdBy;
	
	@Column(name="creationDt")
	private Date creationDate;
	
	@Column(name="updatedBy")
	private String updatedBy;
	
	@Column(name="updationDt")
	private Date updationDate;
	
	
	
	
}
