/*
 *  Copyright (c) 2009, Prasanna Velagapudi <pkv@cs.cmu.edu>
 *  All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the project nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ''AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE PROJECT AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.ThreatUtils.generateNumOfThreats;
import static tr.edu.metu.ceng.ms.thesis.modstarlite.util.ThreatUtils.generateThreatSize;


import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ExecutionType;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.MOState;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Obstacle;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Threat;
import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOStaticMap;
import tr.edu.metu.ceng.ms.thesis.mogpp.core.env.MOGeneticMap;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.util.CoordUtils;
import tr.edu.metu.ceng.ms.thesis.spea2.integration.env.MOSPEA2GeneticMap;

/**
 * Contains generation functions for simple 2D grid maps.
 */
public class GridMapGenerator {

	// /**
	// * Creates a random multi-objective maze map
	// *
	// * @param width
	// * @param height
	// * @param objectiveCount
	// * @param objBehaviours
	// * @return
	// */
	// public static MOStaticMap createRandomMOMazeMap2D(int width, int height,
	// ObjectiveBehaviour[] objBehaviours) {
	// // create a new multi-objective "static" map...
	// MOStaticMap map = new MOStaticMap(width, height, objBehaviours);
	// // map.resize(width, height);
	//
	// for (int i = 0; i < width; i++) {
	// for (int j = 0; j < height; j++) {
	// if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
	// // Map borders are untraversable
	// map.set(MOState.getInfiniteState(objBehaviours), i, j);
	// } else if ((i == 1 && j == 1)
	// || (i == width - 2 && j == height - 2)) {
	// // the static start and goal locations as free states.
	// ObjectiveArray objectives = createRandomizedObjectives(
	// map.getObjectiveBehaviours(), i, j, width, height);
	// // create a multi-objective state as free cell.
	// map.set(new MOState(objectives, false), i, j);
	// } else {
	// boolean a = (i < 1) ? false : map.get(i - 1, j)
	// .hasObstacle(); // left
	// boolean b = (j < 1) ? false : map.get(i, j - 1)
	// .hasObstacle(); // up
	// boolean c = ((i < 1) || (j < 1)) ? false : map.get(i - 1,
	// j - 1).hasObstacle(); // up-left
	//
	// double prob;
	// if (a) {
	// if (b) {
	// // all sides blocked - left and up but no corner
	// prob = c ? 0.6 : 0.4;
	// } else {
	// // left and corner - just left
	// prob = c ? 0.3 : 0.2;
	// }
	// } else {
	// if (b) {
	// // up and corner - just up
	// prob = c ? 0.3 : 0.2;
	// } else {
	// // just corner - nothing
	// prob = c ? 0.0 : 0.1;
	// }
	// }
	//
	// // double weightVal = Math.random() * 255;
	//
	// // create an objectives array.
	// ObjectiveArray objectives = createRandomizedObjectives(
	// map.getObjectiveBehaviours(), i, j, width, height);
	// if (Math.random() < prob) {
	// // create a multi-objective state as obstacle.
	// map.set(new MOState(objectives, true), i, j);
	// } else {
	// // create a multi-objective state as non-obstacle.
	// map.set(new MOState(objectives, false), i, j);
	// }
	// }
	// }
	// }
	//
	// return map;
	// }

	public GridMapGenerator(String mapFileName) {
		PropertiesReader.initializeProperties(mapFileName);
	}

	private static ObjectiveArray createRandomizedObjectives(
			ObjectiveBehaviour[] objectiveBehaviours, int i, int j, int width,
			int height) {
		// the known static goal coordinates.
		Coordinate goalCoords = new IntCoord(width - 2, height - 2);

		// create a new objective array.
		Objective[] objectives = new Objective[objectiveBehaviours.length];

		for (int k = 0; k < objectiveBehaviours.length; k++) {
			// first objective is manhattan distance to goal for each state.
			if (k == 0) {
				// create coordinate instance from current i and j.
				Coordinate currentCoords = new IntCoord(i, j);
				double mdist = CoordUtils.mdist(currentCoords, goalCoords);
				objectives[k] = new Objective(mdist, objectiveBehaviours[k]);
			}
			// //second objective is the total risk on this state.
			// else if (k == 1) {
			//
			// }
			else {
				// TODO: this 10 must be parameterized.
				// generate the value of this obstacle between 0 and 10
				int value = (int) (Math.random() * 10);

				// int value = 0;
				objectives[k] = new Objective(value, objectiveBehaviours[k]);
			}
		}

		return new ObjectiveArray(objectives);
	}

	public MOStaticMap create2DMOMazeMapWithThreatAndObstaclesUsingPredefinedParams() {

		// get context-properties from corresponding file.
		int width = PropertiesReader.getWidth();
		int height = PropertiesReader.getHeight();

		// initialize the 2D map.
		MOStaticMap map = initializeMOStaticMap(width, height);

		// locate all threats.
		for (Threat threat : PropertiesReader.getThreats()) {
			map.locateThreat(threat);
		}

		// locate all obstacles.
		for (Obstacle obstacle : PropertiesReader.getObstacles()) {
			map.locateObstacle(obstacle);
		}

		// calculate and set all objectives.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					continue;
				} else {
					// create an objectives array.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setObjectives(objectives);
					// map.get(i,j).getCoords().setRisk(map.get(i,j).getTotalRisk());
				}
			}
		}

		return map;
	}

	public MOStaticMap create2DMOMazeMapWithObstaclesUsingPredefinedParams() {

		// get context-properties from corresponding file.
		int width = PropertiesReader.getWidth();
		int height = PropertiesReader.getHeight();

		// initialize the 2D map.
		MOStaticMap map = initializeMOStaticMap(width, height);

		// locate all obstacles.
		for (Obstacle obstacle : PropertiesReader.getObstacles()) {
			map.locateObstacle(obstacle);
		}

		// calculate and set all objectives.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					continue;
				} else {
					// create an objectives array.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setObjectives(objectives);
				}
			}
		}

		return map;

	}

	/**
	 * This method creates a 2-dimentional multi-objective maze with random
	 * threats (with threat zones) and obstacles.
	 * 
	 * @param width
	 *            width of the map
	 * @param height
	 *            height of the map, generally equals with @param width
	 * @param viewingFrustumArea
	 * @param objBehaviours
	 *            the objective behaviours of this map, generally [
	 *            {@link ObjectiveBehaviour#MINIMIZED},
	 *            {@link ObjectiveBehaviour#MINIMIZED}]
	 * 
	 * @return created multi-objective 2D maze.
	 */
	private MOStaticMap createRandom2DMOMazeMapWithThreatsAndObstacles() {

		// get context-properties from corresponding file.
		int width = PropertiesReader.getWidth();
		int height = PropertiesReader.getHeight();

		// initialize the 2D map.
		MOStaticMap map = initializeMOStaticMap(width, height);

		// firstly, generate threats with their zones.
		int numOfThreats = generateNumOfThreats(width, height);

		// generate all threats.
		for (int k = 0; k < numOfThreats; k++) {
			// generate the actual threat size.
			int threatSize = generateThreatSize(width, height);
			// generate the start location of actual threat.
			int threatStartX = (int) ((Math.random() * ((width - 2) - threatSize)) + 1);
			int threatStartY = (int) ((Math.random() * ((height - 2) - threatSize)) + 1);
			// generate difference between threat zones.
			int differenceBetweenZones = (int) (Math.random() * threatSize);
			// generate the actual theat.
			Threat threat = new Threat(
					new IntCoord(threatStartX, threatStartY), threatSize,
					differenceBetweenZones);
			// locate this threat into map.
			map.locateThreat(threat);
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					continue;
				} else if ((i == 1 && j == 1)
						|| (i == width - 2 && j == height - 2)) {

					// the static start and goal locations as free states.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setHasObstacle(false);
					map.get(i, j).setObjectives(objectives);
				} else {
					boolean a = (i < 1) ? false : map.get(i - 1, j)
							.hasObstacle(); // left
					boolean b = (j < 1) ? false : map.get(i, j - 1)
							.hasObstacle(); // up
					boolean c = ((i < 1) || (j < 1)) ? false : map.get(i - 1,
							j - 1).hasObstacle(); // up-left

					double prob;
					if (a) {
						if (b) {
							// all sides blocked - left and up but no corner
							prob = c ? 0.6 : 0.4;
						} else {
							// left and corner - just left
							prob = c ? 0.3 : 0.2;
						}
					} else {
						if (b) {
							// up and corner - just up
							prob = c ? 0.3 : 0.2;
						} else {
							// just corner - nothing
							prob = c ? 0.0 : 0.1;
						}
					}

					// double weightVal = Math.random() * 255;

					// create an objectives array.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setObjectives(objectives);
					if ((Math.random() < prob) && !map.get(i, j).isInThreat()) {
						// update a multi-objective state as obstacle.
						map.locateObstacle(new Obstacle(i, j));
						// map.get(i, j).setHasObstacle(true);
					} else {
						// update a multi-objective state as non-obstacle.
						map.get(i, j).setHasObstacle(false);
					}
				}
			}
		}

		return map;
	}

	private ObjectiveArray createObjectives(MOStaticMap map, int i, int j,
			int width, int height) {
		// the known static goal coordinates.
		Coordinate goalCoords = new IntCoord(width - 2, height - 2);

		// create a new objective array.
		Objective[] objectives = new Objective[map.getObjectiveBehaviours().length];

		for (int k = 0; k < map.getObjectiveBehaviours().length; k++) {
			// first objective is manhattan distance to goal for each state.
			if (k == 0) {
				// create coordinate instance from current i and j.
				Coordinate currentCoords = new IntCoord(i, j);
				double mdist = CoordUtils.mdist(currentCoords, goalCoords);
				objectives[k] = new Objective(mdist,
						map.getObjectiveBehaviours()[k]);
			} else {
				// //second objective is the total risk on this state.
				objectives[k] = new Objective(map.get(i, j).getTotalRisk(),
						map.getObjectiveBehaviours()[k]);
				// objectives[k] = new Objective(0,
				// map.getObjectiveBehaviours()[k]);
			}
		}

		return new ObjectiveArray(objectives);
	}

	private MOStaticMap initializeMOStaticMap(int width, int height) {

		int[] start = PropertiesReader.getStart();
		int[] goal = PropertiesReader.getGoal();

		if (start[0] == -1 && start[1] == -1) {
			start[0] = (int) Math.round(Math.random() * width);
			start[1] = (int) Math.round(Math.random() * height);
		}

		if (goal[0] == -1 && goal[1] == -1) {
			goal[0] = (int) Math.round(Math.random() * width);
			goal[1] = (int) Math.round(Math.random() * height);
		}

		int[] viewingFrustumArea = PropertiesReader.getViewingFrustumBorders();
		ObjectiveBehaviour[] objBehaviours = PropertiesReader
				.getObjBehaviours();
		ExecutionType execType = PropertiesReader.getExecutionType();

		// create a new 2D static map instance.
		MOStaticMap map = new MOStaticMap(width, height, start, goal,
				viewingFrustumArea, execType, objBehaviours);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				MOState stateToAdd = null;
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					stateToAdd = MOState.getInfiniteState(objBehaviours);
					// Map borders are untraversable
					map.set(stateToAdd, i, j);
				} else {
					stateToAdd = new MOState(i, j);
					map.set(stateToAdd, i, j);
					map.setInViewingFrustum(stateToAdd);
				}
			}
		}

		// set initial tmp goal state.
		map.updateTmpGoal();

		return map;
	}

	public MOStaticMap generateMap() {
		// Generate a (random | parameterized) multi-objective blocky map.
		return (PropertiesReader.isRandomGeneration() ? createRandom2DMOMazeMapWithThreatsAndObstacles()
				: create2DMOMazeMapWithThreatAndObstaclesUsingPredefinedParams());
	}

	public MOGeneticMap generateGeneticMap() {
		// get context-properties from corresponding file.
		int width = PropertiesReader.getWidth();
		int height = PropertiesReader.getHeight();

		// initialize the 2D map.
		MOGeneticMap map = initializeMOGeneticMap(width, height);

		// locate all threats.
		for (Threat threat : PropertiesReader.getThreats()) {
			map.locateThreat(threat);
		}

		// locate all obstacles.
		for (Obstacle obstacle : PropertiesReader.getObstacles()) {
			map.locateObstacle(obstacle);
		}

		// calculate and set all objectives.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					continue;
				} else {
					// create an objectives array.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setObjectives(objectives);
					// map.get(i,j).getCoords().setRisk(map.get(i,j).getTotalRisk());
				}
			}
		}

		return map;
	}

	private MOGeneticMap initializeMOGeneticMap(int width, int height) {

		int[] start = PropertiesReader.getStart();
		int[] goal = PropertiesReader.getGoal();

		if (start[0] == -1 && start[1] == -1) {
			start[0] = (int) Math.round(Math.random() * width);
			start[1] = (int) Math.round(Math.random() * height);
		}

		if (goal[0] == -1 && goal[1] == -1) {
			goal[0] = (int) Math.round(Math.random() * width);
			goal[1] = (int) Math.round(Math.random() * height);
		}

		ObjectiveBehaviour[] objBehaviours = PropertiesReader
				.getObjBehaviours();
		
		// create a new 2D genetic map instance.
		MOGeneticMap map = MOGeneticMap.getInstance();
		map.setStart(start);
		map.setGoal(goal);
		map.setCurrentAgentLocation(start);
		map.setViewingFrustumBorders(PropertiesReader.getViewingFrustumBorders());
		map.setExecutionType(PropertiesReader.getExecutionType());
		map.setObjectiveBehaviours(objBehaviours);
		map.resize(width, height);
		
		//set genetic map properties.
		map.setMaxIteration(PropertiesReader.getMaxIteration());
		map.setElitism_k(PropertiesReader.getElitists());
		map.setPopulationSize(PropertiesReader.getPopulationSize());
		map.setCrossoverRate(PropertiesReader.getCrossoverRate());
		map.setMutationRate(PropertiesReader.getMutationRate());

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				MOState stateToAdd = null;
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					stateToAdd = MOState.getInfiniteState(objBehaviours);
					// Map borders are untraversable
					map.set(stateToAdd, i, j);
				} else {
					stateToAdd = new MOState(i, j);
					map.set(stateToAdd, i, j);
					map.setInViewingFrustum(stateToAdd);
				}
			}
		}

		// set initial tmp goal state.
		map.updateTmpGoal();

		return map;
	}

	public MOSPEA2GeneticMap generateSPEA2Map() {
		
		
		// get context-properties from corresponding file.
		int width = PropertiesReader.getWidth();
		int height = PropertiesReader.getHeight();

		// initialize the 2D map.
		MOSPEA2GeneticMap map = initializeMOSPEA2GeneticMap(width, height);

		// locate all threats.
		for (Threat threat : PropertiesReader.getThreats()) {
			map.locateThreat(threat);
		}

		// locate all obstacles.
		for (Obstacle obstacle : PropertiesReader.getObstacles()) {
			map.locateObstacle(obstacle);
		}

		// calculate and set all objectives.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					continue;
				} else {
					// create an objectives array.
					ObjectiveArray objectives = createObjectives(map, i, j,
							width, height);
					map.get(i, j).setObjectives(objectives);
					// map.get(i,j).getCoords().setRisk(map.get(i,j).getTotalRisk());
				}
			}
		}

		return map;
	}

	private MOSPEA2GeneticMap initializeMOSPEA2GeneticMap(int width, int height) {
		
		int[] start = PropertiesReader.getStart();
		int[] goal = PropertiesReader.getGoal();

		if (start[0] == -1 && start[1] == -1) {
			start[0] = (int) Math.round(Math.random() * width);
			start[1] = (int) Math.round(Math.random() * height);
		}

		if (goal[0] == -1 && goal[1] == -1) {
			goal[0] = (int) Math.round(Math.random() * width);
			goal[1] = (int) Math.round(Math.random() * height);
		}

		ObjectiveBehaviour[] objBehaviours = PropertiesReader
				.getObjBehaviours();
		
		// create a new 2D genetic map instance.
		MOSPEA2GeneticMap map = MOSPEA2GeneticMap.getInstance();
		map.setStart(start);
		map.setGoal(goal);
		map.setCurrentAgentLocation(start);
		map.setViewingFrustumBorders(PropertiesReader.getViewingFrustumBorders());
		map.setExecutionType(PropertiesReader.getExecutionType());
		map.setObjectiveBehaviours(objBehaviours);
		map.resize(width, height);
		
		//set genetic map properties.
		map.setMaxIteration(50); // 20
		map.setArchiveSize(50); // 50
		map.setPopulationSize(50); // 50
		map.setCrossoverRate(0.8); // 0.8
		map.setCrossoverDistIndex(20.0);
		map.setMutationRate(0.05);
		map.setMutationDistIndex(20.0);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				MOState stateToAdd = null;
				if (i == 0 || i == width - 1 || j == 0 || j == height - 1) {
					stateToAdd = MOState.getInfiniteState(objBehaviours);
					// Map borders are untraversable
					map.set(stateToAdd, i, j);
				} else {
					stateToAdd = new MOState(i, j);
					map.set(stateToAdd, i, j);
					map.setInViewingFrustum(stateToAdd);
				}
			}
		}

		// set initial tmp goal state.
		map.updateTmpGoal();

		return map;
	}

}
