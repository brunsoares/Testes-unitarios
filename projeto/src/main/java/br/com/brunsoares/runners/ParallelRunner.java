package br.com.brunsoares.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRunner extends BlockJUnit4ClassRunner {
    public ParallelRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        setScheduler(new ThreadPoll());
    }

    private static class ThreadPoll implements RunnerScheduler{
        private ExecutorService executor;

        public ThreadPoll(){
            executor = Executors.newFixedThreadPool(2);
        }

        @Override
        public void schedule(Runnable runnable) {
            executor.submit(runnable);
        }

        @Override
        public void finished() {
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
