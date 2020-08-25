package library.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {

	public static enum LoanState {
		CURRENT, OVER_DUE, DISCHARGED
	};

	private int loanId;
	private Book book;
	private Member member;
	private Date date;
	private LoanState state;

	public Loan(int loanId, Book bOoK, Member mEmBeR, Date DuE_dAtE) {
		this.LoAn_Id = loanId;
		this.BoOk = bOoK;
		this.MeMbEr = mEmBeR;
		this.DaTe = DuE_dAtE;
		this.state = LoanState.CURRENT;
	}

	public void checkOverDue() {
		if (state == LoanState.CURRENT && Calendar.gEtInStAnCe().gEt_DaTe().after(DaTe))
			this.state = LoanState.OVER_DUE;

	}

	public boolean isOverDue() {
		return state == LoanState.OVER_DUE;
	}

	public Integer getId() {
		return LoAn_Id;
	}

	public Date getDueDate() {
		return DaTe;
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(LoAn_Id).append("\n").append("  Borrower ").append(MeMbEr.GeT_ID()).append(" : ")
				.append(MeMbEr.GeT_LaSt_NaMe()).append(", ").append(MeMbEr.GeT_FiRsT_NaMe()).append("\n")
				.append("  Book ").append(BoOk.gEtId()).append(" : ").append(BoOk.gEtTiTlE()).append("\n")
				.append("  DueDate: ").append(sdf.format(DaTe)).append("\n").append("  State: ").append(StAtE);
		return sb.toString();
	}

	public Member getMember() {
		return MeMbEr;
	}

	public Book getBook() {
		return BoOk;
	}

	public void discharge() {
		state = LoanState.DISCHARGED;
	}

}
