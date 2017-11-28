package com.presenso.matrixmultiplymt;

import java.util.Arrays;
import java.util.Random;

/**
 * Square matrix with ability to multiply by another matrix with {@link #multiply(Matrix)} method.
 *
 * I expect matrix is immutable to avoid data sharing issues,
 * so modifying methods should have acces to {@link #data}
 */
public class Matrix {

   public final static double Delta = 0.0001;

   // matrix data as just 2D array
   protected final double[][] data;

   public Matrix(int size) {
      data = new double[size][size];
   }

   public Matrix(double[][] data) {
      this.data = new double[data.length][data.length];

      for(int row=0; row<data.length; ++row) {
         if( data[row].length != data.length ) {
            throw new IllegalArgumentException("Array is not square");
         }
         for(int col=0; col<data.length; ++col) {
            this.data[row][col] = data[row][col];
         }
      }
   }

   public int size() {
      return data.length;
   }

   /**
    * Compares matrices with delta
    *
    * @param o
    * @param delta
    * @return
    */
   public boolean equals(Object o, double delta) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Matrix matrix = (Matrix) o;
      double[][] data = matrix.data;

      if( this.data.length != data.length ) {
         return false;
      }

      for(int row=0; row<data.length; ++row) {
         if( data[row].length != data.length ) {
            return false;
         }
         for(int col=0; col<data.length; ++col) {
            if( Math.abs(this.data[row][col] - data[row][col]) > delta ) {
               return false;
            }
         }
      }

      return true;
   }

   @Override
   public boolean equals(Object o) {
      return equals(o, Delta);
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(data);
   }

   /**
    * Multiplication of matrices
    *
    * Actual work is delegated to {@link Executor#multiply(Matrix, Matrix, Matrix)}
    *
    * @param ano
    * @return
    */
   public Matrix multiply(Matrix ano) {


      if( size() != ano.size() ) {
         throw new IllegalArgumentException("Sizes of matrices are not equal");
      }


      Matrix ans = new Matrix(size());

      Executor.multiply(ans, this, ano);

      return ans;

   }

   public static Matrix random(int size, double min, double max, long seed) {

      Random rnd = new Random(seed);

      Matrix ans = new Matrix(size);
      for(int row=0; row<ans.size(); ++row) {
         for(int col=0; col<ans.size(); ++col) {
            ans.data[row][col] = rnd.nextDouble()*(max-min)+min;
         }
      }

      return ans;


   }

   public static Matrix random(int size, double min, double max) {
      return random(size, min, max, 0);
   }


}
