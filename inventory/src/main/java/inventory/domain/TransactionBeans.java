package inventory.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: TransactionBeans
 *
 */
@Entity
@Table(name="transaction")
@SequenceGenerator(name="azd_transaction", sequenceName="azd_productsMasterSeq")
public class TransactionBeans implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azd_transaction")
	private Integer transactionId;

	@Column(name="userId", length = 10485760)
	private String globalId;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="orderId", referencedColumnName = "id")
	private Order order;
	
	@Column(name="code", length=10485760)
	private String paymentCode;
	
	// it will be Credit, Debit
	@Column(name="type")
	private Integer transactionType;
	
	// Offline, COD, Cheque, Draft, Wired, and Online
	@Column(name="mode")
	private Integer modeOfOrderTransaction;
	
	// New,Cancelled, Failed, pending, Declined, Rejected, and Success
	@Column(name="status")
	private Integer statusOfOrderTransaction;
	
	@Column(name="createdAt", nullable = true)
	private Date createDt ;
	
	@Column(name="updatedAt")
	private Date updateDt;
	
	@Column(name="content", length =10485760 )
	private String content;

	
	
	public TransactionBeans() {
		super();
	}



	public Integer getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}



	public String getGlobalId() {
		return globalId;
	}



	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}



	public Order getOrder() {
		return order;
	}



	public void setOrder(Order order) {
		this.order = order;
	}



	public String getPaymentCode() {
		return paymentCode;
	}



	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}



	public Integer getTransactionType() {
		return transactionType;
	}



	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}



	public Integer getModeOfOrderTransaction() {
		return modeOfOrderTransaction;
	}



	public void setModeOfOrderTransaction(Integer modeOfOrderTransaction) {
		this.modeOfOrderTransaction = modeOfOrderTransaction;
	}



	public Integer getStatusOfOrderTransaction() {
		return statusOfOrderTransaction;
	}



	public void setStatusOfOrderTransaction(Integer statusOfOrderTransaction) {
		this.statusOfOrderTransaction = statusOfOrderTransaction;
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
