/**
 * 
 */
package registrations.domain;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserRegistrationRequestBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
