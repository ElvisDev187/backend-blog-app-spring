package com.elvis.blog.controllers;

import com.elvis.blog.domain.dtos.CreateTagRequest;
import com.elvis.blog.domain.dtos.TagResponse;
import com.elvis.blog.domain.entities.Tag;
import com.elvis.blog.mappers.TagMapper;
import com.elvis.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
      List<TagResponse> tagResponses=  tags.stream().map(tagMapper::tagToTagResponse).toList();

      return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@Valid @RequestBody CreateTagRequest createTagRequest) {
        List<Tag> savedTags = tagService.createTags(createTagRequest.getNames());
        List<TagResponse> tagResponses = savedTags.stream().map(tagMapper::tagToTagResponse).toList();

        return new ResponseEntity<>(tagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
