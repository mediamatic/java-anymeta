package net.anymeta;

import java.io.IOException;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.*;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.SignatureMethod;;

public class AnyMetaAPI 
{

	public String entrypoint;
	private String ckey;
	private String csec;
	private String tkey;
	private String tsec;
	
	public AnyMetaAPI(String entrypoint, String ckey, String csec, String tkey, String tsec)
	{
		this.entrypoint = entrypoint;
		this.ckey = ckey;
		this.csec = csec;
		this.tkey = tkey;
		this.tsec = tsec;
	}
	
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

	public JSONObject doMethod(String method)
		throws AnyMetaException {
		return this.doMethod(method, new HashMap<String, String>()); 
	}
	
	public JSONObject doMethod(String method, Map<String, String> args) 
		throws AnyMetaException {
		
		String params = "";
		
		args.put("method", method);
		args.put("format", "json");
		
		for (Iterator<String> it=args.keySet().iterator(); it.hasNext();) {
			String k = it.next();
			params += k + "=" + args.get(k);
			if (it.hasNext()) params += "&";
		}
		
		String url = this.entrypoint + "?" + params;
		
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(this.ckey, this.csec, SignatureMethod.PLAINTEXT);
		consumer.setTokenWithSecret(this.tkey, this.tsec);
		
		HttpPost request = new HttpPost(url);
		
		try {
			consumer.sign(request);
		} catch (OAuthMessageSignerException e) {
			throw new AnyMetaException(e.getMessage());
		} catch (OAuthExpectationFailedException e) {
			throw new AnyMetaException(e.getMessage());
		}
		
		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		String response = "";
		try {
			response = client.execute(request, handler);
		} catch (IOException e) {
			throw new AnyMetaException(e.getMessage());
		}
		
		if (response.equals("null"))
		{
			// empty response
			return new JSONObject();
		}
		
		// System.out.println(response);
		
		JSONObject o;
		try {
			o = new JSONObject(response);
		} catch (JSONException e) {
			throw new AnyMetaException(e.getMessage());
		}
		
		if (o.has("err")) {
			// handle error
			try {
				JSONObject err = o.getJSONObject("err").getJSONObject("-attrib-");
				throw new AnyMetaException(err.getString("code") + ": " + err.getString("msg"));
			} catch (JSONException e) {
				throw new AnyMetaException("Unexpected response in API error");
			}
		}
		
		return o;
	}
}
