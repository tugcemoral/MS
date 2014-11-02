package tr.edu.metu.ceng.ms.thesis.spea2.integration.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//import tr.edu.metu.ceng.ms.thesis.spea2.core.Solution;
//import tr.edu.metu.ceng.ms.thesis.spea2.core.SolutionSet;
import tr.edu.metu.ceng.ms.thesis.spea2.util.Configuration;

public class SPEA2SolutionSet {
	/**
	 * Stores a list of <code>solution</code> objects.
	 */
	protected final List<SPEA2Solution> solutionsList_;

	/**
	 * Maximum size of the solution set
	 */
	private int capacity_ = 0;

	/**
	 * Constructor. Creates an unbounded solution set.
	 */
	public SPEA2SolutionSet() {
		solutionsList_ = new ArrayList<SPEA2Solution>();
	} // SolutionSet

	/**
	 * Creates a empty solutionSet with a maximum capacity.
	 * 
	 * @param maximumSize
	 *            Maximum size.
	 */
	public SPEA2SolutionSet(int maximumSize) {
		solutionsList_ = new ArrayList<SPEA2Solution>();
		capacity_ = maximumSize;
	} // SolutionSet

	/**
	 * Inserts a new solution into the SolutionSet.
	 * 
	 * @param solution
	 *            The <code>Solution</code> to store
	 * @return True If the <code>Solution</code> has been inserted, false
	 *         otherwise.
	 */
	public boolean add(SPEA2Solution solution) {
		if (solutionsList_.size() == capacity_) {
			Configuration.logger_.severe("The population is full");
			Configuration.logger_.severe("Capacity is : " + capacity_);
			Configuration.logger_.severe("\t Size is: " + this.size());
			return false;
		} // if

		solutionsList_.add(solution);
		return true;
	} // add

	public boolean add(int index, SPEA2Solution solution) {
		solutionsList_.add(index, solution);
		return true;
	}
	/*
	 * public void add(Solution solution) { if (solutionsList_.size() ==
	 * capacity_) try { throw new
	 * JMException("SolutionSet.Add(): the population is full") ; } catch
	 * (JMException e) { e.printStackTrace(); } else
	 * solutionsList_.add(solution); }
	 */
	/**
	 * Returns the ith solution in the set.
	 * 
	 * @param i
	 *            Position of the solution to obtain.
	 * @return The <code>Solution</code> at the position i.
	 * @throws IndexOutOfBoundsException
	 *             Exception
	 */
	public SPEA2Solution get(int i) {
		if (i >= solutionsList_.size()) {
			throw new IndexOutOfBoundsException("Index out of Bound " + i);
		}
		return solutionsList_.get(i);
	} // get

	/**
	 * Returns the maximum capacity of the solution set
	 * 
	 * @return The maximum capacity of the solution set
	 */
	public int getMaxSize() {
		return capacity_;
	} // getMaxSize

	/**
	 * Sorts a SolutionSet using a <code>Comparator</code>.
	 * 
	 * @param comparator
	 *            <code>Comparator</code> used to sort.
	 */
	public void sort(Comparator comparator) {
		if (comparator == null) {
			Configuration.logger_.severe("No criterium for comparing exist");
			return;
		} // if
		Collections.sort(solutionsList_, comparator);
	} // sort

	/**
	 * Returns the index of the best Solution using a <code>Comparator</code>.
	 * If there are more than one occurrences, only the index of the first one
	 * is returned
	 * 
	 * @param comparator
	 *            <code>Comparator</code> used to compare solutions.
	 * @return The index of the best Solution attending to the comparator or
	 *         <code>-1<code> if the SolutionSet is empty
	 */
	int indexBest(Comparator comparator) {
		if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
			return -1;
		}

		int index = 0;
		SPEA2Solution bestKnown = solutionsList_.get(0), candidateSolution;
		int flag;
		for (int i = 1; i < solutionsList_.size(); i++) {
			candidateSolution = solutionsList_.get(i);
			flag = comparator.compare(bestKnown, candidateSolution);
			if (flag == +1) {
				index = i;
				bestKnown = candidateSolution;
			}
		}

		return index;
	} // indexBest

	/**
	 * Returns the best Solution using a <code>Comparator</code>. If there are
	 * more than one occurrences, only the first one is returned
	 * 
	 * @param comparator
	 *            <code>Comparator</code> used to compare solutions.
	 * @return The best Solution attending to the comparator or <code>null<code>
	 * if the SolutionSet is empty
	 */
	public SPEA2Solution best(Comparator comparator) {
		int indexBest = indexBest(comparator);
		if (indexBest < 0) {
			return null;
		} else {
			return solutionsList_.get(indexBest);
		}

	} // best

	/**
	 * Returns the index of the worst Solution using a <code>Comparator</code>.
	 * If there are more than one occurrences, only the index of the first one
	 * is returned
	 * 
	 * @param comparator
	 *            <code>Comparator</code> used to compare solutions.
	 * @return The index of the worst Solution attending to the comparator or
	 *         <code>-1<code> if the SolutionSet is empty
	 */
	public int indexWorst(Comparator comparator) {
		if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
			return -1;
		}

		int index = 0;
		SPEA2Solution worstKnown = solutionsList_.get(0), candidateSolution;
		int flag;
		for (int i = 1; i < solutionsList_.size(); i++) {
			candidateSolution = solutionsList_.get(i);
			flag = comparator.compare(worstKnown, candidateSolution);
			if (flag == -1) {
				index = i;
				worstKnown = candidateSolution;
			}
		}

		return index;

	} // indexWorst

	/**
	 * Returns the worst Solution using a <code>Comparator</code>. If there are
	 * more than one occurrences, only the first one is returned
	 * 
	 * @param comparator
	 *            <code>Comparator</code> used to compare solutions.
	 * @return The worst Solution attending to the comparator or
	 *         <code>null<code>
	 * if the SolutionSet is empty
	 */
	public SPEA2Solution worst(Comparator comparator) {

		int index = indexWorst(comparator);
		if (index < 0) {
			return null;
		} else {
			return solutionsList_.get(index);
		}

	} // worst

	/**
	 * Returns the number of solutions in the SolutionSet.
	 * 
	 * @return The size of the SolutionSet.
	 */
	public int size() {
		return solutionsList_.size();
	} // size

	/**
	 * Writes the objective function values of the <code>Solution</code> objects
	 * into the set in a file.
	 * 
	 * @param path
	 *            The output file name
	 */
	public void printObjectivesToFile(String path) {
		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for (SPEA2Solution aSolutionsList_ : solutionsList_) {
				// if (this.vector[i].getFitness()<1.0) {
				bw.write(aSolutionsList_.toString());
				bw.newLine();
				// }
			}

			/* Close the file */
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printObjectivesToFile

	/**
	 * Empties the SolutionSet
	 */
	public void clear() {
		solutionsList_.clear();
	} // clear

	/**
	 * Deletes the <code>Solution</code> at position i in the set.
	 * 
	 * @param i
	 *            The position of the solution to remove.
	 */
	public void remove(int i) {
		if (i > solutionsList_.size() - 1) {
			Configuration.logger_.severe("Size is: " + this.size());
		} // if
		solutionsList_.remove(i);
	} // remove

	/**
	 * Returns an <code>Iterator</code> to access to the solution set list.
	 * 
	 * @return the <code>Iterator</code>.
	 */
	public Iterator<SPEA2Solution> iterator() {
		return solutionsList_.iterator();
	} // iterator

	/**
	 * Returns a new <code>SolutionSet</code> which is the result of the union
	 * between the current solution set and the one passed as a parameter.
	 * 
	 * @param solutionSet
	 *            SolutionSet to join with the current solutionSet.
	 * @return The result of the union operation.
	 */
	public SPEA2SolutionSet union(SPEA2SolutionSet solutionSet) {
		// Check the correct size. In development
		int newSize = this.size() + solutionSet.size();
		if (newSize < capacity_)
			newSize = capacity_;

		// Create a new population
		SPEA2SolutionSet union = new SPEA2SolutionSet(newSize);
		for (int i = 0; i < this.size(); i++) {
			union.add(this.get(i));
		} // for

		for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
			union.add(solutionSet.get(i - this.size()));
		} // for

		return union;
	} // union

	/**
	 * Replaces a solution by a new one
	 * 
	 * @param position
	 *            The position of the solution to replace
	 * @param solution
	 *            The new solution
	 */
	public void replace(int position, SPEA2Solution solution) {
		if (position > this.solutionsList_.size()) {
			solutionsList_.add(solution);
		} // if
		solutionsList_.remove(position);
		solutionsList_.add(position, solution);
	} // replace

	public void printObjectives() {
		for (int i = 0; i < solutionsList_.size(); i++)
			System.out.println("" + solutionsList_.get(i));
	}

	public void setCapacity(int capacity) {
		capacity_ = capacity;
	}

	public int getCapacity() {
		return capacity_;
	}
	
	public List<SPEA2Solution> getSolutions(){
		return solutionsList_;
	}
}
