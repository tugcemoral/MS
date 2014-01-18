/*
 *  The MIT License
 * 
 *  Copyright 2010 Prasanna Velagapudi <psigen@gmail.com>.
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package tr.edu.metu.ceng.ms.thesis.robotutils.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;

/**
 * A simple tuple class that correctly represents an integer coordinate in
 * arbitrary dimensions. Equality, hashcode and comparisons are all implemented
 * as a lexical ordering over the integer array elements.
 */
public class IntCoord implements Coordinate {

	protected final int[] _coords;
	
	private int insertionCount;
	
	private Map<Coordinate, List<ObjectiveArray>> parents;
	
	private boolean expanded = false;
	
	private int expansion = 0;
	
	private double risk;
	
	public IntCoord(int... values) {
		_coords = Arrays.copyOf(values, values.length);
		insertionCount = 0;
	}

	public int[] getInts() {
		return _coords;
	}

	public double[] get() {
		double[] d = new double[_coords.length];

		for (int i = 0; i < d.length; i++) {
			d[i] = (double) _coords[i];
		}

		return d;
	}

	public double get(int dim) {
		return (double) _coords[dim];
	}

	public int dims() {
		return _coords.length;
	}

	@Override
	public String toString() {
		return Arrays.toString(_coords);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntCoord other = (IntCoord) obj;
		if (!Arrays.equals(_coords, other._coords))
			return false;
		if (insertionCount != other.insertionCount)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(_coords);
		result = prime * result + insertionCount;
		return result;
	}

	@Override
	public int getInsertionCount() {
		return insertionCount;
	}

	@Override
	public void inserted() {
		insertionCount++;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	@Override
	public Map<Coordinate, List<ObjectiveArray>> getParents() {
		if (parents == null) {
			parents = new HashMap<Coordinate, List<ObjectiveArray>>();
		}
		return parents;
	}

	@Override
	public void incrExpansion() {
		this.expansion++;
	}

	@Override
	public int getExpansion() {
		return expansion;
	}

//	@Override
//	public double getRisk() {
//		return risk;
//	}
//
//	@Override
//	public void setRisk(double risk) {
//		this.risk = risk;
//	}

}
