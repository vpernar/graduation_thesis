package rs.raf.orchestrationservice.service;

public interface WorkflowStep {

    WorkflowStepStatus getStatus();
    boolean process();
    boolean revert();

}
