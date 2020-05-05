package ejb;

import java.util.List;
import java.util.Dictionary;
import javax.ejb.Remote;
import objects.Reservation;
import objects.StatisticData;

@Remote
public interface ReservationRemote {

		Dictionary<Integer, Reservation> searchReserv(Dictionary<String, String> args);
		int addReserv(Dictionary <String, String>  args);
		void deleteReserv(int id);
		void updateReserv(Dictionary <String, String>  args);
		List <StatisticData> statReserv();
}
