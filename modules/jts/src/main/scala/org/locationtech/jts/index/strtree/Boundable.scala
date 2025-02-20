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
package org.locationtech.jts.index.strtree

/**
 * A spatial object in an AbstractSTRtree.
 *
 * @version 1.7
 */
trait Boundable {

  /**
   * Returns a representation of space that encloses this Boundable, preferably not much bigger than
   * this Boundable's boundary yet fast to test for intersection with the bounds of other
   * Boundables. The class of object returned depends on the subclass of AbstractSTRtree.
   *
   * return an Envelope (for STRtrees), an Interval (for SIRtrees), or other object (for other
   * subclasses of AbstractSTRtree)
   */
  def getBounds: Any
}
