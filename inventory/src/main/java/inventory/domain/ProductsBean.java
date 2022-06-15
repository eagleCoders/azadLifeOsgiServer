/**
 * 
 */
package inventory.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="products")
@SequenceGenerator(name="azd_products", sequenceName="azd_productsMasterSeq")

public class ProductsBean implements Serializable{

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_products")
	private Integer productId;
	
	@Column(name="title", nullable = false)
	private String productTitle;
	
	@Column(name="summary", nullable = false)
	private String summary;

	@Column(name="type", nullable = false)
	private Integer type = 0;
	
	@Column(name="createdAt", nullable = false)
	private Date createDt;
	
	@Column(name="updatedAt")
	private Date updateDt;
	
	@Column(name="content", length = 10485760)
	private String contnet;
	
	@OneToMany(targetEntity = ProductMetaBean.class)
	@JoinColumn(name = "productId")
	private List<ProductMetaBean> productMetaList;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "product_category", joinColumns = { @JoinColumn(name="productId", referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name="categoryId", referencedColumnName = "id")})
	private List<CategoryBean> productCategoryList;

	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * @return the productTitle
	 */
	public String getProductTitle() {
		return productTitle;
	}

	/**
	 * @param productTitle the productTitle to set
	 */
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the createDt
	 */
	public Date getCreateDt() {
		return createDt;
	}

	/**
	 * @param createDt the createDt to set
	 */
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	/**
	 * @return the updateDt
	 */
	public Date getUpdateDt() {
		return updateDt;
	}

	/**
	 * @param updateDt the updateDt to set
	 */
	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	/**
	 * @return the contnet
	 */
	public String getContnet() {
		return contnet;
	}

	/**
	 * @param contnet the contnet to set
	 */
	public void setContnet(String contnet) {
		this.contnet = contnet;
	}
	
	
	
}
