import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class OpenXCHackathonService extends Service{

		String tag="TestService";
	   @Override
	   public void onCreate() {
	       super.onCreate();
	       Toast.makeText(this, "Service created...", Toast.LENGTH_LONG).show();      
	       Log.i(tag, "Service created...");
	   }
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
	    return Service.START_NOT_STICKY;
	  }
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
