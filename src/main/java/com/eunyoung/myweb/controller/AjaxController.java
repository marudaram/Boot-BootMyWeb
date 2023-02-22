package com.eunyoung.myweb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eunyoung.myweb.command.CategoryVO;
import com.eunyoung.myweb.command.ProductUploadVO;
import com.eunyoung.myweb.command.ProductVO;
import com.eunyoung.myweb.product.service.ProductService;

@RestController
public class AjaxController {

	@Value("${project.uploadpath}")
	private String uploadpath;
	
	
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
	
	
	//이미지정보를 처리
	//1. ?키=값
	//2. PathVariable
	//화면에는 2진데이터 타입이 반환된다
//	@GetMapping("/display/{filepath}/{uuid}/{filename}")
//	public byte[] display(@PathVariable("filepath") String filepath,
//						  @PathVariable("uuid") String uuid,
//						  @PathVariable("filename") String filename) {
//		
//		String savename = uploadpath+ "\\" + filepath + "\\" + uuid + "_" + filename;
//		
//		File file = new File(savename);
//		//저장된 이미지파일의 이진데이터 형식을 구함
//		byte[] result = null;
//		try {
//			result = FileCopyUtils.copyToByteArray(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return result;
//	}
	
	
	//ResponseEntity - 응답본문을 직접 작성
	@GetMapping("/display/{filepath}/{uuid}/{filename}")
	public ResponseEntity<byte[]> display(@PathVariable("filepath") String filepath,
						  @PathVariable("uuid") String uuid,
						  @PathVariable("filename") String filename) {
		
		String savename = uploadpath+ "\\" + filepath + "\\" + uuid + "_" + filename;
		
		File file = new File(savename);
		//저장된 이미지파일의 이진데이터 형식을 구함
		
		byte[] result = null; //1. data
		ResponseEntity<byte[]> entity = null;
		try {
			result = FileCopyUtils.copyToByteArray(file);
			
			//2. header
			HttpHeaders header = new HttpHeaders();
			header.add("Content-type", Files.probeContentType(file.toPath())); //파일의 컨텐츠타입을 직접 구해서 header에 저장
			
			//3. 상태값
			entity = new ResponseEntity<>(result, header, HttpStatus.OK);//데이터, 헤더, 상태값(성공or실패)
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return entity;
	}

	
	//prod_id값을 받아서 이미지정보를 반환 (함수의 모형을 선언)
	@PostMapping("/getProductImg")
	public ResponseEntity<List<ProductUploadVO>> getProductImg(@RequestBody ProductVO vo) {
		
		return new ResponseEntity<>(productService.getProductImg(vo), HttpStatus.OK);	}
	
	
	//다운로드기능 
	@GetMapping("/download/{filepath}/{uuid}/{filename}")
	public ResponseEntity<byte[]> download(@PathVariable("filepath") String filepath,
						  @PathVariable("uuid") String uuid,
						  @PathVariable("filename") String filename) {
		
		String savename = uploadpath+ "\\" + filepath + "\\" + uuid + "_" + filename;
		
		File file = new File(savename);
		//저장된 이미지파일의 이진데이터 형식을 구함
		
		byte[] result = null; //1. data
		ResponseEntity<byte[]> entity = null;
		try {
			result = FileCopyUtils.copyToByteArray(file);
			
			//2. header
			HttpHeaders header = new HttpHeaders();
			
			//다운로드임을 명시
			header.add("Content-Disposition", "attachment; filename=" + filename ); //파일의 컨텐츠타입을 직접 구해서 header에 저장
			
			//3. 상태값
			entity = new ResponseEntity<>(result, header, HttpStatus.OK);//데이터, 헤더, 상태값(성공or실패)
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return entity;
	}
	
	
	
}
	
	
	
	
	

