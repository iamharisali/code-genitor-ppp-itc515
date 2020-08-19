package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class FixBookControl {
	
	private FixBookUI Ui;
	private enum controlState { INITIALISED, READY, FIXING };
	private controlState state;
	
	private Library library;
	private Book currentBook;


	public fixbookControl() {
		this.library = Library.getInstance();
		state = controlState.INITIALISED;
	}
	
	
	public void setUi( FixBookUI ui) {
		if (!state.equals(controlState.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.Ui = ui;
		ui.setState(FixBookUI.uiState.READY);
		state = controlState.READY;		
	}


	public void getBookScanned(int BoOkId) {
		if (!state.equals(controlState.READY)) 
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
			
		currentBook = library.gEt_BoOk(BoOkId);
		
		if (currentBook == null) {
			Ui.dIsPlAy("Invalid bookId");
			return;
		}
		if (!currentBook.iS_DaMaGeD()) {
			Ui.dIsPlAy("Book has not been damaged");
			return;
		}
		Ui.dIsPlAy(currentBook.toString());
		Ui.setState(FixBookUI.uiState.FIXING);
		state = controlState.FIXING;		
	}


	public void FiX_BoOk(boolean mUsT_FiX) {
		if (!state.equals(controlState.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			library.RePaIr_BoOk(currentBook);
		
		currentBook = null;
		Ui.setState(FixBookUI.uiState.READY);
		state = controlState.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!state.equals(controlState.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(FixBookUI.uiState.COMPLETED);		
	}

}
