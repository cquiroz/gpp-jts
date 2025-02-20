// Copyright (c) 2016-2023 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

/*
 * Copyright (c) 2016 Vivid Solutions.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package org.locationtech.jts.noding

import org.locationtech.jts.algorithm.LineIntersector
import org.locationtech.jts.geom.Coordinate

import java.util

/**
 * Finds <b>interior</b> intersections between line segments in {link NodedSegmentString}s, and adds
 * them as nodes using {link NodedSegmentString#addIntersection(LineIntersector, int, int, int)}.
 * <p> This class is used primarily for Snap-Rounding. For general-purpose noding, use {link
 * IntersectionAdder}.
 *
 * @version 1.7
 * @see
 *   IntersectionAdder
 */
class InteriorIntersectionFinderAdder(var li: LineIntersector)

/**
 * Creates an intersection finder which finds all proper intersections
 *
 * @param li
 *   the LineIntersector to use
 */
    extends SegmentIntersector {
  final private val interiorIntersections = new util.ArrayList[Coordinate]

  def getInteriorIntersections: util.ArrayList[Coordinate] = interiorIntersections

  /**
   * This method is called by clients of the {link SegmentIntersector} class to process
   * intersections for two segments of the {link SegmentString}s being intersected. Note that some
   * clients (such as <code>MonotoneChain</code>s) may optimize away this call for segment pairs
   * which they have determined do not intersect (e.g. by an disjoint envelope test).
   */
  override def processIntersections(
    e0:        SegmentString,
    segIndex0: Int,
    e1:        SegmentString,
    segIndex1: Int
  ): Unit = { // don't bother intersecting a segment with itself
    if ((e0 eq e1) && segIndex0 == segIndex1) return
    val p00 = e0.getCoordinates(segIndex0)
    val p01 = e0.getCoordinates(segIndex0 + 1)
    val p10 = e1.getCoordinates(segIndex1)
    val p11 = e1.getCoordinates(segIndex1 + 1)
    li.computeIntersection(p00, p01, p10, p11)
    // if (li.hasIntersection() && li.isProper()) Debug.println(li);
    if (li.hasIntersection) if (li.isInteriorIntersection) {
      var intIndex = 0
      while (intIndex < li.getIntersectionNum) {
        interiorIntersections.add(li.getIntersection(intIndex))
        intIndex += 1
      }
      e0.asInstanceOf[NodedSegmentString].addIntersections(li, segIndex0, 0)
      e1.asInstanceOf[NodedSegmentString].addIntersections(li, segIndex1, 1)
    }
  }

  /**
   * Always process all intersections
   *
   * return false always
   */
  override def isDone = false
}
