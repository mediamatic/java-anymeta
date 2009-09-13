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

		// Lookup an RFID tag.
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("type", "rfid");
		args.put("raw", "urn:rfid:DEADBEEFxx");
		
		o = (JSONObject)api.doMethod("identity.identify", args);
		System.out.println(o.toString());

		// Get the 10 newest uploaded files.
		args.clear();
		args.put("q_kind", "ATTACHMENT");
		args.put("q_sort", "-create_date");
		args.put("paglen", "10");
		
		JSONArray a = (JSONArray)api.doMethod("query.execute", args);
		System.out.println(a.toString());

		
	}
}
