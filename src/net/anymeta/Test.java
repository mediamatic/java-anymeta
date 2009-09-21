/*
 * Copyright 2009, Arjan Scherpenisse <arjan@scherpenisse.net>
 * See LICENSE for details.
 */

package net.anymeta;

import java.util.*;
import org.json.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] arg) 
		throws AnyMetaRegistryException, AnyMetaException, JSONException
	{
		// Load the API
		AnyMetaAPI api = AnyMetaAPI.fromRegistry("pluto.local");
		
		// Get information for the currently logged in user.
		JSONObject o = (JSONObject)api.doMethod("anymeta.user.info");
		System.out.println("Logged in as " + o.getString("title"));

		//  
		HashMap<String, Object> args2 = new HashMap<String, Object>();
		args2.put("q_kind", "PERSON");
		
		JSONArray a = (JSONArray)api.doMethod("query.execute", args2);
		System.out.println(a.toString());
		System.out.println(a.length());

		// Lookup an RFID tag.
		HashMap<String, Object> args = new HashMap<String, Object>();
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("65");
		ids.add("94");
		args.put("ids", ids);
		args.put("predicate", "KNOWS");
		
		o = (JSONObject)api.doMethod("contact.link", args);
		System.out.println(o.toString());
		
	}
}
