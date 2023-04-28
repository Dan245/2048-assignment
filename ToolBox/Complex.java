package ToolBox;

public class Complex {
    public double a;
    public double b;

    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public static Complex sum(Complex addendA, Complex addendB) {
        return new Complex(addendA.a + addendB.a, addendA.b + addendB.b);
    }

    public static Complex sum(Complex complexAddend, double realAddend) {
        return new Complex(complexAddend.a + realAddend, complexAddend.b);
    }

    public static Complex difference(Complex minuend, Complex subtrahend) {
        return new Complex(minuend.a - subtrahend.a, minuend.b - subtrahend.b);
    }

    public static Complex difference(Complex complexNum, double realNum) {
        return Complex.difference(complexNum, realNum, true);
    }

    public static Complex difference(Complex complexNum, double realNum, boolean complexIsMinuend) {
        return complexIsMinuend ? new Complex(complexNum.a - realNum, complexNum.b) :
                new Complex(realNum - complexNum.a, -complexNum.b);
    }

    public static Complex product(Complex factorA, Complex factorB) {
        double a = factorA.a * factorB.a - factorA.b * factorB.b;
        double b = factorA.a * factorB.a + factorA.b * factorB.b;
        return new Complex(a, b);
    }

    public static Complex product(Complex complexFactor, double realFactor) {
        return new Complex(complexFactor.a * realFactor, complexFactor.b * realFactor);
    }

    public static Complex quotient(Complex dividend, Complex divisor) {
        double a = (dividend.a * divisor.a + dividend.b * divisor.b) / (Math.pow(divisor.a, 2) + Math.pow(divisor.b, 2));
        double b = (dividend.b * divisor.a - dividend.a * divisor.b) / (Math.pow(divisor.a, 2) + Math.pow(divisor.b, 2));
        return new Complex(a, b);
    }


    public static Complex quotient(Complex complexNum, double realNum) {
        return Complex.quotient(complexNum, realNum, true);
    }

    public static Complex quotient(Complex complexNum, double realNum, boolean complexIsDividend) {
        return complexIsDividend ? new Complex(complexNum.a / realNum, complexNum.b / realNum) :
                new Complex(realNum / complexNum.a, realNum / complexNum.b);
    }

    public void add(Complex addend) {
        Complex newComplex = Complex.sum(this, addend);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void subtract(Complex subtrahend) {
        Complex newComplex = Complex.difference(this, subtrahend);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void multiply(Complex factor) {
        Complex newComplex = Complex.product(this, factor);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void divide(Complex divisor) {
        Complex newComplex = Complex.quotient(this, divisor);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void add(double addend) {
        Complex newComplex = Complex.sum(this, addend);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void subtract(double realNum) {
        Complex newComplex = Complex.difference(this, realNum);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void subtract(double realNum, boolean complexIsMinuend) {
        Complex newComplex = Complex.difference(this, realNum, complexIsMinuend);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void multiply(double factor) {
        Complex newComplex = Complex.product(this, factor);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void divide(double realNum) {
        Complex newComplex = Complex.quotient(this, realNum);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

    public void divide(double realNum, boolean complexIsDividend) {
        Complex newComplex = Complex.quotient(this, realNum, complexIsDividend);
        this.a = newComplex.a;
        this.b = newComplex.b;
    }

}