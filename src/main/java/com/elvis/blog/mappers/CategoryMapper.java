package com.elvis.blog.mappers;

import com.elvis.blog.domain.PostStatus;
import com.elvis.blog.domain.dtos.CategoryDto;
import com.elvis.blog.domain.dtos.CreateCategoryRequest;
import com.elvis.blog.domain.entities.Category;
import com.elvis.blog.domain.entities.Post;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    
    @Mapping(target = "postCount", source="posts", qualifiedByName="calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named(value = "calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return 0;
        }

       return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();

    }
}
