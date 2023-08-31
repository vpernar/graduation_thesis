package rs.raf.orchestrationservice.service;

import java.util.List;

public class OrderWorkFlow implements Workflow {

    private final List<WorkflowStep> steps;

    public OrderWorkFlow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public List<WorkflowStep> getSteps() {
        return this.steps;
    }

}
