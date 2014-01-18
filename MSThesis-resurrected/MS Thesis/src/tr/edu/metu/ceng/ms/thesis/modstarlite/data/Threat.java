package tr.edu.metu.ceng.ms.thesis.modstarlite.data;

import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;

public class Threat {

	private int size;

	private int differenceBetweenZones;

	private Coordinate startCoord;

	// center coordinates for this threat.
	private double centerX;
	private double centerY;

	private ThreatZone level1;

	private ThreatZone level2;

	private ThreatZone level3;

	public Threat(IntCoord startCoord, int size, int differenceBetweenZones) {
		this.size = size;
		this.startCoord = startCoord;
		this.differenceBetweenZones = differenceBetweenZones;

		double halfSize = (size / 2);
		this.centerX = startCoord.get()[0] + halfSize;
		this.centerY = startCoord.get()[1] + halfSize;

		// assign size and generate level1.
		level1 = new ThreatZone(size, Math.random() * 100,
				ThreatZoneLevel.Level1);
		// possibility to generate threat zone 2 and 3.
		if (size - differenceBetweenZones >= 1) {
			// re-assign size (of level2)
			size = size - differenceBetweenZones;
			level2 = new ThreatZone(size, Math.random() * 100
					+ level1.getRisk(), ThreatZoneLevel.Level2);
			// possibility to generate threat zone 3.
			if (size - differenceBetweenZones >= 1) {
				// re-assign size (of level3)
				size = size - differenceBetweenZones;
				level3 = new ThreatZone(size, Math.random() * 100
						+ level2.getRisk(), ThreatZoneLevel.Level3);
			}
		}
	}

	public Threat(IntCoord startCoord, int size, int differenceBetweenZones,
			double... levelRisks) {
		this(startCoord, size, differenceBetweenZones);
		if (getLevel1() != null) {
			getLevel1().setRisk(levelRisks[0]);
		}
		if (getLevel2() != null) {
			getLevel2().setRisk(levelRisks[1]);
		}
		if (getLevel3() != null) {
			getLevel3().setRisk(levelRisks[2]);
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Coordinate getStartCoord() {
		return startCoord;
	}

	public void setStartCoord(Coordinate startCoord) {
		this.startCoord = startCoord;
	}

	public double getThreatZoneRisk(int x, int y) {
		ThreatZone correspondingTZone = null;

		// get start points...
		int startX = (int) getStartCoord().get(0);
		int startY = (int) getStartCoord().get(1);

		// : In level1
		// (startX <= x < startX + size) && (startY <= y < startY + size)
		if ((x == startX || x == startX + getSize() - 1)
				|| (y == startY || y == startY + getSize() - 1)) {
			correspondingTZone = getLevel1();
		}
		// : In level2
		// (startX + 1 <= x < startX + size -1) && (startY + 1 <= y <startY +
		// size -1)
		else if ((x == startX + 1 || x == startX + getSize() - 2)
				|| (y == startY + 1 || y == startY + getSize() - 2)) {
			correspondingTZone = getLevel2();
		}
		// o/w, tzone 3.
		else {
			correspondingTZone = getLevel3();
		}

		if (correspondingTZone == null) {
			// FIXME : burasini duzelt, differencyBetweenZones kullanarak.
			correspondingTZone = getLevel1();
		}

		return correspondingTZone.getRisk();
	}

	public ThreatZone getLevel1() {
		return level1;
	}

	public void setLevel1(ThreatZone level1) {
		this.level1 = level1;
	}

	public ThreatZone getLevel2() {
		return level2;
	}

	public void setLevel2(ThreatZone level2) {
		this.level2 = level2;
	}

	public ThreatZone getLevel3() {
		return level3;
	}

	public void setLevel3(ThreatZone level3) {
		this.level3 = level3;
	}

	public double getRiskByCoordinate(int row, int col) {

		double[] startCoordinates = this.getStartCoord().get();

		if (((startCoordinates[1] + this.size >= row) && (startCoordinates[1] <= row))
				&& ((startCoordinates[0] + this.size >= col) && (startCoordinates[0] <= col))) {
			return getLevel1().getRisk();
		} else {
			return 0.0;
		}
	}

	public double distanceToDamage(double dist, double speed) {
		// required for MOA*, default values for perception and cooldown
		int perceptionTime = 0;
		int coolDown = 1;
		double damage = getLevel1().getRisk();

		double time = dist / speed - perceptionTime;
		if (time > 0) {
			return (Math.floor(time / coolDown) + 1) * damage;
		} else {
			return 0;
		}
	}

	public String toPropString() {
		String propStr = this.getStartCoord().get(0) + ", "
				+ this.getStartCoord().get(1) + ", " + this.getSize() + ", "
				+ this.getDifferenceBetweenZones();

		propStr += ", "
				+ ((getLevel1() != null) ? String
						.valueOf(Math.round(getLevel1().getRisk())) : "0");
		propStr += ", "
				+ ((getLevel2() != null) ? String
						.valueOf(Math.round(getLevel2().getRisk())) : "0");
		propStr += ", "
				+ ((getLevel3() != null) ? String
						.valueOf(Math.round(getLevel3().getRisk())) : "0");

		return propStr;
	}

	public int getDifferenceBetweenZones() {
		return differenceBetweenZones;
	}

	public void setDifferenceBetweenZones(int differenceBetweenZones) {
		this.differenceBetweenZones = differenceBetweenZones;
	}

	public double getCenterX() {
		return centerX;
	}

	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}

}
