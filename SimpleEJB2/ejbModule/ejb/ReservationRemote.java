package ejb;

import java.util.List;
import java.util.Dictionary;
import javax.ejb.Remote;
import objects.Reservation;

@Remote
public interface ReservationRemote {

		List<Dictionary<Integer, Reservation>> searchReserv(Dictionary<String, String> args);
		int addReserv(Dictionary <String, String>  args);
		void deleteReserv(int id);
		void updateReserv(Dictionary <String, String>  args);
		
}
