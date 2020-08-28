package library.returnBook;
import library.entities.Book;
import library.entities.Library;
import library.entities.Loan;

public class ReturnBookControl {

        private enum ControlState { INITIALISED, READY, INSPECTING };
        
	private ReturnBookUI returnBookUi;	
	private ControlState state;	
	private Library library;
	private Loan currentLoan;	

    /**
     *
     */
    public ReturnBookControl() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}	
	
    /**
     *
     * @param ui
     */
    public void setUi(ReturnBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
                }
                
		this.returnBookUi = ui;
		ui.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;		
	}

    /**
     *
     * @param bookId
     */
    public void bookScaned(int bookId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
                }
		
		Book currentBook = library.getBook(bookId);
		
		if (currentBook == null) {
			returnBookUi.display("Invalid Book Id");
			return;
		}
		if (!currentBook.inOnLoan()) {
			returnBookUi.display("Book has not been borrowed");
			return;
		}		
		currentLoan = library.getLoanByBookId(bookId);	
		double overDueFine = 0.0;
		if (currentLoan.isOverDue()) {
			overDueFine = library.calculateOverdueFines(currentLoan);
                }
                
		returnBookUi.display("Inspecting");
		returnBookUi.display(currentBook.toString());
		returnBookUi.display(currentLoan.toString());
		
		if (currentLoan.isOverDue()) {
			returnBookUi.display(String.format("\nOverdue fine : $%.2f", overDueFine));
                }
                
		returnBookUi.setState(ReturnBookUI.UiState.INSPECTING);
		state = ControlState.INSPECTING;		
	}

    /**
     *
     */
    public void scanningComplete() {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
                }
		returnBookUi.setState(ReturnBookUI.UiState.COMPLETED);		
	}

    /**
     *
     * @param isDamaged
     */
    public void dischargeLoan(boolean isDamaged) {
		if (!state.equals(ControlState.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
                }
                
		library.dischargeLoans(currentLoan, isDamaged);
		currentLoan = null;
		returnBookUi.setState(ReturnBookUI.UiState.READY);
		state = ControlState.READY;				
	}
}
