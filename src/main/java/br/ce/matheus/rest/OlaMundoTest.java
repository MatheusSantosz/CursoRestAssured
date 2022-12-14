package br.ce.matheus.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	
	public void testOlaMundo() {
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
		///System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		///System.out.println(response.statusCode()== 200);
		Assert.assertTrue("Status code Deveria ser 200",response.statusCode()== 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	
	public void devoConherOutrasFormasRestAssured() {
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("https://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given() ///Pre Condicoes
		.when() ///Acao
			.get("https://restapi.wcaquino.me/ola")
		.then() ///Assertivas
//			.assertThat()
			.statusCode(200);
			
	}
	
	@Test
	public void devoConhecerMachersHamcrest () {
		
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128d, Matchers.greaterThan(120d));
		Assert.assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, containsInAnyOrder(1,3,5,7,9));
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1, 5));
		
		assertThat("Maria", is(not("Joao")));
		assertThat("Maria", not("Joao"));
		assertThat("Maria", anyOf(is("Joao"), is("Maria")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}


	@Test
	public void devoValidarBody() {
		
		get("https://restapi.wcaquino.me/ola").then().statusCode(200);
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/ola")
		.then() 
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!")) ///do mais restritivo ao menos restritivo
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
		
	
	}
		}