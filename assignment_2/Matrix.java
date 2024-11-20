import java.util.*;
import java.io.*;

public class Matrix{
  public static void main(String[] args){
    if(args.length==0)
      System.exit(0);

    boolean spp = false;
    String fileName = "";
    for(String a: args)
      if(a.equalsIgnoreCase("--spp"))
        spp = true;
      else if(fileName.equals(""))
        fileName = a;
    //remove extension
    if(fileName.contains(".lin"))
      fileName = fileName.substring(0,fileName.lastIndexOf(".lin"));

    try{
      File file = new File(fileName+".lin");
      Scanner input = new Scanner(file);

      int n = Integer.parseInt(input.nextLine());

      double[][] coeff = new double[n][n];
      double[] cons = new double[n];

      for(int i=0;i<n;i++){
        String[] row = input.nextLine().split("\\s+");
        for(int j=0;j<n;j++)
          coeff[i][j] = Double.parseDouble(row[j]);
      }
      for(int i=0;i<n;i++)
        cons[i] = Double.parseDouble(input.next());

      double[] sol = (spp) ? SPPGaussian(coeff,cons) : naiveGaussian(coeff,cons);
      FileWriter output = new FileWriter(fileName+".sol");
      String solOut = Arrays.toString(sol);
      solOut = solOut.substring(1,solOut.length()-1);
      output.write(solOut);
      output.close();
    } catch (IOException e) {
      System.out.println("AAAAAAAAAAAAA");
    }
  }

  public static void fwdElimination(double[][] coeff, double[] cons){
    int n = coeff.length;
    for(int k=0;k<n-1;k++){
      for(int i=k+1;i<n;i++){
        double mult = coeff[i][k] / coeff[k][k];
        for(int j=k;j<n;j++)
          coeff[i][j] -= mult * coeff[k][j];
        cons[i] -= mult * cons[k];
      }
    }
  }

  public static void backSubst(double[][] coeff, double[] cons, double[] sol){
    int n = coeff.length;
    sol[n-1] = cons[n-1] / coeff[n-1][n-1];
    for(int i=n-2;i>=0;i--){
      double sum = cons[i];
      for(int j=i+1;j<n;j++){
        sum -= coeff[i][j] * sol[j];
      }
      sol[i] = sum / coeff[i][i];
    }
  }

  public static double[] naiveGaussian(double[][] coeff, double[] cons){
    double[] sol = new double[coeff.length];
    fwdElimination(coeff,cons);
    backSubst(coeff,cons,sol);
    return sol;
  }

  public static void SPPFwdElimination(double[][] coeff, double[] cons, int[] ind){
    int n = coeff.length;
    double[] scaling = new double[n];
    for(int i=0;i<n;i++){
      double smax = 0;
      for(int j=1;j<n;j++){
        smax = Math.max(smax,Math.abs(coeff[i][j]));
      }
      scaling[i] = smax;
    }
    for(int k=0;k<n-1;k++){
      double rmax = 0;
      int maxInd = k;
      for(int i=k;i<n;i++){
        double r = Math.abs(coeff[ind[i]][k] / scaling[ind[i]]);
        if(r>rmax){
          rmax = r;
          maxInd = i;
        }
      }
      int temp = ind[maxInd];
      ind[maxInd] = ind[k];
      ind[k] = temp;
      for(int i=k+1;i<n;i++){
        double mult = coeff[ind[i]][k] / coeff[ind[k]][k];
        for(int j=k+1;j<n;j++){
          coeff[ind[i]][j] -= mult * coeff[ind[k]][j];
        }
        cons[ind[i]] -= mult * cons[ind[k]];
      }
    }
  }

  public static void SPPBackSubst(double[][] coeff, double[] cons, double[] sol, int[] ind){
    int n = coeff.length;
    sol[n-1] = cons[ind[n-1]] / coeff[ind[n-1]][n-1];
    for(int i=n-2;i>=0;i--){
      double sum = cons[ind[i]];
      for(int j=i+1;j<n;j++){
        sum -= coeff[ind[i]][j] * sol[j];
      }
      sol[i] = sum / coeff[ind[i]][i];
    }
  }

  public static double[] SPPGaussian(double[][] coeff, double[] cons){
    int n = coeff.length;
    double[] sol = new double[n];
    int[] ind = new int[n];
    for(int i=0;i<n;i++){
      ind[i] = i;
    }
    SPPFwdElimination(coeff,cons,ind);
    SPPBackSubst(coeff,cons,sol,ind);
    return sol;
  }
}
