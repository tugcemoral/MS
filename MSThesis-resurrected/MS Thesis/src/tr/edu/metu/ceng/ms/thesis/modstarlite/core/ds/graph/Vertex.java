package tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.graph;

import java.util.List;
import java.util.Vector;

public class Vertex {

	private List<Vertex> fromVertices;

//	private List<Vertex> toVertices;

	public int getInDegree() {
		return getFromVertices().size();
	}

//	public int getOutDegree() {
//		return getToVertices().size();
//	}

//	public void addToVertices(Vertex dest) {
//		this.getToVertices().add(dest);
//	}

	public void addFromVertices(Vertex src) {
		this.getFromVertices().add(src);
	}

//	public void removeToVertices(Vertex dest) {
//		this.getToVertices().remove(dest);
//	}

	public void removeFromVertices(Vertex src) {
		this.getFromVertices().remove(src);
	}

	public List<Vertex> getFromVertices() {
		if (fromVertices == null) {
			fromVertices = new Vector<Vertex>();
		}
		return fromVertices;
	}

//	public List<Vertex> getToVertices() {
//		if (toVertices == null) {
//			toVertices = new Vector<Vertex>();
//		}
//		return toVertices;
//	}

	public void setFromVertices(List<Vertex> fromVertices) {
		this.fromVertices = fromVertices;
	}
	
//	public void setToVertices(List<Vertex> toVertices) {
//		this.toVertices = toVertices;
//	}

}
