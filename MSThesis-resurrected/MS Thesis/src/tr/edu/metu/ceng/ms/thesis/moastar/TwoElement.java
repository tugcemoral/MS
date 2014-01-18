package tr.edu.metu.ceng.ms.thesis.moastar;
public class TwoElement {
	public double value1, value2;
	public boolean isDominated=false;
	public Cell cell;
	public Cell parentCell;
	public int parentCellDirection;
	public TwoElement parentTwoElement;
	public int side; 
	public double[] distanceTraveledInThreatZone_i;
	public long finishTime=Long.MAX_VALUE;
	
	public boolean isADiscoveredSolution=false;
	//public Vector distanceTraveledInThreatZone_i=new Vector(0,1);
	
	static public final TwoElementComparatorElem1 Comparator_Element1=new TwoElementComparatorElem1();
	static public final TwoElementComparatorElem2 Comparator_Element2=new TwoElementComparatorElem2();
	public TwoElement(Cell myCell,int mySide,double val1,double val2,int numThreats){
		value1=val1;
		value2=val2;
		cell=myCell;
		//parentCell=myParent;
		//parentCellDirection
		side=mySide;
		
		distanceTraveledInThreatZone_i=new double[numThreats];
	}
	
	public void add(TwoElement elem2){
		value1+=elem2.value1;
		value2+=elem2.value2;
	}
	
	public void setParent(Cell p, int dir,TwoElement te){
		parentCell=p;
		parentCellDirection=dir;
		parentTwoElement=te;
	}
	
	static public TwoElement add(TwoElement elem1, TwoElement elem2) throws Exception{
		if(elem1.distanceTraveledInThreatZone_i.length != elem1.distanceTraveledInThreatZone_i.length) throw new Exception("threat zone numbers not the same");
		return new TwoElement(elem1.cell, elem1.side,elem1.value1+elem2.value1, elem1.value2+elem2.value2,elem1.distanceTraveledInThreatZone_i.length);
	}
	
	public boolean dominates(TwoElement elem){
		if(  (this.value1<elem.value1 && this.value2<=elem.value2) ||
				(this.value1<=elem.value1 && this.value2<elem.value2)) return true;
		else{
			return false;
		}
	}
	public boolean nonDominates(TwoElement elem){
		if(  (this.value1<elem.value1 && this.value2>elem.value2) ||
				(this.value1>elem.value1 && this.value2<elem.value2)) return true;
		else{
			return false;
		}
	}
	
	public boolean dominatesOrEquals(TwoElement elem){
		if(  (this.value1<=elem.value1 && this.value2<=elem.value2)) return true;
		else{
			return false;
		}
	}
	
	public TwoElement duplicate(){
		TwoElement dup=new TwoElement(cell,side,value1,value2,this.distanceTraveledInThreatZone_i.length);
		for(int i=0;i<distanceTraveledInThreatZone_i.length;i++){
			dup.distanceTraveledInThreatZone_i[i]=this.distanceTraveledInThreatZone_i[i];
		}
		
		return dup;
	}

}