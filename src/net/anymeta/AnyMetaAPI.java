/*
 * Copyright 2009, Arjan Scherpenisse <arjan@scherpenisse.net>
 * See LICENSE for details.
 */

package net.anymeta;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.*;
import oauth.signpost.exception.*;
import oauth.signpost.signature.SignatureMethod;;

/**
 * Main API access class. Implements synchronous access to an AnyMeta site.
 *
 * @author arjan
 */
public class AnyMetaAPI
{
	public String entrypoint;
	private String ckey;
	private String csec;
	private String tkey;
	private String tsec;


	/**
	 * Create a AnyMetaAPI instance by specifying the entry point and
	 * OAuth credentials. It is often easier to use the "fromRegistry"
	 * method to separate your code and your OAuth secrets.
	 *
	 * @param entrypoint
	 * @param ckey
	 * @param csec
	 * @param tkey
	 * @param tsec
	 */
	public AnyMetaAPI(String entrypoint, String ckey, String csec, String tkey, String tsec)
	{
		this.entrypoint = entrypoint;
		this.ckey = ckey;
		this.csec = csec;
		this.tkey = tkey;
		this.tsec = tsec;
	}


	/**
	 * Load a AnyMetaAPI object from the registry.
	 *
	 * @param identifier	The registry identifier.
	 * @return				A valid AnyMetaAPI object which can be used to access the site.
	 * @throws AnyMetaRegistryException
	 */
	public static AnyMetaAPI fromRegistry(String identifier)
		throws AnyMetaRegistryException
	{
		AnyMetaRegistry reg = AnyMetaRegistry.getInstance();
		Map<String, String> values = reg.get(identifier);

		String ckey = values.get("c_key");
		String csec = values.get("c_sec");
		String tkey = values.get("t_key");
		String tsec = values.get("t_sec");
		String entrypoint = values.get("entrypoint");

		return new AnyMetaAPI(entrypoint, ckey, csec, tkey, tsec);
	}


	public String toString() {
		return "<AnyMetaAPI: " + this.entrypoint + ">";
	}

	/**
	 * Execute the given API call onto the anymeta instance.
	 *
	 * @param method	The method.
	 * @return			A JSONObject with the response.
	 * @throws AnyMetaException
	 */
	public Object doMethod(String method)
		throws AnyMetaException {
		return this.doMethod(method, new HashMap<String, Object>());
	}


	/**
	 * Execute the given API call onto the anymeta instance, with arguments.
	 *
	 * @param method 	The method, e.g. "anymeta.user.info"
	 * @param args		Arguments to give to the call.
	 * @return			A JSONObject or JSONArray with the response.
	 * @throws AnyMetaException
	 */
	public Object doMethod(String method, Map<String, Object> args)
		throws AnyMetaException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("method", method));
		params.add(new BasicNameValuePair("format", "json"));

		for (String k : args.keySet())
		{
			if (args.get(k) instanceof String) {
				String key = (String)args.get(k);
				String value;
				if (key.charAt(0) == '@') {
					// try to do some file-magic
					try {
						File f = new File(key.substring(1));
						if (f.exists() && f.canRead()) {
							byte[] buf = new byte[(int)f.length()];
							FileInputStream fis = new FileInputStream(f);
							fis.read(buf);
							value = new String(Base64.encodeBase64(buf));
						} else {
							value = key;
						}
					} catch (IOException e) {
						throw new AnyMetaException(e.getMessage());
					}
				} else {
					value = key;
				}
				params.add(new BasicNameValuePair(k,value));
			} else if (args.get(k) instanceof List) {
				for (String v : ((List<String>)args.get(k))) {
					params.add(new BasicNameValuePair(k+"[]",v));
				}
			}
		}

		String url = this.entrypoint;

		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(this.ckey, this.csec, SignatureMethod.PLAINTEXT);
		consumer.setTokenWithSecret(this.tkey, this.tsec);

		HttpPost request = new HttpPost(url);

		// set up httppost
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			request.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			throw new AnyMetaException(e.getMessage());
		}
		
		try {
			consumer.sign(request);
		} catch (OAuthMessageSignerException e) {
			throw new AnyMetaException(e.getMessage());
		} catch (OAuthExpectationFailedException e) {
			throw new AnyMetaException(e.getMessage());
		}

		DefaultHttpClient client = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();

		String response = "";
		try {
			response = client.execute(request, handler);
		} catch (IOException e) {
			throw new AnyMetaException(e.getMessage());
		}
		
		if (response.equalsIgnoreCase("null") || response.equalsIgnoreCase("false"))
		{
			return null;
		}

		if (!response.startsWith("[") && !response.startsWith("{"))
		{
			response = "{\"result\": " + response + "}";
		}

		// System.out.println(response);

		Object o;
		try {
			o = new JSONObject(response);
		} catch (JSONException e) {
			try {
				o = new JSONArray(response);
			} catch (JSONException e2) {
				throw new AnyMetaException(e.getMessage() + ": response=" + response);
			}
		}

		if (o instanceof JSONObject && ((JSONObject)o).has("err")) {
			// handle error
			try {
				JSONObject err = ((JSONObject)o).getJSONObject("err").getJSONObject("-attrib-");
				throw new AnyMetaException(err.getString("code") + ": " + err.getString("msg"));
			} catch (JSONException e) {
				throw new AnyMetaException("Unexpected response in API error: response=" + response);
			}
		}

		return o;
	}
}
