package tr.edu.metu.ceng.ms.thesis.moastar;

public class Agent {

	private double maxHP;
	private double HP;
	private double allowedDamage;
	private double minimumHealth;

	@Deprecated
	public Agent(double _hp, double _allowedDamagePercent) {
		if (_hp <= 0) {
			System.out.println("Agent hitpoints must be positive!");
			System.exit(1);
		}
		if (_allowedDamagePercent < 0 || _allowedDamagePercent > 1) {
			System.out
					.println("Agent allowed damage percentage must be between [0, 1]");
			System.exit(1);
		}
		maxHP = _hp;
		HP = maxHP;
		allowedDamage = maxHP * _allowedDamagePercent;
		minimumHealth = maxHP - allowedDamage;
	}

	public Agent() {
	}

	public double getMaxHP() {
		return maxHP;
	}

	public double getAllowedDamage() {
		return allowedDamage;
	}

	public void overrideAllowedDamage(double val) throws Exception {
		if (val < 0)
			throw new Exception("damage value cannot be overriden to <0.0");
		if (val > maxHP)
			throw new Exception("damage value cannot be overriden to >maxHP");
		allowedDamage = val;
	}
}
