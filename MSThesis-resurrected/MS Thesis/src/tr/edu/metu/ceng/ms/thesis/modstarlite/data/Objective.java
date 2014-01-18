package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

public class Objective {

	public static final Objective ZERO = new Objective(0d,
			ObjectiveBehaviour.MINIMIZED);

	public static final Objective INFINITY = new Objective(
			Double.MAX_VALUE, ObjectiveBehaviour.MINIMIZED);

	private double value;

	private ObjectiveBehaviour behaviour;

	public Objective(double value, ObjectiveBehaviour behaviour) {
		this.value = value;
		this.behaviour = behaviour;
	}

	public void setValue(double value) {
		if (Math.floor(value) != value && value > 10d)
			value = Math.floor(value);
		this.value = value;
	}

	public ObjectiveBehaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(ObjectiveBehaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public String toString() {
		return this.getValue() + " (" + this.getShortenedBehaviour() + ")";
	}

	public String toShortenString() {
		return String.valueOf(this.getValue());
	}

	private String getShortenedBehaviour() {
		return (this.getBehaviour().equals(ObjectiveBehaviour.MINIMIZED)) ? "MIN"
				: "MAX";
	}

	public double getValue() {
		if (Math.floor(value) != value && value > 10d)
			value = Math.floor(value);
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((behaviour == null) ? 0 : behaviour.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Objective other = (Objective) obj;
		if (behaviour != other.behaviour)
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

}
