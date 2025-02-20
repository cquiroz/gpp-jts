/*
 * Copyright (c) 2016-2023 Association of Universities for Research in Astronomy, Inc. (AURA)
 * For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause
 */

package org.locationtech.jts.io;

import java.io.IOException;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import junit.framework.TestCase;
import junit.textui.TestRunner;



/**
 * Tests the {@link WKTReader} with exponential notation.
 */
public class WKTReaderExpTest
    extends TestCase
{
  public static void main(String args[]) {
    TestRunner.run(WKTReaderExpTest.class);
  }

  private GeometryFactory fact = new GeometryFactory();
  private WKTReader rdr = new WKTReader(fact);

  public WKTReaderExpTest(String name)
  {
    super(name);
  }

  public void testGoodBasicExp() throws IOException, ParseException
  {
    readGoodCheckCoordinate("POINT ( 1e01 -1E02)", 1E01, -1E02);
  }

  public void testGoodWithExpSign() throws IOException, ParseException
  {
    readGoodCheckCoordinate("POINT ( 1e-04 1E-05)", 1e-04, 1e-05);
  }

  public void testBadExpFormat() throws IOException, ParseException
  {
    readBad("POINT (1e0a1 1X02)");
  }

  public void testBadExpPlusSign() throws IOException, ParseException
  {
    readBad("POINT (1e+01 1X02)");
  }

  public void testBadPlusSign() throws IOException, ParseException
  {
    readBad("POINT ( +1e+01 1X02)");
  }

  private void readGoodCheckCoordinate(String wkt, double x, double y)
      throws IOException, ParseException
  {
    Geometry g = rdr.read(wkt);
    Coordinate pt = g.getCoordinate();
    assertEquals(pt.x(), x, 0.0001);
    assertEquals(pt.y(), y, 0.0001);
  }

  private void readBad(String wkt)
      throws IOException
  {
    boolean threwParseEx = false;
    try {
      Geometry g = rdr.read(wkt);
    }
    catch (ParseException ex) {
      //System.out.println(ex.getMessage());
      threwParseEx = true;
    }
    assertTrue(threwParseEx);
  }
}

