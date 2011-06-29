package tr.edu.metu.ceng.wbsa.autocomplete;

import java.util.List;
import java.util.Vector;

public class City {

	private String name;

	private List<String> counties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCounties() {
		if (counties == null) {
			counties = new Vector<String>();
		}
		return counties;
	}

	public void setCounties(List<String> counties) {
		this.counties = counties;
	}

	public void addCounty(String county) {
		this.getCounties().add(county);
	}

}
