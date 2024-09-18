package com.fajaralfa.threeapprover;

import EnhydraShark.App;
import org.hibernate.jdbc.Work;
import org.joget.apps.app.service.AppService;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.workflow.model.WorkflowAssignment;
import org.joget.workflow.model.service.WorkflowManager;

import java.util.HashMap;
import java.util.Map;

public class ThreeApproverMapper extends DefaultApplicationPlugin {

    @Override
    public String getName() {
        return "Three Approver Mapper";
    }

    @Override
    public String getVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public String getDescription() {
        return "Map Three Approver to Workflow Variable";
    }

    @Override
    public Object execute(Map map) {
        AppService appService = (AppService) AppUtil.getApplicationContext().getBean("appService");
        WorkflowManager workflowManager = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");

        WorkflowAssignment workflowAssignment = (WorkflowAssignment) map.get("workflowAssignment");

        LogUtil.info(getClassName(), "workflowManager " + workflowManager);
        LogUtil.info(getClassName(), "workflowAssignment " + workflowAssignment);

        String processId = workflowAssignment.getProcessId();
        String primaryKey = appService.getOriginProcessId(processId);

        ThreeApproverMapperDAO dao = new ThreeApproverMapperDAO();

        // get requester username
        String requesterUsername = dao.getRequesterUsername(primaryKey);
        LogUtil.info(getClassName(), "Username: " + requesterUsername);
        // query, get approvers by requester
        HashMap<String, String> threeApprovers = dao.getThreeApprovers(requesterUsername);
        // store result to workflow variable
        threeApprovers.forEach((k, v) -> {
            LogUtil.info(getClassName(), "Key: " + k);
            LogUtil.info(getClassName(), "Val: " + v);
            workflowManager.activityVariable(workflowAssignment.getActivityId(), k, v);
        });

        return null;
    }

    @Override
    public String getLabel() {
        return "Three Approver Mapper";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return "";
    }
}
