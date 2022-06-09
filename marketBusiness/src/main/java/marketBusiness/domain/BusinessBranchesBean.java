/**
 * 
 */
package marketBusiness.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "azd_bussiness_brnch")
@NoArgsConstructor
@Data
@SequenceGenerator(name="kys_bizBranch", sequenceName="kys_bizMasterSeq")
public class BusinessBranchesBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="branch_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kys_bizBranch")
	private Integer id;

	@Column(name="branch_position")
	private Integer branchPosition;

	@Column(name="branch_name")
	private String branchName;
	
	@Column(name="brnch_address")
	private String branchAdress;

	@Column(name="isprimary")
	private Boolean primaryBranch = false;
	
	@Column(name="biz_lat")
	private Long businessLatitude;
	
	@Column(name="biz_lang")
	private Long businessLongitude;

	
//	private List<BusinessBranchsInventory> businessInventories;
	
//	@Column(name="master_code")
//	private BusinessMasterBean businessMasterBean;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the branchPosition
	 */
	public Integer getBranchPosition() {
		return branchPosition;
	}

	/**
	 * @param branchPosition the branchPosition to set
	 */
	public void setBranchPosition(Integer branchPosition) {
		this.branchPosition = branchPosition;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the branchAdress
	 */
	public String getBranchAdress() {
		return branchAdress;
	}

	/**
	 * @param branchAdress the branchAdress to set
	 */
	public void setBranchAdress(String branchAdress) {
		this.branchAdress = branchAdress;
	}

	/**
	 * @return the primaryBranch
	 */
	public Boolean getPrimaryBranch() {
		return primaryBranch;
	}

	/**
	 * @param primaryBranch the primaryBranch to set
	 */
	public void setPrimaryBranch(Boolean primaryBranch) {
		this.primaryBranch = primaryBranch;
	}
	
	/**
	 * @return the businessLatitude
	 */
	public Long getBusinessLatitude() {
		return businessLatitude;
	}

	/**
	 * @param businessLatitude the businessLatitude to set
	 */
	public void setBusinessLatitude(Long businessLatitude) {
		this.businessLatitude = businessLatitude;
	}

	/**
	 * @return the businessLongitude
	 */
	public Long getBusinessLongitude() {
		return businessLongitude;
	}

	/**
	 * @param businessLongitude the businessLongitude to set
	 */
	public void setBusinessLongitude(Long businessLongitude) {
		this.businessLongitude = businessLongitude;
	}


//	/**
//	 * @return the businessMasterBean
//	 */
//	@Column(name="master_code")
//	public BusinessMasterBean getBusinessMasterBean() {
//		return businessMasterBean;
//	}
//
//	/**
//	 * @param businessMasterBean the businessMasterBean to set
//	 */
//	public void setBusinessMasterBean(BusinessMasterBean businessMasterBean) {
//		this.businessMasterBean = businessMasterBean;
//	}
//	
	
}

