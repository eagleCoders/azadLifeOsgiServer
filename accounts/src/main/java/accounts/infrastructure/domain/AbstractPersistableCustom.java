/**
 * 
 */
package accounts.infrastructure.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

/**
 * @author anees-ur-rehman
 *
 */
//@MappedSuperclass
public abstract class AbstractPersistableCustom implements  Serializable {

//	private static final long serialVersionUID = 9181640245194392646L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Transient
//    private boolean isNew = true;
//
//
//    protected void setId(final Long id) {
//        this.id = id;
//    }
//
//    @PrePersist
//    @PostLoad
//    void markNotNew() {
//        this.isNew = false;
//    }
}
