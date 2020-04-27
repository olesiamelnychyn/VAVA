package application;

import ejb.CalcRemote;
import ejb.MyExeception;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
public class ControllerMain implements Runnable {
    
	private Calculator calculator;
    @FXML
    private TextField displayTextField;

    @FXML
    private Button num1Button;

    @FXML
    private Button num2Button;

    @FXML
    private Button num3Button;

    @FXML
    private Button num4Button;

    @FXML
    private Button num5Button;

    @FXML
    private Button num6Button;

    @FXML
    private Button num7Button;

    @FXML
    private Button num8Button;

    @FXML
    private Button num9Button;

    @FXML
    private Button num0Button;

    @FXML
    private Button addButton;

    @FXML
    private Button subButton;

    @FXML
    private Button mulButton;

    @FXML
    private Button divButton;

    @FXML
    private Button clrButton;

    @FXML
    private Button resButton;

    @FXML
    private void initialize() {
        System.out.println("initialize()");
        calculator = new Calculator();
        displayTextField.setText("0");

        num1Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("1")));
        num2Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("2")));
        num3Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("3")));
        num4Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("4")));
        num5Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("5")));
        num6Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("6")));
        num7Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("7")));
        num8Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("8")));
        num9Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("9")));
        num0Button.setOnAction(event -> displayTextField.setText(calculator.addNumber("0")));
        addButton.setOnAction(event -> {
            calculator.addOperator("+");
            displayTextField.setText("");
        });
        subButton.setOnAction(event -> {
            calculator.addOperator("-");
            displayTextField.setText("");
        });
        mulButton.setOnAction(event -> {
            calculator.addOperator("*");
            displayTextField.setText("");
        });
        divButton.setOnAction(event -> {
            calculator.addOperator("/");
            displayTextField.setText("");
        });
        resButton.setOnAction(event -> new Thread(this).start());
        clrButton.setOnAction(event -> displayTextField.setText(""));
    }

    private void doRequest(String[] data) throws NamingException, MyExeception {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
//        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//        props.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
//        props.put("jboss.naming.client.ejb.context", "true");
//        props.put(Context.SECURITY_PRINCIPAL, "admin");
//        props.put(Context.SECURITY_CREDENTIALS, "123");
        Context ctx = new InitialContext(props);
        //System.out.println(ctx.getEnvironment());

        CalcRemote calcRemote = (CalcRemote) ctx.lookup("ejb:/SimpleEJB2//CalcSessionEJB!ejb.CalcRemote");    //java:jboss/exported/Calc_ear_exploded/ejb/CalcSessionEJB!com.calc.server.CalcRemote
        String res = Integer.toString(process(calcRemote, data));
        displayTextField.setText(res);
        System.out.println(res);
    }

    private int process(CalcRemote calcRemote, String[] resData) throws MyExeception {
        int operand1 = Integer.parseInt(resData[0]);
        int operand2 = Integer.parseInt(resData[1]);
        String operator = resData[2];
        switch (operator) {
            case "+":
                return calcRemote.add(operand1, operand2);
            case "-":
                return calcRemote.sub(operand1, operand2);
            case "*":
                return calcRemote.mul(operand1, operand2);
            case "/":
                return calcRemote.div(operand1, operand2);
        }
        return 0;
    }

    @Override
    public void run() {
    	System.out.println("here");
        displayTextField.setText("WAITING...");
        try {
            doRequest(calculator.getResult());
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (MyExeception ex) {
            displayTextField.setText(ex.getMessage());
        }
    }
}