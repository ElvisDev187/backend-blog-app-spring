package com.elvis.blog.mappers;

import com.elvis.blog.domain.PostStatus;
import com.elvis.blog.domain.dtos.TagResponse;
import com.elvis.blog.domain.entities.Post;
import com.elvis.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse tagToTagResponse(Tag tag);

    @Named("calculatePostCount")
  default Integer calculatePostCount(Set<Post> posts) {
       if (posts == null || posts.isEmpty()) {
           return 0;
       }

         return  (int) posts.stream().filter(post-> PostStatus.PUBLISHED.equals(post.getStatus())).count();

    }
}