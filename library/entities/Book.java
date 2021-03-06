package library.entities;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private String title;
	private String author;
	private String callNumber;
	private int id;
	
	private enum State { AVAILABLE, ON_LOAN, DAMAGED, RESERVED };
	private State state;
	
	
	public Book(String author, String title, String callNumber, int id) {
		this.author = author;
		this.title = title;
		this.callNumber = callNumber;
		this.id = id;
		this.state = State.AVAILABLE;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Book: ").append(id).append("\n")
		  .append("  Title:  ").append(title).append("\n")
		  .append("  Author: ").append(author).append("\n")
		  .append("  CallNo: ").append(callNumber).append("\n")
		  .append("  State:  ").append(state);
		
		return sb.toString();
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}


	
	public boolean isAvailable() {
		return state == State.AVAILABLE;
	}

	
	public boolean inOnLoan() {
		return state == State.ON_LOAN;
	}

	
	public boolean isDamaged() {
		return state == State.DAMAGED;
	}

	
	public void borrow() {
		if (state.equals(State.AVAILABLE)) 
			state = State.ON_LOAN;
		
		else 
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", state));
		
		
	}

	public void returnState(boolean damaged) {
		if (state.equals(State.ON_LOAN)) {
      if (damaged) 
				state = State.DAMAGED;
			
			else 
				state = State.AVAILABLE;
    }
			
	  else 
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", state));
				
	}

	
	public void repair() {
		if (state.equals(State.DAMAGED)) 
			state = State.AVAILABLE;
		
		else 
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", state));
		
	}


}
