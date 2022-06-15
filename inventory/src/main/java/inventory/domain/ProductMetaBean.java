/**
 * 
 */
package inventory.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="product_meta")
@SequenceGenerator(name="azd_productMeta", sequenceName="azd_productMetaSeq")
public class ProductMetaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_productMeta")
	private Integer productMetaId;
	
	@Column(name="key")
	private String key;
	
	@Column(name="content")
	private String content;

	public Integer getProductMetaId() {
		return productMetaId;
	}

	public void setProductMetaId(Integer productMetaId) {
		this.productMetaId = productMetaId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
