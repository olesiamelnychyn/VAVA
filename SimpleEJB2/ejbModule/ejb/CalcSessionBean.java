package ejb;

import javax.ejb.Stateless;

@Stateless(name = "CalcSessionEJB")
public class CalcSessionBean implements CalcRemote {
    public CalcSessionBean() {
    }

    @Override
    public int add(int a, int b) {
    	System.out.println(a+" "+b);
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }

    @Override
    public int mul(int a, int b) {
        return a * b;
    }

    @Override
    public int div(int a, int b) throws MyExeception{
        try {
            return a / b;
        } catch (ArithmeticException ex) {
        	throw new MyExeception("Divide by Zero!!!");
        }
        
    }
}
