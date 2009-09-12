
// An example which reads an RFID from a sonmicro reader, 
// and retrieves the associated person. It then prints the person's name.

import org.json.*;
import net.anymeta.*;

// Download library at: http://bitbucket.org/arjan/onsignal-rfid/
import org.onsignal.rfid.*;
import gnu.io.*;

// global API object.
AnyMetaAPI api = null;

RFIDReader r;

import java.util.HashMap;

class TagListener implements RFIDListener
{
    void tagAdded(RFIDTagEvent e) {
	//println("Tag " + e.getTag() + " was added to reader " + e.getReader());

        // Setup the arguments for the API call "identity.identify".
        HashMap args = new HashMap();
        args.put("type", "rfid");
        args.put("raw", "urn:rfid:" + e.getTag().toString().replaceAll(":", ""));
        
        try {
          // Do the call
          JSONObject person = api.doMethod("identity.identify", args);
          
          // We got a response!

          if (person == null) {
            println("No person is connected to " + e.getTag());
          } else {
            println("Connected person: " + person.getString("title"));
          }
          
        } catch (JSONException ex) {
          println(ex);
        } catch (AnyMetaException ex) {
          println(ex);
        }
    }

    void tagRemoved(RFIDTagEvent e) {
    }
}



void setup()
{
   // Setup anymeta connection
   
   try {
     api = AnyMetaAPI.fromRegistry("www.mediamatic.net");
   } catch (AnyMetaRegistryException e) {
     // The specified API key was not found
     println("Anymeta registry exception: " + e.getMessage());
   }
   
   // Setup RFID reader.
   String portName = "/dev/ttyUSB0";
    try {
	r = new RFIDReader(portName, new TagListener());
    } catch (NoSuchPortException e) {
	println("Port "+portName+" was not found!");
    } catch (PortInUseException e) {
	println("Port "+portName+" is in use by another program.");
    }
   
}
