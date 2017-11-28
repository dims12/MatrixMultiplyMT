package com.presenso.matrixmultiplymt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A multiplication of row and column in the form of {@link Callable}. Object holds
 * the indices of row and column being multiplied
 *
 * Task is automatically submitted to executor upon object creation, then
 * {@link #assign(Matrix)} method can be called to set result to destination matrix
 */
public class MultiplyRowAndColTask implements Callable<Double> {

   private final Matrix matrix1;
   private final int row1;

   private final Matrix matrix2;
   private final int col2;

   private final Future<Double> future;

   /**
    * Remembers arguments and submits the task
    *
    * @param matrix1
    * @param matrix2
    * @param row1
    * @param col2
    */
   protected MultiplyRowAndColTask(Matrix matrix1, Matrix matrix2, int row1, int col2) {
      this.matrix1 = matrix1;
      this.row1 = row1;

      this.matrix2 = matrix2;
      this.col2 = col2;

      future = Executor.submitMultiplyRowAndColTask(this);
   }

   @Override
   public Double call() throws Exception {
      return Executor.multiplyRowAndCol_ST(matrix1, matrix2, row1, col2);
   }

   /**
    * Obtaining result
    *
    * @param dest
    */
   protected void assign(Matrix dest) {
      try {
         dest.data[row1][col2] = future.get();
      } catch (InterruptedException | ExecutionException e) {
         throw new RuntimeException(e);
      }
   }
}
