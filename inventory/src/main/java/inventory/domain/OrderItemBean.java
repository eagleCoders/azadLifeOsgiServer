/**
 * 
 */
package inventory.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="order_item")
@SequenceGenerator(name="azd_orderItem", sequenceName="azd_itemSeq")
public class OrderItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_orderItem")
	private Integer orderItemId;
	
	@ManyToOne
	@JoinColumn(name="productId", referencedColumnName = "id")
	private ProductsBean productBean;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="itemId", referencedColumnName = "id")
	private ItemBean itemId;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="orderId", referencedColumnName = "id")
	private Order orderId;
	
	@Column(name="sku", length = 100, nullable = false)
	private String stockKeppingUnitAtPurchasing;
	
	@Column(name="price")
	private Float productPrice;

	@Column(name="discount")
	private Float productDiscount;
	
	@Column(name="quantity")
	private Integer productQuantity;
	
	@Column(name="createdAt", nullable = true)
	private Date createDt ;
	
	@Column(name="updatedAt")
	private Date updateDt;
	
	@Column(name="content", length =10485760 )
	private String content;

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public ProductsBean getProductBean() {
		return productBean;
	}

	public void setProductBean(ProductsBean productBean) {
		this.productBean = productBean;
	}

	public ItemBean getItemId() {
		return itemId;
	}

	public void setItemId(ItemBean itemId) {
		this.itemId = itemId;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public String getStockKeppingUnitAtPurchasing() {
		return stockKeppingUnitAtPurchasing;
	}

	public void setStockKeppingUnitAtPurchasing(String stockKeppingUnitAtPurchasing) {
		this.stockKeppingUnitAtPurchasing = stockKeppingUnitAtPurchasing;
	}

	public Float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

	public Float getProductDiscount() {
		return productDiscount;
	}

	public void setProductDiscount(Float productDiscount) {
		this.productDiscount = productDiscount;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
