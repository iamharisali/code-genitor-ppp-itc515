package library.fixbook;
import library.entities.Book;
import library.entities.Library;

public class fixbookControl {
	
	private fixbookUi Ui;
	private enum controlState { INITIALISED, READY, FIXING };
	private controlState state;
	
	private Library library;
	private Book currentBook;


	public fixbookControl() {
		this.library = Library.GeTiNsTaNcE();
		state = controlState.INITIALISED;
	}
	
	
	public void SetUi( fixbookUi ui) {
		if (!state.equals(controlState.INITIALISED)) 
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
			
		this.Ui = ui;
		ui.setState(fixbookUi.uiState.READY);
		state = controlState.READY;		
	}


	public void BoOk_ScAnNeD(int BoOkId) {
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
		Ui.setState(fixbookUi.uiState.FIXING);
		state = controlState.FIXING;		
	}


	public void FiX_BoOk(boolean mUsT_FiX) {
		if (!state.equals(controlState.FIXING)) 
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
			
		if (mUsT_FiX) 
			library.RePaIr_BoOk(currentBook);
		
		currentBook = null;
		Ui.setState(fixbookUi.uiState.READY);
		state = controlState.READY;		
	}

	
	public void SCannING_COMplete() {
		if (!state.equals(controlState.READY)) 
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
			
		Ui.setState(fixbookUi.uiState.COMPLETED);		
	}

}
