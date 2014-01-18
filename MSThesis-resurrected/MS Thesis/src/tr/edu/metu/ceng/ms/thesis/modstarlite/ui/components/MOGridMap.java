package tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;

public interface MOGridMap {

	public void resize(int... sizes);

	public int length();

	public int size(int dim);

	public int[] sizes();

	public int dims();

	public MOState get(int... idx);

	public boolean hasObstacle(int... idx);
	
	public void setHasObstacle(boolean hasObstacle, int... idx);
	
//	public boolean isTraversable(int... idx);
	
	public boolean isInViewingFrustumArea(int... idx);
	
	public void setInViewingFrustum(boolean isInViewingFrustum, int... idx);

	public void set(MOState val, int... idx);

	public int getObjectiveCount();
	
	public ObjectiveBehaviour[] getObjectiveBehaviours();

	public boolean insideBounds(int... idx);

}
