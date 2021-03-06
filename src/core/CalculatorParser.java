package core;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalculatorParser {
    private Stack<Double>operand = new Stack<Double>();
    private Stack<String>operator = new Stack<String>();

    private Map<String, Integer>priority = new HashMap<>();

    public CalculatorParser()
    {
        this.priority.put("÷", 1);
        this.priority.put("×", 1);
        this.priority.put("-", 0);
        this.priority.put("+", 0);
    }

    private int getPriority(String operator)
    {
        return this.priority.get(operator);
    }

    private boolean isHighestPriority(String operator)
    {
        if(this.operator.isEmpty())
            return true;
        else if(this.getPriority(operator) > this.getPriority(this.operator.peek()))
            return true;
        else
            return false;
    }

    public String getRPN(String expression)
    {
        String rpn = "";
        int beginIndex = 0, endIndex = 0;
        String currentOperator = null;

        for(int i = 0; i<expression.length(); i++)
        {
            int firstChar = expression.codePointAt(i);
            if(Character.isDigit(firstChar) || '.' == firstChar || '-' == firstChar)
                endIndex++;
            else
            {
                rpn += expression.substring(beginIndex, endIndex) + " ";
                beginIndex = endIndex + 1;

                currentOperator = expression.substring(endIndex, endIndex + 1);

                if(isHighestPriority(currentOperator))
                    this.operator.push(currentOperator);
                else
                {
                    do {
                        rpn += this.operator.pop() + " ";
                    }while(!isHighestPriority(currentOperator));
                    this.operator.push(currentOperator);
                }
                endIndex++;
            }
        }

        rpn += expression.substring(beginIndex, endIndex) + " ";

        while(!this.operator.isEmpty())
        {
            rpn += this.operator.pop() + " ";
        }

        return rpn;
    }

    public String getResult(String rpn)
    {
        double firstOperand = 0;
        double secondOperand = 0;

        double result = 0;

        String[] temp = rpn.trim().split(" ");

        for(int i = 0; i<temp.length; i++)
        {
            if(Character.isDigit(temp[i].codePointAt(0)) || '-' == temp[i].codePointAt(0))
            {
                this.operand.push(Double.parseDouble(temp[i]));
            }
            else
            {
                secondOperand = this.operand.pop();
                firstOperand = this.operand.pop();

                switch(temp[i])
                {
                    case "÷":
                        result = firstOperand / secondOperand;
                        break;
                    case "×":
                        result = firstOperand * secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;

                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                }
                this.operand.push(result);
            }
        }

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(15);

        return format.format(this.operand.pop());
    }
}
