package ejb;

import java.util.List;
import java.sql.SQLException;
import java.util.Dictionary;
import javax.ejb.Remote;

import objects.Employee;
import objects.Meal;
import objects.Reservation;
import objects.Restaurant;
import objects.StatisticData;

@Remote
public interface ReservationRemote {

		Dictionary<Integer, Reservation> searchReserv(Dictionary<String, String> args);
		int addReserv(Dictionary <String, String>  args);
		void deleteReserv(int id);
		void updateReserv(Dictionary <String, String>  args);
		List <StatisticData> statReserv();
		Dictionary<Integer, Restaurant> getRestReserv(Integer id) throws SQLException;
		Dictionary<Integer, Meal> getMealReserv(Integer id);
		Dictionary<Integer, Employee> getEmpReserv(Integer id);
}
