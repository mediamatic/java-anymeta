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
		HashMap args = new HashMap();
		args.put("mime", "image/jpeg");
		args.put("data", "@/home/arjan/poster.jpg");
		
		o = (JSONObject)api.doMethod("anymeta.attachment.create", args);
		System.out.println(o.toString());
		
	}
}
