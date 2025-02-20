/*
 * Copyright (c) 2016-2023 Association of Universities for Research in Astronomy, Inc. (AURA)
 * For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause
 */

package org.locationtech.jts.io;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import junit.framework.TestCase;

public class WKBWriterTest extends TestCase {

    public WKBWriterTest(String name) {
        super(name);
    }

    public void testSRID() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point p1 = gf.createPoint(new Coordinate(1,2));
        p1.setSRID(1234);

        //first write out without srid set
        WKBWriter w = new WKBWriter();
        byte[] wkb = w.write(p1);

        //check the 3rd bit of the second byte, should be unset
        byte b = (byte) (wkb[1] & 0x20);
        assertEquals(0, b);

        //read geometry back in
        WKBReader r = new WKBReader(gf);
        Point p2 = (Point) r.read(wkb);

        assertTrue(p1.equalsExact(p2));
        assertEquals(0, p2.getSRID());

        //not write out with srid set
        w = new WKBWriter(2, true);
        wkb = w.write(p1);

        //check the 3rd bit of the second byte, should be set
        b = (byte) (wkb[1] & 0x20);
        assertEquals(0x20, b);

        int srid = ((wkb[5] & 0xff) << 24) | ((wkb[6] & 0xff) << 16)
            | ((wkb[7] & 0xff) << 8) | ((wkb[8] & 0xff) );

        assertEquals(1234, srid);

        r = new WKBReader(gf);
        p2 = (Point) r.read(wkb);

        //read the geometry back in
        assertTrue(p1.equalsExact(p2));
        assertEquals(1234, p2.getSRID());
    }
}
