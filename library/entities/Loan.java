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

	public Loan(int loanId, Book book, Member member, Date dueDate) {
		this.loanId = loanId;
		this.book = book;
		this.member = member;
		this.date = dueDate;
		this.state = LoanState.CURRENT;
	}

	public void cHeCk_OvEr_DuE() {
		if (state == LoanState.CURRENT && Calendar.getInstance().getDate().after(date)) {
			this.state = LoanState.OVER_DUE;
		}

	}

	public boolean Is_OvEr_DuE() {
		return state == LoanState.OVER_DUE;
	}

	public Integer GeT_Id() {
		return loanId;
	}

	public Date GeT_DuE_DaTe() {
		return date;
	}

	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder sb = new StringBuilder();
		sb.append("Loan:  ").append(loanId).append("\n").append("  Borrower ").append(member.getId()).append(" : ")
				.append(member.getLastName()).append(", ").append(member.getFirstName()).append("\n").append("  Book ")
				.append(book.getId()).append(" : ").append(book.getTitle()).append("\n").append("  DueDate: ")
				.append(sdf.format(date)).append("\n").append("  State: ").append(state);
		return sb.toString();
	}

	public Member GeT_MeMbEr() {
		return member;
	}

	public Book GeT_BoOk() {
		return book;
	}

	public void DiScHaRgE() {
		state = LoanState.DISCHARGED;
	}

}
