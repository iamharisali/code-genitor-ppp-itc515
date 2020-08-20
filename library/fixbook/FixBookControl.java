package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
	
	private FixBookUI Ui;
	private enum ControlState { INITIALISED, READY, FIXING };
	private ControlState state;
	
	private Library library;
	private Book currentBook;


	public void fixBookControl() {
		this.library = Library.getInstance();
		state = ControlState.INITIALISED;
	}
	
	
	public void setUi( FixBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.Ui = ui;
		ui.setState(FixBookUI.UiState.READY);
		state = ControlState.READY;		
	}


	public void getBookScanned(int bookId) {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
			
		currentBook = library.getBook(bookId);
		
		if (currentBook == null) {
			Ui.display("Invalid bookId");
			return;
		}
		if (!currentBook.isDamaged()) {
			Ui.display("Book has not been damaged");
			return;
		}
		Ui.display(currentBook.toString());
		Ui.setState(FixBookUI.UiState.FIXING);
		state = ControlState.FIXING;		
	}


	public void toFixBook(boolean mustFix) {
		if (!state.equals(ControlState.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mustFix) 
			library.repairBook(currentBook);
		
		currentBook = null;
		Ui.setState(FixBookUI.UiState.READY);
		state = ControlState.READY;		
	}

	
	public void getScanningComplete() {
		if (!state.equals(ControlState.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(FixBookUI.UiState.COMPLETED);		
	}

}
