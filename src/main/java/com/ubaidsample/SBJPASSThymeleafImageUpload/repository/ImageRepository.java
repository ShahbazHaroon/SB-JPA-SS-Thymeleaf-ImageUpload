package com.ubaidsample.SBJPASSThymeleafImageUpload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ubaidsample.SBJPASSThymeleafImageUpload.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

	Image findByName(String text);

	Image findFirstByName(String text);

}
