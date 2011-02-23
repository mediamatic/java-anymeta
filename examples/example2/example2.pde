// An example which connects to anymeta and prints out the name of the connected user.

import org.json.*;
import net.anymeta.*;

void setup()
{
    AnyMetaAPI api = null;

   try {
     api = AnyMetaAPI.fromRegistry("www.mediamatic.net");
   } catch (AnyMetaRegistryException e) {
     // The specified API key was not found
     println("Anymeta registry exception: " + e.getMessage());
   }

   // API has been set up; let's do an API call.

   try {

       HashMap<String, Object> args, data, text;


		// Upload an image
		args = new HashMap<String, Object>();
		args.put("mime", "image/jpeg");
		args.put("data", "@/home/arjan/Desktop/welcome.jpg");

		JSONObject o = (JSONObject)api.doMethod("anymeta.attachment.create", args);
		System.out.println(o.toString());

        /*
       
       // Create thing arguments
       data = new HashMap<String, Object>();
       data.put("kind", "PERSON");
       data.put("axo", "persons");
       data.put("pubstate", "1");

       text = new HashMap<String, Object>();
       text.put("language", "nl");
       text.put("title", "Some new person");
       text.put("subtitle", "really, a person");
       text.put("intro", "Hello, I'm a person. This is a link in wiki syntax: [www.nu.nl link]");
       data.put("text", text);

       // do the API call
       args = new HashMap<String, Object>();
       args.put("data", data);
     // Get info about the user that is associated with the registry entry.
       JSONObject r = (JSONObject)api.doMethod("anymeta.thing.insert", args);
       println("Result: " + r.toString());

        */

   } catch (AnyMetaException e) {
     // An anymeta error occurred:
     println("Anymeta exception: " + e.getMessage());
     
   }
}

