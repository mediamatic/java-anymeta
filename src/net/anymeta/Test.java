package net.anymeta;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

import net.anymeta.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] arg) 
	throws AnyMetaRegistryException,AnyMetaException, JSONException
	{
		AnyMetaAPI api = AnyMetaAPI.fromRegistry("www.mediamatic.net");
		JSONObject o = api.doMethod("anymeta.user.info");
		System.out.println("Logged in as " + o.getString("title"));

		HashMap<String, String> args = new HashMap();
		args.put("field", "text.title");
		args.put("id", "22661");
		
		o = api.doMethod("anymeta.predicates.get", args);
		System.out.println(o.getString("result"));
	}
}
