package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

public class ThreatZone {

	private int size;

	private int area;

	private double risk;

	private ThreatZoneLevel threatZoneLevel;

	public ThreatZone(int size, double risk, ThreatZoneLevel tzl) {
		this.size = size;
		this.risk = risk;
		this.threatZoneLevel = tzl;

		if (!this.threatZoneLevel.equals(ThreatZoneLevel.Level3)) {
			this.area = (this.size == 1) ? 1 : (4 * (this.size - 1));
		} else {
			this.area = this.size * this.size;
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getRisk() {
		return risk;
	}

	public void setRisk(double risk) {
		this.risk = risk;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public ThreatZoneLevel getThreatZoneLevel() {
		return threatZoneLevel;
	}

	public void setThreatZoneLevel(ThreatZoneLevel threatZoneLevel) {
		this.threatZoneLevel = threatZoneLevel;
	}

}
