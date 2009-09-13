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

     // Get info about the user that is associated with the registry entry.
     JSONObject r = (JSONObject)api.doMethod("anymeta.user.info");
     println("Connected as " + r.getString("title"));


   } catch (AnyMetaException e) {
     // An anymeta error occurred:
     println("Anymeta exception: " + e.getMessage());

   } catch (JSONException e) {
     // The parsing of the result went wrong
     println(e.getMessage());
   }
}
