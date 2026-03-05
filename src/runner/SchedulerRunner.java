package runner;

import scheduler.Job;
import scheduler.JobScheduler;

public class SchedulerRunner {

    public static void main(String[] args) throws InterruptedException {

        JobScheduler scheduler = new JobScheduler(3);

        for (int i = 0; i < 10; i++) {
            int jobNumber = i;

            Job job = new Job(() -> {
                System.out.println("Executing job " + jobNumber);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            });

            scheduler.submit(job);
        }

        Thread.sleep(5000);
        scheduler.shutdown();
    }
}