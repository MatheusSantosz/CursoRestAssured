package br.ce.matheus.rest;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

public class EnvioDadosTest {
	
	@Test
	public void deveEnviarDadosQueryXml() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=xml")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			;	
		
	}
	
	@Test
	public void deveEnviarDadosQueryJson() {
		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
			;	
		
	}

	@Test
	public void deveEnviarDadosQueryParam() {
		
		given()
			.log().all()
			.queryParam("format", "xml")
			.queryParam("outra", "coisa")
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			.contentType(containsString("utf-8"))
			;	
		
	}

	@Test
	public void deveEnviarDadosHeader() {
		
		given()
			.log().all()
			.contentType(ContentType.XML)
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
			;	
		
	}
}
