package library.payfine;

import library.entities.Library;
import library.entities.Member;

///////////////////////////////////////////////////////////////////////////////
//                   
// Subject:          Professional Programming Practice 
// @author:          Abhimanyu Bhat
// Email:            abhimanyubhat4296@gmail.com
// Lecturer's Name:  Recep Ulusoy
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////

public class PayFineControl {

	private PayFineUi ui;

	private enum ControlState {
		INITIALISED, READY, PAYING, COMPLETED, CANCELLED
	};

	private ControlState state;

	private Library library;
	private Member member;

	public PayFineControl() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}

	public void setUi(PayFineUi ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}
		this.ui = ui;
		ui.setState(PayFineUi.UiState.READY);
		state = ControlState.READY;
	}

	public void cardSwiped(int memberId) {
		if (!state.equals(ControlState.READY))
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");

		member = library.getMember(memberId);

		if (member == null) {
			ui.display("Invalid Member Id");
			return;
		}
		ui.display(member.toString());
		ui.setState(PayFineUi.UiState.PAYING);
		state = ControlState.PAYING;
	}

	public void cancel() {
		ui.setState(PayFineUi.UiState.CANCELLED);
		state = ControlState.CANCELLED;
	}

	public double payFine(double amount) {
		if (!state.equals(ControlState.PAYING))
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");

		double change = member.payFine(amount);
		if (change > 0)
			ui.display(String.format("Change: $%.2f", change));

		ui.display(member.toString());
		ui.setState(PayFineUi.UiState.COMPLETED);
		state = ControlState.COMPLETED;
		return change;
	}

}
