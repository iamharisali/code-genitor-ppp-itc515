package library.payfine;
import library.entities.Library;
import library.entities.Member;

public class PayFineControl {
	
	private PayFineUI Ui;
	private enum ControlState{ INITIALISED, READY, PAYING, COMPLETED, CANCELLED };
	private ControlState StAtE;
	
	private Library LiBrArY;
	private Member MeMbEr;


	public PayFineControl() {
		this.LiBrArY = Library.GeTiNsTaNcE();
		StAtE = ControlState.INITIALISED;
	}
	
	
	public void SetUi(PayFineUI uI) {
		if (!StAtE.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}	
		this.Ui = uI;
		uI.SeT_StAtE(PayFineUI.uI_sTaTe.READY);
		StAtE = ControlState.READY;		
	}


	public void CardSwiped(int memberId) {
		if (!StAtE.equals(ControlState.READY)) 
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
			
		MeMbEr = LiBrArY.gEt_MeMbEr(memberId);
		
		if (MeMbEr == null) {
			Ui.DiSplAY("Invalid Member Id");
			return;
		}
		Ui.DiSplAY(MeMbEr.toString());
		Ui.SeT_StAtE(PayFineUI.uI_sTaTe.PAYING);
		StAtE = ControlState.PAYING;
	}
	
	
	public void Cancel() {
		Ui.SeT_StAtE(PayFineUI.uI_sTaTe.CANCELLED);
		StAtE = ControlState.CANCELLED;
	}


	public double PayFine(double AmOuNt) {
		if (!StAtE.equals(ControlState.PAYING)) 
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
			
		double ChAnGe = MeMbEr.PayFine(AmOuNt);
		if (ChAnGe > 0) 
			Ui.DiSplAY(String.format("Change: $%.2f", ChAnGe));
		
		Ui.DiSplAY(MeMbEr.toString());
		Ui.SeT_StAtE(PayFineUI.uI_sTaTe.COMPLETED);
		StAtE = ControlState.COMPLETED;
		return ChAnGe;
	}
	


}
