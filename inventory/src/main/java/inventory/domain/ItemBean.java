/**
 * 
 */
package inventory.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="item")
@SequenceGenerator(name="azd_item", sequenceName="azd_itemSeq")
public class ItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_item")
	private Integer itemId;
	
	@ManyToOne
	private ProductsBean productBean;
	
	@ManyToOne
	private BrandBean brandBean;

	@Column(name="supplierId")
	private String supplierId;
	
	@ManyToOne
	private Order order;
	
	@Column(name="createdAt", nullable = false)
	private Date createDt;
	
	@Column(name="updatedAt")
	private Date updateDt;

	@Column(name="sku", length = 100, nullable = false)
	private String stockKeppingUnit;
	
	@Column(name="mrp", nullable = false)
	private Float maximumRetailPrice = 0F;
	
	@Column(name = "discount", nullable = false)
	private Float discountFromSupplier = 0F;
	
	@Column(name="price", nullable = false)
	private Float productPurchasingPrice = 0F;
	
	@Column(name="quantitiy", nullable = false)
	private Integer quantityRecievedAtInventory =0;
	
	@Column(name="sold", nullable = false)
	private Integer quantitySoldToCustomers = 0;
	
	@Column(name="available", nullable = false)
	private Integer quantityAvailableOnStock = 0;
	
	@Column(name="defective", nullable = false)
	private Integer defectiveItemsOfInventory = 0;

	
	@Column(name="createdBy", length = 10485760)
	private String createBy;
	
	@Column(name="updatedBy", length = 10485760)
	private String updateBy;

	/**
	 * @return the itemId
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the productBean
	 */
	public ProductsBean getProductBean() {
		return productBean;
	}

	/**
	 * @param productBean the productBean to set
	 */
	public void setProductBean(ProductsBean productBean) {
		this.productBean = productBean;
	}

	/**
	 * @return the brandBean
	 */
	public BrandBean getBrandBean() {
		return brandBean;
	}

	/**
	 * @param brandBean the brandBean to set
	 */
	public void setBrandBean(BrandBean brandBean) {
		this.brandBean = brandBean;
	}

	/**
	 * @return the supplierId
	 */
	public String getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
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
	 * @return the stockKeppingUnit
	 */
	public String getStockKeppingUnit() {
		return stockKeppingUnit;
	}

	/**
	 * @param stockKeppingUnit the stockKeppingUnit to set
	 */
	public void setStockKeppingUnit(String stockKeppingUnit) {
		this.stockKeppingUnit = stockKeppingUnit;
	}

	/**
	 * @return the maximumRetailPrice
	 */
	public Float getMaximumRetailPrice() {
		return maximumRetailPrice;
	}

	/**
	 * @param maximumRetailPrice the maximumRetailPrice to set
	 */
	public void setMaximumRetailPrice(Float maximumRetailPrice) {
		this.maximumRetailPrice = maximumRetailPrice;
	}

	/**
	 * @return the discountFromSupplier
	 */
	public Float getDiscountFromSupplier() {
		return discountFromSupplier;
	}

	/**
	 * @param discountFromSupplier the discountFromSupplier to set
	 */
	public void setDiscountFromSupplier(Float discountFromSupplier) {
		this.discountFromSupplier = discountFromSupplier;
	}

	/**
	 * @return the productPurchasingPrice
	 */
	public Float getProductPurchasingPrice() {
		return productPurchasingPrice;
	}

	/**
	 * @param productPurchasingPrice the productPurchasingPrice to set
	 */
	public void setProductPurchasingPrice(Float productPurchasingPrice) {
		this.productPurchasingPrice = productPurchasingPrice;
	}

	/**
	 * @return the quantityRecievedAtInventory
	 */
	public Integer getQuantityRecievedAtInventory() {
		return quantityRecievedAtInventory;
	}

	/**
	 * @param quantityRecievedAtInventory the quantityRecievedAtInventory to set
	 */
	public void setQuantityRecievedAtInventory(Integer quantityRecievedAtInventory) {
		this.quantityRecievedAtInventory = quantityRecievedAtInventory;
	}

	/**
	 * @return the quantitySoldToCustomers
	 */
	public Integer getQuantitySoldToCustomers() {
		return quantitySoldToCustomers;
	}

	/**
	 * @param quantitySoldToCustomers the quantitySoldToCustomers to set
	 */
	public void setQuantitySoldToCustomers(Integer quantitySoldToCustomers) {
		this.quantitySoldToCustomers = quantitySoldToCustomers;
	}

	/**
	 * @return the quantityAvailableOnStock
	 */
	public Integer getQuantityAvailableOnStock() {
		return quantityAvailableOnStock;
	}

	/**
	 * @param quantityAvailableOnStock the quantityAvailableOnStock to set
	 */
	public void setQuantityAvailableOnStock(Integer quantityAvailableOnStock) {
		this.quantityAvailableOnStock = quantityAvailableOnStock;
	}

	/**
	 * @return the defectiveItemsOfInventory
	 */
	public Integer getDefectiveItemsOfInventory() {
		return defectiveItemsOfInventory;
	}

	/**
	 * @param defectiveItemsOfInventory the defectiveItemsOfInventory to set
	 */
	public void setDefectiveItemsOfInventory(Integer defectiveItemsOfInventory) {
		this.defectiveItemsOfInventory = defectiveItemsOfInventory;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the updateBy
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy the updateBy to set
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	
	
}
