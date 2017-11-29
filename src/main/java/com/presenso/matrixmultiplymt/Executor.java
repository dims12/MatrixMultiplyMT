package com.presenso.matrixmultiplymt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Singleton static class
 *
 * Incapsulates execution of matrix operations, either multithreaded (MT), or single threaded (ST). The
 * core member is {@link #delegate}
 *
 * Only matrix multiplication is implemented yet
 */
public class Executor {

   // minimal size of matrix at which multithreading is used
   protected static final int MTSize = 100;

   // number of threads to use; equal to the number of available processors
   protected static final int NThreads = Runtime.getRuntime().availableProcessors();

   // MT executor
   private static final ExecutorService delegate = Executors.newFixedThreadPool(NThreads);

   /**
    * ST multiplication of one row from first matrix with one column from second matrix
    *
    * @param matrix1 first matrix
    * @param matrix2 second matrix
    * @param row1 row number inside first matrix
    * @param col2 column number inside second matrix
    * @return
    */
   protected static double multiplyRowAndCol_ST(Matrix matrix1, Matrix matrix2, int row1, int col2) {
      double ans = 0;
      for(int i=0; i<matrix1.size(); ++i) {
         ans += matrix1.data[row1][i] * matrix2.data[i][col2];
      }
      return ans;
   }

   protected static void multiplyStripe_ST(Matrix dest, Matrix matrix1, Matrix matrix2, int col_start, int col_end) {
      for(int row=0; row<dest.size(); ++row) {
         for(int col=col_start; col<col_end; ++col) {
            dest.data[row][col] = multiplyRowAndCol_ST(matrix1, matrix2, row, col);
         }
      }
   }

   /**
    * ST multiplication of two matrices
    *
    * Sizes are not checked, caller responsible
    *
    * @param dest destination matrix to fill
    * @param matrix1 source matrix #1
    * @param matrix2 source marix #2
    */
   protected static void multiply_ST(Matrix dest, Matrix matrix1, Matrix matrix2) {
      for(int row=0; row<dest.size(); ++row) {
         for(int col=0; col<dest.size(); ++col) {
            dest.data[row][col] = multiplyRowAndCol_ST(matrix1, matrix2, row, col);
         }
      }
   }

   /**
    * MT multiplication of two matrices
    *
    * Task is splitted into multiplications of single rows and columns
    *
    * Sizes are not checked, caller responsible
    *
    * @param ans
    * @param matrix1
    * @param matrix2
    */
   protected static void multiply_MT(Matrix ans, Matrix matrix1, Matrix matrix2) {

      List<MultiplyStripeTask> tasks = new ArrayList<>();

      int stripeSize = ans.size() / NThreads;

      // map
      int col_end = 0;
      System.out.println("Mapping tasks...");
      for(int col_start=0; col_start<ans.size() && col_end<ans.size(); col_start+=stripeSize) {
         col_end = col_start + stripeSize;
         if(col_end > ans.size()) {
            col_end = ans.size();
         }
         else if(ans.size() - col_end < stripeSize/2) {
            col_end = ans.size();
         }
         System.out.println(String.format("Task %d..%d", col_start, col_end));
         tasks.add(new MultiplyStripeTask(ans, matrix1, matrix2, col_start, col_end));

      }
      System.out.println(String.format("Mapped %d tasks, awaiting finish...", tasks.size()));


      // reduce
      try {
         for(MultiplyStripeTask task : tasks) {
               task.get();
         }
      } catch (ExecutionException | InterruptedException e) {
         throw new RuntimeException(e);
      }

   }

   /**
    * Multiplication of two matrices either MT or ST depending on size
    *
    * Sizes are not checked, caller responsible
    *
    * @param ans
    * @param matrix1
    * @param matrix2
    */
   protected static void multiply(Matrix ans, Matrix matrix1, Matrix matrix2) {

      if( ans.size() < MTSize ) {
         multiply_ST(ans, matrix1, matrix2);
      }
      else {
         multiply_MT(ans, matrix1, matrix2);
      }

   }

   /**
    * Submitting single multiplication task to executor
    * @param task
    * @return
    */
   protected static Future<MultiplyRowAndColTask> submitMultiplyRowAndColTask(MultiplyRowAndColTask task) {
      return delegate.submit(task, task);
   }

   protected static Future<MultiplyStripeTask> submitMultiplyStripeTask(MultiplyStripeTask task) {
      return delegate.submit(task, task);
   }


}
