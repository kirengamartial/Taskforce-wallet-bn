package com.wallet.wallet.service;

import com.wallet.wallet.dto.CategoryDTO;
import com.wallet.wallet.entity.Category;
import com.wallet.wallet.exception.ResourceNotFoundException;
import com.wallet.wallet.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        if (categoryDTO.getParentId() != null) {
            category.setParent(categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found")));
        }

        category = categoryRepository.save(category);
        return convertToDTO(category);
    }

    public List<CategoryDTO> getSubcategories(Long parentId) {
        return categoryRepository.findByParentId(parentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        return dto;
    }
}
