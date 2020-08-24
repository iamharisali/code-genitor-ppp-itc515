package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

	private ReturnBookUI Ui;
	private enum ControlState { INITIALISED, READY, INSPECTING };
	private ControlState state;
	
	private Library library;
	private Loan currentLoan;
	

	public ReturnBookControl() {
		this.library = Library.GeTiNsTaNcE();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUi(ReturnBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		
		this.Ui = ui;
		ui.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;		
	}


	public void bookScaned(int bookId) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		
		Book currentBook = library.gEt_BoOk(bookId);
		
		if (currentBook == null) {
			Ui.display("Invalid Book Id");
			return;
		}
		if (!currentBook.iS_On_LoAn()) {
			Ui.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.GeT_LoAn_By_BoOkId(bookId);	
		double Over_Due_Fine = 0.0;
		if (currentLoan.Is_OvEr_DuE()) 
			Over_Due_Fine = library.CaLcUlAtE_OvEr_DuE_FiNe(currentLoan);
		
		Ui.display("Inspecting");
		Ui.display(currentBook.toString());
		Ui.display(currentLoan.toString());
		
		if (currentLoan.Is_OvEr_DuE()) 
			Ui.display(String.format("\nOverdue fine : $%.2f", Over_Due_Fine));
		
		Ui.setState(ReturnBookUI.UiState.INSPECTING);
		state = ControlState.INSPECTING;		
	}


	public void sCaNnInG_cOmPlEtE() {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(ReturnBookUI.UiState.COMPLETED);		
	}


	public void dIsChArGe_lOaN(boolean iS_dAmAgEd) {
		if (!state.equals(ControlState.INSPECTING)) 
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		
		library.DiScHaRgE_LoAn(currentLoan, iS_dAmAgEd);
		currentLoan = null;
		Ui.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;				
	}


}
