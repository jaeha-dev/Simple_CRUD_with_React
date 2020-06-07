package com.github.devsjh.web;

import com.github.devsjh.model.Group;
import com.github.devsjh.model.GroupRepository;
import com.github.devsjh.model.User;
import com.github.devsjh.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @title  : Group 컨트롤러 클래스
 * @author : jaeha-dev (Git)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class GroupController {
    private final Logger log = LoggerFactory.getLogger(GroupController.class);
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    /**
     * 전체 그룹 조회
     * @param principal : 현재 로그인 사용자 이름
     * @return          : 전체 그룹 정보
     */
    @GetMapping("/groups")
    Collection<Group> groups(Principal principal) {
        return groupRepository.findAllByUserId(principal.getName());
    }

    /**
     * 단일 그룹 조회 (그룹 수정 화면에 사용)
     * @param id : 그룹 ID
     * @return   : 단일 그룹 정보
     */
    @GetMapping("/groups/{id}")
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group> group = groupRepository.findById(id);

        return group.map(response -> ResponseEntity.ok().body(response))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 그룹 생성
     * @param group : 그룹 정보
     * @return      : 상태 코드(201 Created) 및 생성된 그룹 정보
     */
    @PostMapping("/groups")
    ResponseEntity<?> createGroup(@Valid @RequestBody Group group,
                                  @AuthenticationPrincipal OAuth2User principal) throws URISyntaxException {
        log.info("Request to create group: {}", group);

        Map<String, Object> details = principal.getAttributes();
        log.info("Login user info {}", details);
        String userId = details.get("sub").toString();

        // Check to see if user already exists
        Optional<User> user = userRepository.findById(userId);
        group.setUser(user.orElse(new User(userId,
                                           details.get("name").toString(),
                                           details.get("email").toString())));

        Group result = groupRepository.save(group);

        log.info("Create result info {}", ResponseEntity.created(new URI("/api/groups/" + result.getId())).body(result));
        // 201 Created: 요청이 성공적으로 수행되었고 새로운 리소스가 생성된다.
        return ResponseEntity.created(new URI("/api/groups/" + result.getId())).body(result);
    }

    /**
     * 그룹 수정
     * @param group : 그룹 정보
     * @return      : 상태 코드(200 Ok) 및 수정된 그룹 정보
     */
    @PutMapping("/groups/{id}")
    ResponseEntity<?> updateGroup(@Valid @RequestBody Group group) {
        log.info("Request to update group: {}", group);
        Group result = groupRepository.save(group);

        log.info("Update result info {}", ResponseEntity.ok().body(result));
        return ResponseEntity.ok().body(result);
    }

    /**
     * 그룹 삭제
     * @param id : 그룹 ID
     * @return   : 상태 코드(200 Ok)
     */
    @DeleteMapping("/groups/{id}")
    ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.info("Request to delete group: {}", id);
        groupRepository.deleteById(id);

        log.info("Delete result info {}", ResponseEntity.ok().build());
        return ResponseEntity.ok().build();
    }
}