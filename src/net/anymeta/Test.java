/*
 * Copyright 2009, Arjan Scherpenisse <arjan@scherpenisse.net>
 * See LICENSE for details.
 */

package net.anymeta;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] arg) 
		throws AnyMetaRegistryException, AnyMetaException, JSONException
	{
		// Load the API
		AnyMetaAPI api = AnyMetaAPI.fromRegistry("www.mediamatic.net");
		
		// Get information for the currently logged in user.
		JSONObject o = api.doMethod("anymeta.user.info");
		System.out.println("Logged in as " + o.getString("title"));

		// Lookup an RFID tag.
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("type", "rfid");
		args.put("raw", "urn:rfid:DEADBEEF");
		
		o = api.doMethod("identity.identify", args);
		System.out.println(o.toString());
	}
}
