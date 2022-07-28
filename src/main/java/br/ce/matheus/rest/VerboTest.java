package br.ce.matheus.rest;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class VerboTest {

	
	@Test
	public void deveSalvarUsuario () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Matheus\",\"age\": 25}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Matheus"))
			.body("age", is(25))
			;
		
	}
	@Test
	public void deveSalvarUsuarioSemNome () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"\",\"age\": 25}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
			;
		
	}
	
	@Test
	public void deveSalvarUsuarioViaXML () {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Mat</name><age>25</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Mat"))
			.body("user.age", is("25"))
			;
		
	}
	@Test
	public void deveAlterarUsuario () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"User Alterado\",\"age\": 33}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("User Alterado"))
			.body("age", is(33))
			.body("salary", is(1234.5678f))
			;
		
	}
	
	@Test
	public void devoCustomizarURL () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"User Alterado\",\"age\": 33}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userID}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("User Alterado"))
			.body("age", is(33))
			.body("salary", is(1234.5678f))
			;
		
	}
	@Test
	public void devoCustomizarURLParte2 () {
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"User Alterado\",\"age\": 33}")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("User Alterado"))
			.body("age", is(33))
			.body("salary", is(1234.5678f))
			;
		
	}
	
	@Test
	public void deveRemoverUsuario () {
		given()
			.log().all()
			.contentType("application/json")
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
			;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
			.contentType("application/json")
		.when()
			.delete("https://restapi.wcaquino.me/users/111")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
			;
	}
	@Test
	public void deveSalvarUsuarioUsandoMap () {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via Map");
		params.put("age", 22);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via Map"))
			.body("age", is(22))
			;
		
	}
	@Test
	public void deveSalvarUsuarioUsandoObject () {
		User user = new User(0, "Usuario via Objeto", 35);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via Objeto"))
			.body("age", is(35))
			;
		
	}
	@Test
	public void deveDeserializarUsuarioUsandoObject () {
		User user = new User(0, "Usuario Deserializado", 35);
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario Deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
	}
}




