package tr.edu.metu.ceng.postit.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sentence Model
 */
public class Sentence {

	private List<Token> tokens = null;

	public Sentence() {
		this.tokens = new ArrayList<Token>();
	}

	/**
	 * Append a token to sentence
	 * 
	 * @param token
	 * @return current sentence
	 */
	public Sentence addToken(Token token) {
		tokens.add(token);
		return this;
	}

	/**
	 * @return tokens as a list
	 */
	public List<Token> getTokens() {
		return tokens;
	}

	/**
	 * @return true if current sentence contains nothing
	 */
	public boolean isEmpty() {
		return this.tokens.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for(int i=0;i<tokens.size();i++){
			if(i<tokens.size()-1){
				sb.append(tokens.get(i)).append(", ");
			}else{
				sb.append(tokens.get(i));
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @return sentence length
	 */
	public int length() {
		return this.tokens.size();
	}

	/**
	 * sort words by input order
	 */
	public Sentence sort() {
		Collections.sort(tokens);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}
}
