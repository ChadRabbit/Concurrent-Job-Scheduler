package scheduler;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Job {

    private final String id;
    private final Runnable task;
    private final AtomicReference<JobStatus> status;

    public Job(Runnable task) {
        this.id = UUID.randomUUID().toString();
        this.task = task;
        this.status = new AtomicReference<>(JobStatus.PENDING);
    }

    public String getId() {
        return id;
    }

    public Runnable getTask() {
        return task;
    }

    public JobStatus getStatus() {
        return status.get();
    }

    public void setStatus(JobStatus newStatus) {
        status.set(newStatus);
    }
}