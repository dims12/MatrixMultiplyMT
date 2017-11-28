package com.presenso.matrixmultiplymt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A multiplication of row and column in the form of {@link Callable}. Object holds
 * the indices of row and column being multiplied
 *
 */
public class MultiplyRowAndColTask implements Runnable {

   private final Matrix dest;

   private final Matrix matrix1;
   private final int row1;

   private final Matrix matrix2;
   private final int col2;

   private final Future<MultiplyRowAndColTask> future;

   /**
    * Remembers arguments and submits the task
    */
   protected MultiplyRowAndColTask(Matrix dest, Matrix matrix1, Matrix matrix2, int row1, int col2) {

      this.dest = dest;

      this.matrix1 = matrix1;
      this.row1 = row1;

      this.matrix2 = matrix2;
      this.col2 = col2;

      future = Executor.submitMultiplyRowAndColTask(this);
   }

   @Override
   public void run() {
      dest.data[row1][col2] = Executor.multiplyRowAndCol_ST(matrix1, matrix2, row1, col2);
   }

   public MultiplyRowAndColTask get() throws ExecutionException, InterruptedException {
      return future.get();
   }


}
