package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ConvertWorkflowRule {
    @JsonProperty("ID")
    private String id;
    
    @JsonProperty("Enabled")
    private Boolean enabled;
    
    @JsonProperty("Prefix")
    private String prefix;
    
    @JsonProperty("ExtFilter")
    private WorkflowExtFilter extFilter;
    
    @JsonProperty("Topology")
    private List<List<String>> topology;
    
    @JsonProperty("Operations")
    private WorkflowOperations operations;

    public String getId() {
        return id;
    }

    public ConvertWorkflowRule setId(String id) {
        this.id = id;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public ConvertWorkflowRule setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ConvertWorkflowRule setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public WorkflowExtFilter getExtFilter() {
        return extFilter;
    }

    public ConvertWorkflowRule setExtFilter(WorkflowExtFilter extFilter) {
        this.extFilter = extFilter;
        return this;
    }

    public List<List<String>> getTopology() {
        return topology;
    }

    public ConvertWorkflowRule setTopology(List<List<String>> topology) {
        this.topology = topology;
        return this;
    }

    public WorkflowOperations getOperations() {
        return operations;
    }

    public ConvertWorkflowRule setOperations(WorkflowOperations operations) {
        this.operations = operations;
        return this;
    }

    public static ConvertWorkflowRuleBuilder builder() {
        return new ConvertWorkflowRuleBuilder();
    }

    public static class ConvertWorkflowRuleBuilder {
        private String id;
        private Boolean enabled;
        private String prefix;
        private WorkflowExtFilter extFilter;
        private List<List<String>> topology;
        private WorkflowOperations operations;

        public ConvertWorkflowRuleBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ConvertWorkflowRuleBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ConvertWorkflowRuleBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ConvertWorkflowRuleBuilder extFilter(WorkflowExtFilter extFilter) {
            this.extFilter = extFilter;
            return this;
        }

        public ConvertWorkflowRuleBuilder topology(List<List<String>> topology) {
            this.topology = topology;
            return this;
        }

        public ConvertWorkflowRuleBuilder operations(WorkflowOperations operations) {
            this.operations = operations;
            return this;
        }

        public ConvertWorkflowRule build() {
            ConvertWorkflowRule r = new ConvertWorkflowRule();
            r.setId(id);
            r.setEnabled(enabled);
            r.setPrefix(prefix);
            r.setExtFilter(extFilter);
            r.setTopology(topology);
            r.setOperations(operations);
            return r;
        }
    }

    @Override
    public String toString() {
        return "ConvertWorkflowRule{" +
                "id='" + id + '\'' +
                ", enabled=" + enabled +
                ", prefix='" + prefix + '\'' +
                ", extFilter=" + extFilter +
                ", topology=" + topology +
                ", operations=" + operations +
                '}';
    }
}
