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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components.MOGridMap;

/**
 * Helper class that converts GridMaps into a variety of useful formats.
 * 
 * @author Prasanna Velagapudi <pkv@cs.cmu.edu>
 */
public class GridMapUtils {

	public static Image toImage(MOGridMap map) {
		if (map.dims() != 2)
			throw new IllegalArgumentException("Cannot display " + map.dims()
					+ "-D map as image.");
		BufferedImage image = new BufferedImage(map.size(0), map.size(1),
				BufferedImage.TYPE_BYTE_GRAY);
		fillImage(image, map);
		return image;
	}

	private static void fillImage(BufferedImage image, MOGridMap map) {
		int height = Math.min(image.getHeight(), map.size(0));
		int width = Math.min(image.getWidth(), map.size(1));

		fillImage(image, map, 0, 0, width, height);
	}

	private static void fillImage(BufferedImage image, MOGridMap map, int x,
			int y, int width, int height) {
		WritableRaster wr = image.getRaster();
		for (int i = y; i < height; i++) {
			for (int j = x; j < width; j++) {
//				if (map.isInViewingFrustumArea(i, j)) {
//					wr.setSample(i, j, 0, (map.hasObstacle(i, j) ? -1d : 0d));
					wr.setSample(i, j, 0, (map.hasObstacle(i, j) ? -1d : 0d));
//				} else {
//					wr.setSample(i, j, 0, (map.hasObstacle(i, j) ? 0d : 0d));
//				}
			}
		}
	}

}
