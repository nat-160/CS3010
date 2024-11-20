import java.util.Scanner;
import java.math.BigInteger;
class dec2ieee {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number: ");
        String choice = input.next();

        String sci = str2sci(choice);
        String dec = sci2dec(sci);
        String bin = dec2bin(dec);
        String ieee = bin2ieee(bin);
        if(isZero(dec)) ieee = "0 00000000 00000000000000000000000";

        System.out.println("IEEE 754: "+ieee);

        input.close();
    }

    //outputs a number with a decimal and exponent
    static String str2sci(String input){
        //clean
        String num = input.replaceAll("[\\(\\)\\[\\]]","");
        //mantissa & exponent
        String mantissa = "", exponent = "";
        if(num.contains("x")){
            String[] temp = num.split("[x\\^]");
            mantissa = temp[0];
            exponent = temp[2];
        } else {
            mantissa = num;
            exponent = "0";
        }
        //decimal
        if(!mantissa.contains("."))
            mantissa += ".";
        //padding
        if(mantissa.charAt(mantissa.length()-1)=='.')
            mantissa += "0";
        if(mantissa.charAt(0)=='.')
            mantissa = "0" + mantissa;
        //return
        return mantissa + "x10^" + exponent;
    }

    static String sci2dec(String input){
        //extract
        String[] temp = input.split("[x\\^]");
        int exponent = Integer.parseInt(temp[2]);
        temp = temp[0].split("[.]");
        StringBuilder left = new StringBuilder(temp[0]);
        StringBuilder right = new StringBuilder(temp[1]);
        //sign
        boolean negative = input.charAt(0)=='-';
        if(negative) left.deleteCharAt(0);
        if(exponent>0){ //right side
            while(exponent>right.length())
                right.append('0');
            if(exponent<right.length())
                right.insert(exponent,'.');
            else
                right.append(".0");
        }
        if(exponent<0){ //left side
            while(0-exponent>left.length())
                left.insert(0,'0');
            left.insert(left.length()+exponent,'.');
            if(left.charAt(0)=='.')
                left.insert(0,'0');
        }
        if(exponent==0) //middle
            left.append('.');
        return (negative ? "-" : "") + left + right;
    }

    static String dec2bin(String input){
        boolean negative = input.charAt(0)=='-';
        if(negative) input = input.substring(1);
        String[] temp = input.split("[.]");
        String whole = whole2bin(temp[0]);
        String fractional = fractional2bin(temp[1]);
        return (negative ? "-" : "") + whole + "." + fractional;
    }

    static String whole2bin(String whole){
        String output = "";
        BigInteger count = new BigInteger(whole);
        BigInteger two = new BigInteger("2");
        while(count!=BigInteger.ZERO){
            output = ((count.mod(two)==BigInteger.ZERO) ? "0" : "1") + output;
            count = count.divide(two);
        }
        return output;
    }

    static String fractional2bin(String fractional){
        String output = "";
        BigInteger count = new BigInteger(fractional);
        BigInteger limit = BigInteger.TEN.pow(fractional.length());
        while(count!=BigInteger.ZERO && output.length()<127){
            count = count.multiply(new BigInteger("2"));
            output += (count.compareTo(limit)>=0) ? "1" : "0";
            if (count.compareTo(limit)>=0) count = count.subtract(limit);
        }
        return output;
    }


    static String bin2ieee(String input){
        boolean negative = input.charAt(0)=='-';
        if(negative) input = input.substring(1);
        StringBuilder data = new StringBuilder(input);
        int decimal = data.indexOf(".");
        int firstOne = data.indexOf("1");
        data.deleteCharAt(decimal);
        data.delete(0,data.indexOf("1")+1);
        data.append("00000000000000000000000");
        data.setLength(23);
        int exponent = (decimal>firstOne)
            ? 126+decimal-firstOne
            : 127+decimal-firstOne;
        if(exponent>255) return "0 11111111 00000000000000000000000";
        String exp = whole2bin(""+exponent);
        while(exp.length()<8) exp = "0" + exp;
        return (negative ? "1 " : "0 ") + exp + " " + data;
    }

    static boolean isZero(String input){
        for(char c : input.toCharArray())
            if(c!='.' && c!='0') return false;
        return true;
    }
}
