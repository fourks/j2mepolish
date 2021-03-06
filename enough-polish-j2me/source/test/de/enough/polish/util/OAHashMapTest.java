/*
 * Created on 01-Dec-2005 at 00:04:40.
 * 
 * Copyright (c) 2005 Robert Virkus / Enough Software
 *
 * This file is part of J2ME Polish.
 *
 * J2ME Polish is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * J2ME Polish is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with J2ME Polish; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Commercial licenses are also available, please
 * refer to the accompanying LICENSE.txt or visit
 * http://www.j2mepolish.org for details.
 */
package de.enough.polish.util;


import java.util.Collection;
import java.util.Hashtable;
import java.util.Random;

import junit.framework.TestCase;

public class OAHashMapTest extends TestCase {
	
	private static final int TEST_RUNS = 50000;
	private final Integer[] integerKeys;
	private final String[] stringKeys;
	private final Integer[] integerKeysMixed;
	private final String[] stringKeysMixed;

	public OAHashMapTest(String name) {
		super(name);
		this.integerKeys = new Integer[ TEST_RUNS ];
		this.stringKeys = new String[ TEST_RUNS ];
		Random random = new Random( System.currentTimeMillis() );
		java.util.HashMap map = new java.util.HashMap( TEST_RUNS );
		int i = 0;
		do {
			Integer key = new Integer( random.nextInt() );
			// make sure that each key is unique:
			if (!map.containsKey(key)) {
				this.integerKeys[i] = key;
				this.stringKeys[i] = key.toString();
				 i++;
				 map.put( key, key );
			}
		} while (i < TEST_RUNS);
		// mix integer keyus
		this.integerKeysMixed = new Integer[ TEST_RUNS ];
		this.stringKeysMixed = new String[ TEST_RUNS ];
		i = 0;
		do {
			int position = random.nextInt( TEST_RUNS );
			Integer key = this.integerKeys[ position ]; 
			if (map.containsKey(key)) {
				this.integerKeysMixed[i] = key;
				this.stringKeysMixed[i] = this.stringKeys[position];
				 i++;
				 map.remove( key );
			}
		} while (i < TEST_RUNS);
	}
	
	public void testPut() {
		System.out.println(">>> put()");
		OAHashMap map = new OAHashMap();
		System.gc();
		long time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			Object previous = map.put( key, value );
			assertNull( previous );
			assertEquals( i + 1, map.size() );
		}
		long neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for putting " + TEST_RUNS + " values into de.enough.polish.util.OAHashMap.");
		
		java.util.HashMap j2semap = new java.util.HashMap(); 
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			Object previous = j2semap.put( key, value );
			assertNull( previous );
			assertEquals( i + 1, j2semap.size() );
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for putting " + TEST_RUNS + " values into java.util.HashMap.");
		
		Hashtable table = new Hashtable(); 
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			Object previous = table.put( key, value );
			assertNull( previous );
			assertEquals( i + 1, table.size() );
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for putting " + TEST_RUNS + " values into java.util.Hashtable.");

	}
	
	
	public void testRemove() {
		System.out.println(">>> remove()");
		OAHashMap map = new OAHashMap();
		try {
			map.remove( "Hello" );
			fail("map.remove() should not be implemeted");
		} catch (Exception e) {
			// expected
		}
	}
	
	public void testPutWithSameKeys() {
		OAHashMap map = new OAHashMap();
		String key = "key";
		Object[] values = new Object[]{"one", "two", new Integer(3), "four"};
		
		Object previous = map.put( key, values[0]);
		assertNull( previous );
		
		previous = map.put( key, values[1]);
		assertEquals( values[0], previous );
		assertEquals( 1, map.size() );
		
		previous = map.put( key, values[2]);
		assertEquals( values[1], previous );
		assertEquals( 1, map.size() );

		previous = map.put( key, values[3]);
		assertEquals( values[2], previous );
		assertEquals( 1, map.size() );
		
		assertEquals( values[3], map.get( key ));
		assertEquals( 1, map.size() );
	}
	
	public void testGet() {
		System.out.println(">>> get()");
		OAHashMap map = new OAHashMap();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			map.put( key, value );
			assertEquals( value, map.get( key ));
		}
		System.gc();
		long time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			Object value = this.stringKeysMixed[i];			
			assertEquals( value, map.get( key ));
		}
		
		long neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for getting " + TEST_RUNS + " values in de.enough.polish.util.OAHashMap.");
		
		java.util.HashMap j2semap = new java.util.HashMap(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			j2semap.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			Object value = this.stringKeysMixed[i];			
			assertEquals( value, j2semap.get( key ));
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for getting " + TEST_RUNS + " values in java.util.HashMap.");
		
		Hashtable table = new Hashtable(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			table.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			Object value = this.stringKeysMixed[i];			
			assertEquals( value, table.get( key ));
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for getting " + TEST_RUNS + " values in java.util.Hashtable.");
	}
	
	public void testContainsKey() {
		System.out.println(">>> containsKey()");
		OAHashMap map = new OAHashMap();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			map.put( key, value );
		}
		System.gc();
		long time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			assertTrue( map.containsKey( key ));
		}		
		long neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS + " keys in de.enough.polish.util.OAHashMap.");
		
		java.util.HashMap j2semap = new java.util.HashMap(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			j2semap.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			assertTrue( j2semap.containsKey( key ));
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS + " keys in java.util.HashMap.");
		
		Hashtable table = new Hashtable(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			table.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeysMixed[i];
			assertTrue( table.containsKey( key ));
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS + " keys in java.util.Hashtable.");
	}
	
	public void testContainsValue() {
		System.out.println(">>> containsValue()");
		OAHashMap map = new OAHashMap();
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			map.put( key, value );
		}
		System.gc();
		long time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS/10; i++) {
			Object value = this.stringKeysMixed[i];			
			assertTrue( map.containsValue( value ));
		}		
		long neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS/10 + " values in de.enough.polish.util.OAHashMap.");
		
		java.util.HashMap j2semap = new java.util.HashMap(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			j2semap.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS/10; i++) {
			Object value = this.stringKeysMixed[i];			
			assertTrue( j2semap.containsValue( value ));
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS/10 + " values in java.util.HashMap.");
		
		Hashtable table = new Hashtable(); 
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			table.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < TEST_RUNS/10; i++) {
			Object value = this.stringKeysMixed[i];			
			assertTrue( table.get( value ) != null);
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for checking " + TEST_RUNS/10 + " keys/values in java.util.Hashtable.");
	}

	public void testValues() {
		System.out.println(">>> values()");
		OAHashMap map = new OAHashMap();
		Object[] values = map.values();
		assertEquals( 0, values.length );
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			map.put( key, value );
		}
		System.gc();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 3; i++ ) {
			values = map.values();
			assertNotNull( values );
			assertEquals( map.size(), values.length );
		}
		long neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for extracting " + TEST_RUNS + " values from de.enough.polish.util.OAHashMap.");
		System.gc();
		time = System.currentTimeMillis();
		String[] stringValues = (String[]) map.values( new String[ map.size() ]);
		assertNotNull( stringValues );
		assertEquals( map.size(), stringValues.length );
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for extracting " + TEST_RUNS + " String values from de.enough.polish.util.HashMap.");

		
		
		java.util.HashMap j2semap = new java.util.HashMap();
		Collection col = j2semap.values();
		values = col.toArray();
		assertEquals( 0, values.length  );
		for (int i = 0; i < TEST_RUNS; i++) {
			Object key = this.integerKeys[i];
			Object value = this.stringKeys[i];
			j2semap.put( key, value );
		}
		System.gc();
		time = System.currentTimeMillis();
		for (int i = 0; i < 3; i++ ) {
			col = j2semap.values();
			values = col.toArray();
			assertNotNull( values );
			assertEquals( j2semap.size(), values.length );
		}
		neededTime = System.currentTimeMillis() - time;
		System.out.println("needed " + neededTime + "ms for extracting " + TEST_RUNS + " values from java.util.HashMap.");

//		Hashtable table = new Hashtable();
//		col = table.values();
//		values = col.toArray();
//		assertEquals( 0, values.length  );
//		for (int i = 0; i < TEST_RUNS; i++) {
//			Object key = this.integerKeys[i];
//			Object value = this.stringKeys[i];
//			table.put( key, value );
//		}
//		System.gc();
//		for (int i = 0; i < 3; i++ ) {
//			time = System.currentTimeMillis();
//			col = table.values();
//			values = col.toArray();
//			assertNotNull( values );
//			assertEquals( table.size(), values.length );
//		}
//		neededTime = System.currentTimeMillis() - time;
//		System.out.println("needed " + neededTime + "ms for extracting " + TEST_RUNS + " values from java.util.Hashtable.");
	}
}
