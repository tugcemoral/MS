package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.ms.thesis.modstarlite.core.ds.Key;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;

public class MultiObjectiveUtils {

	// public static List<ObjectiveArray> sum(List<ObjectiveArray> list1,
	// List<ObjectiveArray> list2) {
	// // create a new list of objective array.
	// List<ObjectiveArray> sumList = new Vector<ObjectiveArray>();
	//
	// for (ObjectiveArray objectiveArray : list1) {
	// sumList.addAll(sum(objectiveArray, list2));
	// }
	//
	// return sumList;
	// }

	public static List<ObjectiveArray> sum(ObjectiveArray objArray,
			List<ObjectiveArray> listOfObjArray) {
//		if (hasAnyDominatableObjectiveArray(listOfObjArray)) {
//			System.err.println("!!!This has happened to me");
//		}
		// create a new list of objective array.
		List<ObjectiveArray> sumList = new Vector<ObjectiveArray>();

		// for each objective array in given parameter obj array list, add
		// instance with objArray parameter.
		for (ObjectiveArray objectiveArray : listOfObjArray) {
			sumList.add(sum(objArray, objectiveArray));
		}

//		if (hasAnyDominatableObjectiveArray(sumList)) {
//			System.err.println("!!!This has happened to me");
//		}
		return sumList;
	}

	private static boolean hasAnyDominatableObjectiveArray(
			List<ObjectiveArray> list) {

		for (int i = 0; i < list.size(); i++) {
			ObjectiveArray actualOA = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				ObjectiveArray tmpOA = list.get(j);
				if (actualOA.dominates(tmpOA)) {
					System.out.println("Actual -> " + actualOA);
					System.out.println("Tmp -> " + tmpOA);
					System.out.println("----------------------");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * NOTICE: that objectives of {@link ObjectiveArray} instances are additive.
	 * 
	 * @param objArray
	 * @param objectiveArray
	 * @return
	 */
	public static ObjectiveArray sum(ObjectiveArray objArray1,
			ObjectiveArray objArray2) {

		if (objArray1.size() != objArray2.size()) {
			throw new InconsistentObjectiveSizeException();
		}

		// create a new objective array that holds sums...
		ObjectiveArray summedArray = new ObjectiveArray(objArray1.size());

		for (int i = 0; i < objArray1.size(); i++) {
			if (objArray1.get(i).getBehaviour() != objArray2.get(i)
					.getBehaviour()) {
				throw new InconsistentObjectiveBehaviourException();
			}

			// create a new objective and put it into summed array.
			Objective summedObj = new Objective(objArray1.get(i).getValue()
					+ objArray2.get(i).getValue(), objArray1.get(i)
					.getBehaviour());
			summedArray.put(i, summedObj);
		}
		return summedArray;
	}

	// public static MOState determineDominatedState(MOState stateA, MOState
	// stateB) {
	//
	// // get objectives from states.
	// Objective[] objectivesA = stateA.getObjectives();
	// Objective[] objectivesB = stateB.getObjectives();
	//
	// // check that sizes are equal.
	// if (objectivesA.length != objectivesB.length) {
	// throw new IllegalStateException(
	// "Sizes of objectives of neighbor states should be same");
	// }
	//
	// int dominationCountForA = 0;
	// int dominationCountForB = 0;
	//
	// for (int i = 0; i < objectivesA.length; i++) {
	// // get corresponding objectives to determine domination.
	// Objective objectiveA = objectivesA[i];
	// Objective objectiveB = objectivesB[i];
	//
	// if (!objectiveA.getBehaviour().equals(objectiveB.getBehaviour())) {
	// throw new IllegalStateException(
	// "Equivalent objectives must have same behaviour!");
	// }
	//
	// // if this objective is designed to minimize.
	// if (objectiveA.getBehaviour().equals(ObjectiveBehaviour.MINIMIZED)) {
	// if (objectiveA.getValue() < objectiveB.getValue()) {
	// dominationCountForA++;
	// } else if (objectiveB.getValue() < objectiveA.getValue()) {
	// dominationCountForB++;
	// }
	// } else {
	// if (objectiveA.getValue() > objectiveB.getValue()) {
	// dominationCountForA++;
	// } else if (objectiveB.getValue() > objectiveA.getValue()) {
	// dominationCountForB++;
	// }
	// }
	// }
	//
	// if (dominationCountForA == objectivesA.length) {
	// return stateA;
	// } else if (dominationCountForB == objectivesB.length) {
	// return stateB;
	// } else {
	// return null;
	// }
	// }

	public static boolean equals(List<ObjectiveArray> list1,
			List<ObjectiveArray> list2) {

		if (list1.size() != list2.size()) {
			return false;
		}
		
		List<ObjectiveArray> tmpList1 = new Vector<ObjectiveArray>();
		List<ObjectiveArray> tmpList2 = new Vector<ObjectiveArray>();
		tmpList1.addAll(list1);
		tmpList2.addAll(list2);
		
		for (ObjectiveArray firstObjArray : list1) {
			if(list2.contains(firstObjArray)){
				tmpList1.remove(firstObjArray);
				tmpList2.remove(firstObjArray);
			}else {
				return false;
			}
		}
		
		if(tmpList1.size() == 0 && tmpList2.size() == 0){
			return true;
		}
		return false;
		
//		// FIXME: should [(4,6), (6,5)] equals to [(6,5), (4,6)] ???
//		for (int i = 0; i < list1.size(); i++) {
//			ObjectiveArray firstObjArray = list1.get(i);
//			ObjectiveArray secondObjArray = list2.get(i);
//
//			if (!firstObjArray.equals(secondObjArray)) {
//				return false;
//			}
//		}
//
//		return true;
	}

	// private static List<ObjectiveArray> nonDominatedList(
	// List<ObjectiveArray> list) {
	// // the copy list to iterate.
	// List<ObjectiveArray> copyList = new Vector<ObjectiveArray>();
	// copyList.addAll(list);
	//
	// int j = 0;
	// for (int i = 0; i < copyList.size(); i++) {
	// if (i != j) {
	// if (copyList.get(j).dominates(copyList.get(i))) {
	// list.remove(copyList.get(i));
	// continue;
	// } else if (copyList.get(i).dominates(copyList.get(j))) {
	// list.remove(copyList.get(j));
	// j++;
	// }
	// }
	// }
	//
	// return list;
	// }

	/**
	 * Assume that these lists given as parameters, holds non-dominated
	 * objective arrays, so compare them with each other and extract the
	 * non-dominated -real- list of objective arrays.
	 * 
	 * @param list1
	 *            the first list.
	 * @param list2
	 *            the second list.
	 * @return list of non-dominated objectives array.
	 */
	public static List<ObjectiveArray> nonDominatedList(
			List<ObjectiveArray> list1, List<ObjectiveArray> list2) {

		// create two new lists not to modify references.
		List<ObjectiveArray> tmpList1 = new Vector<ObjectiveArray>();
		List<ObjectiveArray> tmpList2 = new Vector<ObjectiveArray>();

		tmpList1.addAll(list1);
		tmpList2.addAll(list2);

		// create a non-dominated list for given lists.
		List<ObjectiveArray> nonDomList = new Vector<ObjectiveArray>();

		List<ObjectiveArray> dominatedOnA = new Vector<ObjectiveArray>();
		List<ObjectiveArray> dominatedOnB = new Vector<ObjectiveArray>();

		for (int i = 0; i < tmpList1.size(); i++) {
			for (int j = 0; j < tmpList2.size(); j++) {
				if (tmpList1.get(i).dominates(tmpList2.get(j))
						|| tmpList1.get(i).equals(tmpList2.get(j))) {
					dominatedOnB.add(tmpList2.get(j));
				}
			}
			tmpList2.removeAll(dominatedOnB);
			dominatedOnB.clear();
		}

		for (int i = 0; i < tmpList2.size(); i++) {
			for (int j = 0; j < tmpList1.size(); j++) {
				if (tmpList2.get(i).dominates(tmpList1.get(j))
						|| tmpList2.get(i).equals(tmpList1.get(j))) {
					dominatedOnA.add(tmpList1.get(j));
				}
			}
			tmpList1.removeAll(dominatedOnA);
			dominatedOnA.clear();
		}

		nonDomList.addAll(tmpList1);
		nonDomList.addAll(tmpList2);

		return nonDomList;
	}

	/**
	 * Checks that for each objective array in first list dominates each
	 * objective array in second list. If one of them can not procure this
	 * situation, that means first list can not dominate second list. In a
	 * nutshell, this method checks domination in list of objective arrays
	 * perspective.
	 * 
	 * @param firstList
	 *            the first given objective array list.
	 * @param secondList
	 *            the second given objective array list.
	 * @return <code>true</code> if first list completely dominates second list,
	 *         o/w <code>false</code>.
	 */
	public static boolean completelyDominates(List<ObjectiveArray> firstList,
			List<ObjectiveArray> secondList) {

		List<ObjectiveArray> tmpA1 = new Vector<ObjectiveArray>();
		tmpA1.addAll(firstList);
		List<ObjectiveArray> tmpA2 = new Vector<ObjectiveArray>();
		tmpA2.addAll(secondList);

		// trimEquals(tmpA1, tmpA2);

		for (ObjectiveArray firstObjArray : tmpA1) {
			for (ObjectiveArray secondObjArray : tmpA2) {
				if (!firstObjArray.dominates(secondObjArray)) {
					return false;
				}
			}
		}
		return true;
	}

	private static void trimEquals(List<ObjectiveArray> firstList,
			List<ObjectiveArray> secondList) {

		List<ObjectiveArray> equalOAs = new Vector<ObjectiveArray>();

		for (ObjectiveArray firstOA : firstList) {
			for (ObjectiveArray secondOA : secondList) {
				if (firstOA.equals(secondOA)) {
					if (!equalOAs.contains(firstOA)) {
						equalOAs.add(firstOA);
					}
				}

			}
		}

		firstList.removeAll(equalOAs);
		secondList.removeAll(equalOAs);
	}

	public static boolean completelyEquals(List<ObjectiveArray> firstList,
			List<ObjectiveArray> secondList) {
		for (ObjectiveArray firstObjArray : firstList) {
			for (ObjectiveArray secondObjArray : secondList) {
				if (!firstObjArray.equals(secondObjArray)) {
					return false;
				}
			}
		}
		return true;
	}

	public static List<ObjectiveArray> minByRisk(List<ObjectiveArray> list1,
			List<ObjectiveArray> list2) {

		if (completelyDominates(list1, list2)) {
			return list1;
		} else if (completelyDominates(list2, list1)) {
			return list2;
		} else if (equals(list1, list2)) {
			return list1;
		} else {

			double totalRisk = 0f;
			for (ObjectiveArray oa1 : list1) {
				totalRisk += oa1.getObjectives()[1].getValue();
			}
			double averageRiskForL1 = totalRisk / list1.size();

			totalRisk = 0f;
			for (ObjectiveArray oa2 : list2) {
				totalRisk += oa2.getObjectives()[1].getValue();
			}
			double averageRiskForL2 = totalRisk / list2.size();

			if (averageRiskForL1 < averageRiskForL2) {
				return list1;
			} else if (averageRiskForL1 > averageRiskForL2) {
				return list2;
			} else {
				return list1;
			}
		}
	}

	public static boolean hasAtLeastOneDomination(List<ObjectiveArray> list1,
			List<ObjectiveArray> list2) {
		for (ObjectiveArray oa1 : list1) {
			for (ObjectiveArray oa2 : list2) {
				if (oa1.dominates(oa2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int calculateDominationCount(List<ObjectiveArray> list1,
			List<ObjectiveArray> list2) {

		int dominationCount = 0;
		for (ObjectiveArray objectiveArray1 : list1) {
			for (ObjectiveArray objectiveArray2 : list2) {

				// if(objectiveArray1.get(1).getValue()<objectiveArray2.get(1).getValue())
				// {
				if (objectiveArray1.dominates(objectiveArray2)) {
					dominationCount++;
				}
			}
		}
		return dominationCount;
	}

	public static boolean aggregates(List<ObjectiveArray> list1,
			List<ObjectiveArray> list2) {

		if ((list1.size() > list2.size()) && (list1.containsAll(list2))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean completeListDomination(List<ObjectiveArray> rhsPrimes,
			List<List<ObjectiveArray>> minRHSs) {

		for (List<ObjectiveArray> list : minRHSs) {
			if(!completelyDominates(rhsPrimes, list)) {
				return false;
			}
		}
		return true;
	}

	public static boolean completeOneDomination(
			List<List<ObjectiveArray>> minRHSs, List<ObjectiveArray> rhsPrimes) {
		for (List<ObjectiveArray> list : minRHSs) {
			if(!completelyDominates(list, rhsPrimes)) {
				return false;
			}
		}
		return true;
	}

	public static boolean dominatesAll(Key refKey, List<Key> topKeys) {
		
		for (Key key : topKeys) {
			if(refKey.compareTo(key) != -1) {
				return false;
			}
		}
		return true;
	}

	public static boolean contains(List<ObjectiveArray> objArray,
			List<ObjectiveArray> objArray2) {
		return (objArray.size() > objArray2.size() && objArray.containsAll(objArray2));
	}

	public static boolean containsAnyDominatibleOA(List<ObjectiveArray> list) {
		List<ObjectiveArray> tmpList = new Vector<ObjectiveArray>();
		tmpList.addAll(list);
		
		for (ObjectiveArray oA : list) {
			for (ObjectiveArray oA2 : tmpList) {
				if(oA.equals(oA2)){
					continue;
				}
				
				if(oA.dominates(oA2)){
					return true;
				}
			}
		}
		
		return false;
	}

}
