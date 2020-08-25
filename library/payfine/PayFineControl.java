package library.payfine;

import library.entities.Library;
import library.entities.Member;

public class PayFineControl {

	private PayFineUi ui;

	private enum ControlState {
		INITIALISED, READY, PAYING, COMPLETED, CANCELLED
	};

	private ControlState state;

	private Library LiBrArY;
	private Member member;

	public PayFineControl() {
		this.LiBrArY = Library.GeTiNsTaNcE();
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

	public void CardSwiped(int memberId) {
		if (!state.equals(ControlState.READY))
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");

		member = LiBrArY.getMember(memberId);

		if (member == null) {
			ui.display("Invalid Member Id");
			return;
		}
		ui.display(member.toString());
		ui.setState(PayFineUi.UiState.PAYING);
		state = ControlState.PAYING;
	}

	public void Cancel() {
		ui.setState(PayFineUi.UiState.CANCELLED);
		state = ControlState.CANCELLED;
	}

	public double payFine(double AmOuNt) {
		if (!state.equals(ControlState.PAYING))
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");

		double ChAnGe = member.payFine(AmOuNt);
		if (ChAnGe > 0)
			ui.display(String.format("Change: $%.2f", ChAnGe));

		ui.display(member.toString());
		ui.setState(PayFineUi.UiState.COMPLETED);
		state = ControlState.COMPLETED;
		return ChAnGe;
	}

}