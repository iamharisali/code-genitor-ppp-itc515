package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

	private ReturnBookUI Ui;
	private enum ControlState { INITIALISED, READY, INSPECTING };
	private ControlState sTaTe;
	
	private Library lIbRaRy;
	private Loan currentLoan;
	

	public ReturnBookControl() {
		this.lIbRaRy = Library.GeTiNsTaNcE();
		sTaTe = ControlState.INITIALISED;
	}
	
	
	public void sEt_uI(ReturnBookUI uI) {
		if (!sTaTe.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.Ui = uI;
		uI.setState(ReturnBookUI.UiState.READY);
		sTaTe = ControlState.READY;		
	}


	public void bOoK_sCaNnEd(int bOoK_iD) {
		if (!sTaTe.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book cUrReNt_bOoK = lIbRaRy.gEt_BoOk(bOoK_iD);
		
		if (cUrReNt_bOoK == null) {
			Ui.display("Invalid Book Id");
			return;
		}
		if (!cUrReNt_bOoK.iS_On_LoAn()) {
			Ui.display("Book has not been borrowed");
			return;
		}		
		currentLoan = lIbRaRy.GeT_LoAn_By_BoOkId(bOoK_iD);	
		double Over_Due_Fine = 0.0;
		if (currentLoan.Is_OvEr_DuE()) 
			Over_Due_Fine = lIbRaRy.CaLcUlAtE_OvEr_DuE_FiNe(currentLoan);
		
		Ui.display("Inspecting");
		Ui.display(cUrReNt_bOoK.toString());
		Ui.display(currentLoan.toString());
		
		if (currentLoan.Is_OvEr_DuE()) 
			Ui.display(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
		
		Ui.setState(ReturnBookUI.UiState.INSPECTING);
		sTaTe = ControlState.INSPECTING;		
	}


	public void sCaNnInG_cOmPlEtE() {
		if (!sTaTe.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(ReturnBookUI.UiState.COMPLETED);		
	}


	public void dIsChArGe_lOaN(boolean iS_dAmAgEd) {
		if (!sTaTe.equals(ControlState.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		lIbRaRy.DiScHaRgE_LoAn(currentLoan, iS_dAmAgEd);
		currentLoan = null;
		Ui.setState(ReturnBookUI.UiState.READY);
		sTaTe = ControlState.READY;				
	}


}
