package br.ce.matheus.rest;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

import static io.restassured.RestAssured.given;

public class AuthTest {
	@Test
	public void deveAcessarSWAPI() {
		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1/")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
			;	
	}


//  https://api.openweathermap.org/data/2.5/weather?q=Santos,BR&appid=3ccc459320d5499416466f0e4b544022&units=metric

// 3ccc459320d5499416466f0e4b544022

	@Test

	public void deveObterClima() {
	
	given()
		.log().all()
		.queryParam("q", "Santos,BR")
		.queryParam("appid", "3ccc459320d5499416466f0e4b544022")
		.queryParam("units", "metric")
	.when()
		.get("https://api.openweathermap.org/data/2.5/weather")
	.then()
		.log().all()
		.statusCode(200)
		.body("name", is("Santos"))
		.body("main.temp", greaterThan(22f))
		.body("sys.country", is("BR"))
		;	
	}
	@Test

	public void naoDeveAcessarSemSenha() {
	
		given()
			.log().all()
		.when()
		.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
			;	
	}
	@Test
	public void DeveAuthBasica() {
		given()
			.log().all()
		.when()
		.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			;	
	}
	@Test
	public void DeveAuthBasica2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
		.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			;	
	}
	@Test
	public void DeveAuthBasicaChallenger() {
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
		.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
			;	
	}
	
	@Test
	public void DeveAuthBasicaComTokenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "matheus13@gmail.com");
		login.put("senha", "123456");
		
		//login na api
		//receber o token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("matheus"))
			.extract().path("token");
			;	
	
		//ObterContas
		given()
			.log().all()
			.header("Authorization", "JWT " + token)

		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta de Teste"))
			;	
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() {
		//login
		
		String cookie = given()
		.log().all()
		.formParam("email", "matheus13@gmail.com")
		.formParam("senha", "123456")
		.contentType(ContentType.URLENC.withCharset("UTF-8"))
	.when()
		.post("https://seubarriga.wcaquino.me/logar")
	.then()
		.log().all()
		.statusCode(200)
		.extract().header("set-cookie");
		;	
		
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
		
		//tela de contas
		String body = given()
		.log().all()
		.cookie("connect.sid", cookie)
	.when()
		.get("https://seubarriga.wcaquino.me/contas")
	.then()
		.log().all()
		.statusCode(200)
		.body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste"))
		.extract().body().asString();
		;
		System.out.println("___________");
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}