package com.msc.ms.processor.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserSenderService userSenderService;

    public UserController(final UserSenderService pUserSenderService) {

        userSenderService = pUserSenderService;
    }


    @GetMapping("/triggerUserProccess")
    public ResponseEntity<?> triggerUserProcess() {
        this.userSenderService.proccesUsersFiles();
        return ResponseEntity.ok("success");
    }
}
