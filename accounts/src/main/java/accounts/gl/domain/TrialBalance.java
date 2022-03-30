/**
 * 
 */
package accounts.gl.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "m_trial_balance")
public class TrialBalance implements Serializable {

	@Id
	private Long id;
	
	@Column(name = "office_id", nullable = false)
    private Long officeId;

    @Column(name = "account_id", nullable = false)
    private Long glAccountId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "entry_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date entryDate;

    @Column(name = "created_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date transactionDate;

    @Column(name = "closing_balance", nullable = false)
    private BigDecimal closingBalance;
    
    public static TrialBalance getInstance(final Long officeId, final Long glAccountId, final BigDecimal amount, final Date entryDate,
            final Date transactionDate) {
        return new TrialBalance(officeId, glAccountId, amount, entryDate, transactionDate);
    }

    private TrialBalance(final Long officeId, final Long glAccountId, final BigDecimal amount, final Date entryDate,
            final Date transactionDate) {
        this.officeId = officeId;
        this.glAccountId = glAccountId;
        this.amount = amount;
        this.entryDate = entryDate;
        this.transactionDate = transactionDate;
    }

    protected TrialBalance() {}

    public Long getOfficeId() {
        return officeId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setClosingBalance(final BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Long getGlAccountId() {
        return glAccountId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TrialBalance)) {
            return false;
        }
        TrialBalance other = (TrialBalance) obj;
        return Objects.equals(other.officeId, officeId) && Objects.equals(other.glAccountId, glAccountId)
                && Objects.equals(other.amount, amount) && other.entryDate.compareTo(entryDate) == 0 ? Boolean.TRUE
                        : Boolean.FALSE && other.transactionDate.compareTo(transactionDate) == 0 ? Boolean.TRUE
                                : Boolean.FALSE && Objects.equals(other.closingBalance, closingBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(officeId, glAccountId, amount, entryDate, transactionDate, closingBalance);
    }
}
