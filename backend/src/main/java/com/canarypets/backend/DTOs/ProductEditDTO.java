package com.canarypets.backend.DTOs;

import com.canarypets.backend.models.Category;
import com.canarypets.backend.models.Product;
import com.canarypets.backend.models.Tag;

import java.util.List;
import java.util.Set;

public class ProductEditDTO {

    private Product product;
    private List<Category> parentCategories;
    private List<Category> allCategories;
    private List<Tag> allTags;
    private Set<Long> selectedTagIds;
    private Long selectedParentId;

    public ProductEditDTO(Product product, List<Category> parentCategories, List<Category> allCategories, List<Tag> allTags, Set<Long> selectedTagIds, Long selectedParentId) {
        this.product = product;
        this.parentCategories = parentCategories;
        this.allCategories = allCategories;
        this.allTags = allTags;
        this.selectedTagIds = selectedTagIds;
        this.selectedParentId = selectedParentId;
    }

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}

    public List<Category> getParentCategories() {return parentCategories;}
    public void setParentCategories(List<Category> parentCategories) {this.parentCategories = parentCategories;}

    public List<Category> getAllCategories() {return allCategories;}
    public void setAllCategories(List<Category> allCategories) {this.allCategories = allCategories;}

    public List<Tag> getAllTags() {return allTags;}
    public void setAllTags(List<Tag> allTags) {this.allTags = allTags;}

    public Set<Long> getSelectedTagIds() {return selectedTagIds;}
    public void setSelectedTagIds(Set<Long> selectedTagIds) {this.selectedTagIds = selectedTagIds;}

    public Long getSelectedParentId() {return selectedParentId;}
    public void setSelectedParentId(Long selectedParentId) {this.selectedParentId = selectedParentId;}
}