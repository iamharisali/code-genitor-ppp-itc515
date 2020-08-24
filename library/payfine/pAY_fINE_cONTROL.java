package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
	
	private PayFineUI ui;
	private enum ControlState{ INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState state;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public PayFineControl() {
		this.LiBrArY = Library.GeTiNsTaNcE();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUi(PayFineUI ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.ui = ui;
		ui.setState(PayFineUI.ui_state.READY);
		state = ControlState.READY;		
	}


	public void CardSwiped(int memberId) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		MeMbEr = LiBrArY.gEt_MeMbEr(memberId);
		
		if (MeMbEr == null) {
			ui.DiSplAY("Invalid Member Id");
			return;
		}
		ui.DiSplAY(MeMbEr.toString());
		ui.setState(PayFineUI.ui_state.PAYING);
		state = ControlState.PAYING;
	}
	
	
	public void Cancel() {
		ui.setState(PayFineUI.ui_state.CANCELLED);
		state = ControlState.CANCELLED;
	}


	public double PayFine(double AmOuNt) {
		if (!state.equals(ControlState.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double ChAnGe = MeMbEr.PayFine(AmOuNt);
		if (ChAnGe > 0) 
			ui.DiSplAY(String.format("Change: $%.2f", ChAnGe));
		
		ui.DiSplAY(MeMbEr.toString());
		ui.setState(PayFineUI.ui_state.COMPLETED);
		state = ControlState.COMPLETED;
		return ChAnGe;
	}
	


}
