package com.financialapplication.expansesanalysis.Service;

import com.financialapplication.expansesanalysis.Model.Entity.Category;
import com.financialapplication.expansesanalysis.Model.Response.CategoryResponse;
import com.financialapplication.expansesanalysis.Repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<CategoryResponse> getCategoryAmountsByMobileNo(String mobileNo) {


        // Validate input
        if (mobileNo == null || mobileNo.trim().isEmpty()) {
            logger.warning("Invalid mobile number provided.");
            return new ResponseEntity<>(
                    new CategoryResponse(null, "Mobile number cannot be null or empty.", false),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Fetch category
        Optional<Category> category = categoryRepository.findCategoryByMobileNo(mobileNo);

        if (category.isPresent()) {
            logger.info("Category found for mobile number: " + mobileNo);
            return new ResponseEntity<>(
                    new CategoryResponse(category.get(), "Category found successfully.", true),
                    HttpStatus.OK
            );
        } else {
            logger.warning("No category found for mobile number: " + mobileNo);
            return new ResponseEntity<>(
                    new CategoryResponse(null, "No category found for the provided mobile number.", false),
                    HttpStatus.NO_CONTENT
            );
        }
    }
}