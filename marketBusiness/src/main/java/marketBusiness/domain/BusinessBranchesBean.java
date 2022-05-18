/**
 * 
 */
package marketBusiness.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "azd_bussiness_brnch")
public class BusinessBranchesBean implements Serializable{
	@Id
	private Long id;
}
