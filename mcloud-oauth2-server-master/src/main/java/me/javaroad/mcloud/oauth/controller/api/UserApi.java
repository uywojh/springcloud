package me.javaroad.mcloud.oauth.controller.api;

import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Objects;
import javax.validation.Valid;
import me.javaroad.common.exception.UnauthorizedException;
import me.javaroad.mcloud.oauth.controller.OAuthConstants;
import me.javaroad.mcloud.oauth.dto.response.UserResponse;
import me.javaroad.mcloud.oauth.dto.request.UserRequest;
import me.javaroad.mcloud.oauth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heyx
 */
@RestController
@RequestMapping(OAuthConstants.API_PREFIX + "/users")
public class UserApi {

    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get current user", httpMethod = "POST")
    @PostMapping("me")
    public UserResponse me(Principal principal) {
        if (Objects.isNull(principal) || StringUtils.isBlank(principal.getName())) {
            throw new UnauthorizedException("unauthorized");
        }
        return userService.getResponse(principal.getName());
    }

    @ApiOperation(value = "register", httpMethod = "POST")
    @PostMapping("register")
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.register(userRequest);
    }

}
