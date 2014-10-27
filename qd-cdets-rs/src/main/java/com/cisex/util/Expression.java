package com.cisex.util;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/3/13
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Expression {
    private String leftOperand;
    private String operator;
    private String rightOperand;

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }
}
