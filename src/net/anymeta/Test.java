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
        HashMap<String, Object> args;

		// Load the API
		AnyMetaAPI api = AnyMetaAPI.fromRegistry("pluto.local");
		
		// Get information for the currently logged in user.
		JSONObject o = (JSONObject)api.doMethod("anymeta.user.info");
		System.out.println("Logged in as " + o.getString("title"));

		//  
		args = new HashMap<String, Object>();
		args.put("q_kind", "PERSON");
		
		JSONArray a = (JSONArray)api.doMethod("query.execute", args);
		System.out.println(a.toString());
		System.out.println(a.length());

		// Lookup an RFID tag.
		args = new HashMap<String, Object>();
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("65");
		ids.add("94");
		args.put("ids", ids);
		args.put("predicate", "KNOWS");
		
		o = (JSONObject)api.doMethod("contact.link", args);
		System.out.println(o.toString());

		// Upload an image
		args = new HashMap<String, Object>();
		args.put("mime", "image/jpeg");
		args.put("data", "@C:\\bla.jpg");
		
		o = (JSONObject)api.doMethod("anymeta.attachment.create", args);
		System.out.println(o.toString());
		
	}
}
