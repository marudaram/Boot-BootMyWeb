package com.eunyoung.myweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eunyoung.myweb.command.CategoryVO;
import com.eunyoung.myweb.product.service.ProductService;

@RestController
public class AjaxController {

	@Autowired
	private ProductService productService;
	
	
	//대분류 카테고리 요청
	@GetMapping("/getCategory")
	public List<CategoryVO> getCategory() { //무조건 1번이 나오므로 매개변수는 없음
		
		
		
		return productService.getCategory();
	}
	
	//중분류 소분류 카테고리 요청
	@GetMapping("/getCategoryChild/{group_id}/{category_lv}/{category_detail_lv}")
	public List<CategoryVO> getCategoryChild(@PathVariable("group_id") String group_id,
											 @PathVariable("category_lv") int category_lv,
											 @PathVariable("category_detail_lv") int category_detail_lv) {
		
		
		CategoryVO vo = CategoryVO.builder()
								  .group_id(group_id)
								  .category_lv(category_lv)
								  .category_detail_lv(category_detail_lv)
								  .build();
		
		
		return productService.getCategoryChild(vo);
	}
	
}
