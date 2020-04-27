package ejb;

import javax.ejb.Remote;

@Remote
public interface CalcRemote {

	 int add(int a, int b);
	 int sub(int a, int b);
	 int mul(int a, int b);
	 int div(int a, int b) throws MyExeception;
}
