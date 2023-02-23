package com.eunyoung.myweb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component("kakao")
public class KakaoAPI {

	//토큰발급기능
	public String getAccessToken(String code) {

		String requestURL = "https://kauth.kakao.com/oauth/token";
		String redirect_url = "http://127.0.0.1:8484/user/kakao";

		String access_token = "";
		String refresh_token = "";

		//post의 폼데이터 형식 -> 키=값&키=값...
		String data = "grant_type=authorization_code"
				+ "&client_id=21ef6c5263279db8c7e0cb51948565dd"
				+ "&redirect_uri=" + redirect_url
				+ "&code=" + code;


		try {
			URL url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST"); //post형식
			conn.setDoOutput(true); //카카오서버로부터 데이터 응답을 허용

			//데이터전송을 위한 클래스
			//			OutputStream out = conn.getOutputStream();
			//			OutputStreamWriter osw = new OutputStreamWriter(out);
			//			BufferedWriter br = new BufferedWriter(osw);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			bw.write(data);
			bw.flush();

			//응답결과를 conn객체에서 받음
			System.out.println(conn.getResponseCode());

			if(conn.getResponseCode() == 200) { //요청성공

				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String result = "";
				String str = null;
				while((str = br.readLine()) != null) { //한줄씩 읽음 - 읽을값이 없으면 null
					result += str;
				}

				System.out.println(result);

				//제이슨 데이터 분해
				JsonParser json = new JsonParser(); //com.google.~
				JsonElement element = json.parse(result); // json데이터 전달
				JsonObject obj =  element.getAsJsonObject(); //json오브젝트로 형변환
				access_token = obj.get("access_token").getAsString();
				refresh_token = obj.get("refresh_token").getAsString();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		return access_token;
	}


	//토큰기반으로 유저정보 얻기
	public Map<String, Object> getUserInfo(String access_token) {

		Map<String, Object> map = new HashMap<>();
		
		String requestURL = "https://kapi.kakao.com/v2/user/me";
		
		try {
			URL url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod("POST"); //post형식
			conn.setDoOutput(true); //카카오서버로부터 데이터 응답을 허용

			//헤더에 토큰정보 추가
			conn.setRequestProperty("Authorization", "Bearer " + access_token);
			
			System.out.println("토큰요청정보: " + conn.getResponseCode());
			
			if(conn.getResponseCode() == 200) {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String result = "";
				String str = null;
				while((str = br.readLine()) != null) { //한줄씩 읽음 - 읽을값이 없으면 null
					result += str;
				}
				
				System.out.println(result);
				
		        JsonParser json = new JsonParser();
		        JsonElement element = json.parse(result);
		        
		        //json에서 key를 꺼내고, 다시 key를 꺼낸다.
		        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
		        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
		        
		        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
		        String email = kakao_account.getAsJsonObject().get("email").getAsString();
		        
		        map.put("nickname", nickname);
		        map.put("email", email);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}


}
