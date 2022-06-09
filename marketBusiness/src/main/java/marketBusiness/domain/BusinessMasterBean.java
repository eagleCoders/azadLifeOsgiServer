/**
 * 
 */
package marketBusiness.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import marketBusiness.domain.types.BusinessType;
import marketBusiness.domain.types.RegistrationType;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "azd_bussiness_master")
@NoArgsConstructor
@Data
@SequenceGenerator(name="kys_bizMaster", sequenceName="kys_bizMasterSeq")

public class BusinessMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kys_bizMaster")
	private Integer id;
	
	@Column(name="biz_name")
	private String businessName;
	
	@Column(name="biz_type")
	@Enumerated(EnumType.STRING)
	private BusinessType businessType;
	
	@Column(name="biz_banner", length=10485760)
	private String businessBanner;
	
	@Column(name="biz_reciept", length=10485760)
	private String businessRecieptImage;
	
	
	@Column(name="biz_address")
	private String businessCoreAddress;
	
	@Column(name="biz_regType")
	@Enumerated(EnumType.STRING)
	private RegistrationType registrationType;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = BusinessBranchesBean.class)
	@JoinColumn( name = "master_code")
	private List<BusinessBranchesBean> businessBranches;

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
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the businessType
	 */
	public BusinessType getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the businessBanner
	 */
	public String getBusinessBanner() {
		return businessBanner;
	}

	/**
	 * @param businessBanner the businessBanner to set
	 */
	public void setBusinessBanner(String businessBanner) {
		this.businessBanner = businessBanner;
	}

	/**
	 * @return the businessRecieptImage
	 */
	public String getBusinessRecieptImage() {
		return businessRecieptImage;
	}

	/**
	 * @param businessRecieptImage the businessRecieptImage to set
	 */
	public void setBusinessRecieptImage(String businessRecieptImage) {
		this.businessRecieptImage = businessRecieptImage;
	}

	/**
	 * @return the businessCoreAddress
	 */
	public String getBusinessCoreAddress() {
		return businessCoreAddress;
	}

	/**
	 * @param businessCoreAddress the businessCoreAddress to set
	 */
	public void setBusinessCoreAddress(String businessCoreAddress) {
		this.businessCoreAddress = businessCoreAddress;
	}

	/**
	 * @return the businessBranches
	 */
	public List<BusinessBranchesBean> getBusinessBranches() {
		return businessBranches;
	}

	/**
	 * @param businessBranches the businessBranches to set
	 */
	public void setBusinessBranches(List<BusinessBranchesBean> businessBranches) {
		this.businessBranches = businessBranches;
	}

	/**
	 * @return the registrationType
	 */
	public RegistrationType getRegistrationType() {
		return registrationType;
	}

	/**
	 * @param registrationType the registrationType to set
	 */
	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}
	
}
