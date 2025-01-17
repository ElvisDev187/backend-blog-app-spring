package com.elvis.blog.services.impl;

import com.elvis.blog.domain.entities.Tag;
import com.elvis.blog.repositories.TagRepository;
import com.elvis.blog.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
      List<Tag> existingTag =  tagRepository.findByNameIn(tagNames);
      Set<String> existingNames = existingTag.stream()
             .map(Tag::getName)
              .collect(Collectors.toSet());
      List<Tag> newTags = tagNames.stream().filter(name -> !existingNames.contains(name))
              .map(name->Tag.builder()
                      .name(name)
                      .posts(new HashSet<>())
                      .build())
              .toList();

      List<Tag> savedTags = new ArrayList<>();
      if(!newTags.isEmpty()) {
        savedTags =  tagRepository.saveAll(newTags);
      }

        savedTags.addAll(existingTag);
      return savedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
            if(!tag.getPosts().isEmpty()) {
                throw new IllegalArgumentException("Cannot delete tag with posts ");
            }
            tagRepository.deleteById(id);
        });
    }
}
