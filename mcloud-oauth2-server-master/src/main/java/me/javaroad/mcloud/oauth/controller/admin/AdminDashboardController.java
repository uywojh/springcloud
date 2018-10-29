package me.javaroad.mcloud.oauth.controller.admin;

import me.javaroad.mcloud.oauth.controller.OAuthConstants;
import me.javaroad.mcloud.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author heyx
 */
@Controller
@RequestMapping(OAuthConstants.ADMIN_PREFIX)
public class AdminDashboardController extends BaseController {

    @GetMapping(value = {"/dashboard", "/", ""})
    public String index() {
        return view("index");
    }
}
