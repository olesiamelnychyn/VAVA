package application;


public class Calculator {
    private String buffer = "", operator, operand1, operand2;
    private boolean isOperator = false;

    public String addNumber(String value) {
        buffer += value;
        if (!isOperator) {
            operand1 = buffer;
        } else {
            operand2 = buffer;
        }
        return buffer;
    }

    public void addOperator(String value) {
        operator = value;
        buffer = "";
        isOperator = true;
    }

    public String[] getResult() {
        isOperator = false;
        buffer = "";
        int check=0;
        try {
            check = Integer.parseInt(operand1);
            check = Integer.parseInt(operand2);
        } catch (NumberFormatException ex) {
           
        }
        System.out.println(check);
        return new String[]{operand1, operand2, operator};
    }
}