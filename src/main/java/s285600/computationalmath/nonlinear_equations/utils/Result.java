package s285600.computationalmath.nonlinear_equations.utils;

/**
 * @author Kir
 * Created on 28.03.2021
 */

public class Result {
    private double[] ans;
    private int iters;
    private boolean isValid;
    private double x;
    private String message = "default";
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double[] getAns() {
        return ans;
    }

    public void setAns(double[] ans) {
        this.ans = ans;
    }

    public int getIters() {
        return iters;
    }

    public void setIters(int iters) {
        this.iters = iters;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
