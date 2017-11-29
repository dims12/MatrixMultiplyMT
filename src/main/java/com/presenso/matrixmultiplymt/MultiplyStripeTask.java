package com.presenso.matrixmultiplymt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A multiplication of row and column in the form of {@link Callable}. Object holds
 * the indices of row and column being multiplied
 *
 */
public class MultiplyStripeTask implements Runnable {

   private final Matrix dest;

   private final Matrix matrix1;

   private final Matrix matrix2;
   private final int col_start;
   private final int col_end;

   private final Future<MultiplyStripeTask> future;

   /**
    * Remembers arguments and submits the task
    */
   protected MultiplyStripeTask(Matrix dest, Matrix matrix1, Matrix matrix2, int col_start, int col_end) {

      this.dest = dest;

      this.matrix1 = matrix1;

      this.matrix2 = matrix2;
      this.col_start = col_start;
      this.col_end = col_end;

      future = Executor.submitMultiplyStripeTask(this);
   }

   @Override
   public void run() {
      Executor.multiplyStripe_ST(dest, matrix1, matrix2, col_start, col_end);
   }

   public MultiplyStripeTask get() throws ExecutionException, InterruptedException {
      return future.get();
   }


}
