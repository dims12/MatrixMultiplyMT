package com.presenso.matrixmultiplymt;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutorTest {

   @Test
   public void testMultiplyST() {

      Matrix matrix1 = new Matrix(new double[][] {
         {1, 2},
         {3, 4}
      });

      Matrix matrix2 = new Matrix(new double[][] {
         {5, 6},
         {7, 8}
      });

      Matrix expected = new Matrix(new double[][] {
         {19, 22},
         {43, 50}
      });

      Matrix actual = new Matrix(2);

      Executor.multiply_ST(actual, matrix1, matrix2);

      assertEquals(expected, actual);


   }


   @Test
   public void testMultiplyMT() {

      Matrix matrix1 = new Matrix(new double[][] {
         {1, 2},
         {3, 4}
      });

      Matrix matrix2 = new Matrix(new double[][] {
         {5, 6},
         {7, 8}
      });

      Matrix expected = new Matrix(new double[][] {
         {19, 22},
         {43, 50}
      });

      Matrix actual = new Matrix(2);

      Executor.multiply_MT(actual, matrix1, matrix2);

      assertEquals(expected, actual);


   }


   public double measureTimeInSeconds(Runnable runnable) {

      long start = System.nanoTime();
      runnable.run();
      long end = System.nanoTime();
      return (end - start) / 1000000000.;
   }

   @Test
   public void testSpeed() {

      Matrix matrix1 = Matrix.random(1000, -2, 2, 1);
      Matrix matrix2 = Matrix.random(1000, -2, 2, 2);

      System.out.println("Allocating matrices...");
      Matrix actual_st = new Matrix(1000);
      Matrix actual_mt = new Matrix(1000);

      System.out.println("Multiplying ST...");
      double elapsedST = measureTimeInSeconds(() -> Executor.multiply_ST(actual_st, matrix1, matrix2));
      System.out.println("Multiplying MT...");
      double elapsedMT = measureTimeInSeconds(() -> Executor.multiply_MT(actual_st, matrix1, matrix2));

      System.out.println(String.format("Elapsed time with ST is %.4f seconds", elapsedST));
      System.out.println(String.format("Elapsed time with MT is %.4f seconds", elapsedMT));

      assertTrue( elapsedST > elapsedMT );







   }


}