package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

///////////////////////////////////////////////////////////////////////////////
//                   
// Subject:          Professional Programming Practice 
// Author:           DON MEERIYAGALLA
// Email:            lakshansm90@gmail.com
// Lecturer's Name:  Recep Ulusoy
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////

public class ReturnBookControl {

	private ReturnBookUI returnBookUi;
	private enum ControlState { INITIALISED, READY, INSPECTING };
	private ControlState state;
	
	private Library library;
	private Loan currentLoan;
	

	public ReturnBookControl() {
		this.library = Library.GeTiNsTaNcE();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUi(ReturnBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
                }
                
		this.returnBookUi = ui;
		ui.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;		
	}


	public void bookScaned(int bookId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
                }
		
		Book currentBook = library.gEt_BoOk(bookId);
		
		if (currentBook == null) {
			returnBookUi.display("Invalid Book Id");
			return;
		}
		if (!currentBook.iS_On_LoAn()) {
			returnBookUi.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.GeT_LoAn_By_BoOkId(bookId);	
		double overDueFine = 0.0;
		if (currentLoan.Is_OvEr_DuE()) {
			overDueFine = library.CaLcUlAtE_OvEr_DuE_FiNe(currentLoan);
                }
                
		returnBookUi.display("Inspecting");
		returnBookUi.display(currentBook.toString());
		returnBookUi.display(currentLoan.toString());
		
		if (currentLoan.Is_OvEr_DuE()) {
			returnBookUi.display(String.format("\nOverdue fine : $%.2f", overDueFine));
                }
                
		returnBookUi.setState(ReturnBookUI.UiState.INSPECTING);
		state = ControlState.INSPECTING;		
	}


	public void scanningComplete() {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
                }
		returnBookUi.setState(ReturnBookUI.UiState.COMPLETED);		
	}


	public void disChargeLoan(boolean isDamaged) {
		if (!state.equals(ControlState.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
                }
                
		library.DiScHaRgE_LoAn(currentLoan, isDamaged);
		currentLoan = null;
		returnBookUi.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;				
	}


}
