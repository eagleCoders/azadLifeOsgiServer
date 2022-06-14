/**
 * 
 */
package inventory.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name="product_meta")
public class ProductMetaBean implements Serializable {

	@Id
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
