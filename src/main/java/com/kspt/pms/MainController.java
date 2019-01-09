package com.kspt.pms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by kivi on 06.12.17.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String homepage() {
        return "homepage";
    }

}
