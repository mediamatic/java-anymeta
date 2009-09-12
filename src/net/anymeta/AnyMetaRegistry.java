/*
 * Copyright 2009, Arjan Scherpenisse <arjan@scherpenisse.net>
 * See LICENSE for details.
 */

package net.anymeta;

import org.ini4j.*;
import java.io.*;
import java.util.*;


/**
 * The AnyMeta registry is a file in your home directory called ".anymeta". 
 * It contains, in .ini format, OAuth credentials to access AnyMeta sites.
 * This class assists you in loading the information from that file.
 * 
 * @author arjan
 */
public class AnyMetaRegistry 
{

	private static AnyMetaRegistry instance = null;
	
	private Ini ini;
	
	private AnyMetaRegistry() throws AnyMetaRegistryException {
		try {
			ini = new Ini(new FileReader(System.getProperty("user.home")+"/.anymeta"));
		} catch (Exception e) {
			throw new AnyMetaRegistryException(e.getMessage());
		}
	}
	
	public static AnyMetaRegistry getInstance() throws AnyMetaRegistryException {
		if (instance == null) {
			instance = new AnyMetaRegistry();
		}
		return instance;
	}
	
	public String toString() {
		return "<AnyMetaRegistry instance>";
	}
	
	public Map<String, String> get(String id) {
		return ini.get(id);
	}
}

