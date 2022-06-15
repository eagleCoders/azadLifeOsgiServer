/**
 * 
 */
package inventory.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author azadlife
 *
 */
@Entity
@Table(name="category")
public class CategoryBean implements Serializable {

	@Id
	@Column(name="id")
	private Integer categoryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private CategoryBean parent;
	
	@Column(name="title", nullable = false)
	private String title;
	
	@Column(name="metaTitle")
	private String metaTitle;
	
	@Column(name="slug")
	private String slug;

	@Column(name="content", length = 10485760)
	private String content;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryBean getParent() {
		return parent;
	}

	public void setParent(CategoryBean parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
