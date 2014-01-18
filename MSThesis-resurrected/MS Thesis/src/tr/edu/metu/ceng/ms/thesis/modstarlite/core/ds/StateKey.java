package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph.Vertex;

public final class StateKey<State> extends Vertex{

	private final State s;
	private Key k;

	public StateKey(State s, Key k) {
		this.s = s;
		this.k = k;
	}

//	@Override
//	public boolean equals(Object obj) {
//		// We do a dangerous unchecked comparison (no null-test, etc.)
//		// because this is a private class and should be well-behaved.
//		StateKey that = (StateKey) obj;
//		return (this.s.equals(that.s));
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "{" + s.toString() + " = " + k.toString() + "}";
	}

	public Key getK() {
		return k;
	}

	public void setK(Key k) {
		this.k = k;
	}

	public State getS() {
		return s;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateKey other = (StateKey) obj;
		if (k == null) {
			if (other.k != null)
				return false;
		} else if (!k.equals(other.k))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

}