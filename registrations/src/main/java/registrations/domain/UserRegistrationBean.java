/**
 * 
 */
package registrations.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author anees-ur-rehman
 * 						"insert into user_registration (security_id, publickey, privatekey, globalid, user_name, user_password, user_role, status, createdby, creationdt, email)");
		
 *
 */
@XmlRootElement
@XmlType
@Entity
@Table(name = "user_registration")
//@SequenceGenerator(name="kys_authSeq", sequenceName="kys_authSeq")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
//@Getter
//@Setter
public class UserRegistrationBean implements Serializable{

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UserRegistrationPk userRegistrationPk;
	
	
	@Column(name="email", nullable = false, unique = true)
	private String email;

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

	public UserRegistrationPk getUserRegistrationPk() {
		return userRegistrationPk;
	}

	public void setUserRegistrationPk(UserRegistrationPk userRegistrationPk) {
		this.userRegistrationPk = userRegistrationPk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdationDate() {
		return updationDate;
	}

	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}
	
	
	
	
	
}
