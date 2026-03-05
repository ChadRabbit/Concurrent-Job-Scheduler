package scheduler;

import java.util.concurrent.*;

public class JobScheduler {

    private final BlockingQueue<Job> queue;
    private final ExecutorService workerPool;
    private volatile boolean isRunning = true;

    public JobScheduler(int workerCount) {
        this.queue = new LinkedBlockingQueue<>();
        this.workerPool = Executors.newFixedThreadPool(workerCount);

        startWorkers(workerCount);
    }

    private void startWorkers(int workerCount) {
        for (int i = 0; i < workerCount; i++) {
            workerPool.submit(this::workerLoop);
        }
    }

    private void workerLoop() {
        while (isRunning) {
            try {
                Job job = queue.take(); // blocks until job available
                executeJob(job);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void executeJob(Job job) {
        job.setStatus(JobStatus.RUNNING);
        log("Starting job " + job.getId());

        try {
            job.getTask().run();

            job.setStatus(JobStatus.SUCCESS);
            log("Completed job " + job.getId());

        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            log("Job failed " + job.getId() + " : " + e.getMessage());
        }
    }

    public void submit(Job job) {
        if (!isRunning) {
            throw new IllegalStateException("Scheduler is shutting down");
        }

        queue.offer(job);
        log("Submitted job " + job.getId());
    }

    public void shutdown() {
        isRunning = false;
        workerPool.shutdownNow();
        log("Scheduler shutdown initiated");
    }

    private void log(String message) {
        System.out.println("[Scheduler] " + message);
    }
}