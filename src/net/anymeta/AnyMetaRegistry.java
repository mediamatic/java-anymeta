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
 * <p>
 * Note: this class does *not* support creating these files! Use the
 * excellent AnyMetaAPI python package ("<tt>sudo easy_install
 * AnyMetaAPI</tt>") to edit this registry. 
 * <p>
 * For more info, <a href="http://trac.mediamatic.nl/python-anymeta/wiki/GettingStarted">click here</a>.
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
	
	public Map<String, String> get(String id) throws AnyMetaRegistryException {
		Map<String, String> result = ini.get(id);
		if (result == null)
		{
			throw new AnyMetaRegistryException("Registry entry not found: " + id);
		}
		return result;
	}
}

