package inventory.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: BrandBean
 *
 */
@Entity
@Table(name="brand")
public class BrandBean implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private Integer brandId;
   
	@Column(name="title")
	private String title;
	
	@Column(name="summary")
	private String summary;
	
}
