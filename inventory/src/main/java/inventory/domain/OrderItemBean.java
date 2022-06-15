/**
 * 
 */
package inventory.domain;

import java.io.Serializable;

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
}
