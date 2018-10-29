package me.javaroad.mcloud.oauth.controller.adminapi;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import me.javaroad.mcloud.oauth.controller.OAuthConstants;
import me.javaroad.mcloud.oauth.dto.request.ApprovalRequest;
import me.javaroad.mcloud.oauth.dto.response.ApprovalResponse;
import me.javaroad.mcloud.oauth.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heyx
 */
@RestController
@RequestMapping(OAuthConstants.ADMIN_API_PREFIX + "/approvals")
public class AdminApprovalApi {

    private final ApprovalService approvalService;

    @Autowired
    public AdminApprovalApi(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @ApiOperation(value = "Create Approval", httpMethod = "POST")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApprovalResponse createApproval(@RequestBody @Valid ApprovalRequest approvalRequest) {
        return approvalService.create(approvalRequest);
    }

    @ApiOperation(value = "Get All Approval", httpMethod = "GET")
    @GetMapping
    public List<ApprovalResponse> getAllApproval() {
        return approvalService.getAll();
    }

    @ApiOperation(value = "Get Approval By ID", httpMethod = "GET")
    @GetMapping("{approvalId}")
    public ApprovalResponse getApproval(@PathVariable Long approvalId) {
        return approvalService.getResponse(approvalId);
    }

    @ApiOperation(value = "Delete Approval", httpMethod = "DELETE")
    @DeleteMapping("{approvalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApproval(@PathVariable Long approvalId) {
        approvalService.delete(approvalId);
    }


}
