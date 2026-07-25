package com.trackwise.classification.controller;

import com.trackwise.classification.dto.CreateTagRequest;
import com.trackwise.classification.dto.TagResponse;
import com.trackwise.classification.service.ClassificationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final ClassificationService classificationService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getTags(@AuthenticationPrincipal UserDetails userDetails) {
        List<TagResponse> tags = classificationService.getTags(userDetails.getUsername());
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateTagRequest request
    ) {
        TagResponse tag = classificationService.createTag(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tag);
    }
}
