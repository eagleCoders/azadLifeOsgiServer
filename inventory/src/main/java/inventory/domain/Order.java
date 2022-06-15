package inventory.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Order
 *
 */
@Entity
@Table(name="order")
@SequenceGenerator(name="azd_order", sequenceName="azd_orderSeq")
public class Order implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_order")
	private Integer orderId;

	@Column(name="userId", length= 10485760, nullable = false)
	private String userId;
	
	@Column(name="type")
	private Integer orderType;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="subTotal")
	private Long subTotal;
	
	@Column(name="itemDiscount")
	private Long itemDiscount;
	
	@Column(name="tax")
	private Float tax;
	
	@Column(name="shipping")
	private Float shipping;
	
	@Column(name = "total")
	private Long total;
	
	@Column(name = "promo")
	private String promoCode;
	
	@Column(name = "discount")
	private Float discount;
	
	@Column(name = "grandTotal" )
	private Long grandTotal;
	
	@Column(name="createdAt", nullable = false)
	private Date createDt;
	
	@Column(name="updatedAt")
	private Date updateDt;

	@Column(name="content", length = 10485760)
	private String content;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId", referencedColumnName = "id")
	private List<OrderAddress> orderAddressList;
	
	@OneToMany(targetEntity = ProductMetaBean.class)
	@JoinColumn(name = "orderId", referencedColumnName = "id")
	private List<ItemBean> orederedItemsList;

	public Order() {
		super();
	}

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the orderType
	 */
	public Integer getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the subTotal
	 */
	public Long getSubTotal() {
		return subTotal;
	}

	/**
	 * @param subTotal the subTotal to set
	 */
	public void setSubTotal(Long subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * @return the itemDiscount
	 */
	public Long getItemDiscount() {
		return itemDiscount;
	}

	/**
	 * @param itemDiscount the itemDiscount to set
	 */
	public void setItemDiscount(Long itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	/**
	 * @return the tax
	 */
	public Float getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(Float tax) {
		this.tax = tax;
	}

	/**
	 * @return the shipping
	 */
	public Float getShipping() {
		return shipping;
	}

	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(Float shipping) {
		this.shipping = shipping;
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * @return the promoCode
	 */
	public String getPromoCode() {
		return promoCode;
	}

	/**
	 * @param promoCode the promoCode to set
	 */
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	/**
	 * @return the discount
	 */
	public Float getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	/**
	 * @return the grandTotal
	 */
	public Long getGrandTotal() {
		return grandTotal;
	}

	/**
	 * @param grandTotal the grandTotal to set
	 */
	public void setGrandTotal(Long grandTotal) {
		this.grandTotal = grandTotal;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	
}
