package br.ce.matheus.rest;

import static org.hamcrest.Matchers.*;


import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;

public class userXmlTest {

	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		//RestAssured.port = 443;
		//RestAssured.basePath = "/v2";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
	}
	
	@Test
	public void xml () {
		
		given()
			.spec(reqSpec)
		.when()
			.get("/usersXML/3")
		.then()
		.spec(resSpec)
			.body("user.name", is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()", is(2))
			.body("user.filhos.name[0]", is("Zezinho"))
			.body("user.filhos.name[1]", is("Luizinho"))
			.body("user.filhos.name", hasItem("Zezinho"))
			.body("user.filhos.name", hasItems("Luizinho", "Zezinho"));			
	}
	@Test
	public void xmlRaiz() {
		given()
			.spec(reqSpec)
		.when()
			.get("/usersXML/3")
		.then()
			.spec(resSpec)
			.rootPath("user")
			.body("filhos.name.size()", is(2))
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Luizinho", "Zezinho"))
			
			.rootPath("user.filhos")
			.body("name.size()", is(2))
			
			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))
			
			.appendRootPath("filhos")
			.body("name", hasItem("Zezinho"))
			.body("name", hasItems("Luizinho", "Zezinho"));	
					
	}
	@Test
	public void devoFazerPesquisasAvancadasComXml() {
		given()
		.spec(reqSpec)
		.when()
			.get("/usersXML")
		.then()
			.spec(resSpec)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1", "2", "3"))
			.body("users.user.find{it.age ==25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it !=null}", is("1234.5678"))
			.body("users.user.salary.find{it !=null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))
			;	
					
	}
	@Test
	public void devoFazerPesquisasAvancadasComXmlEJava() {
		Object path = given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}")
			;	
		System.out.println(path.toString());
					
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXPath() {
		 given()
		 .spec(reqSpec)
		.when()
			.get("/usersXML")
		.then()
			.spec(resSpec)
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id = '1']"))
			.body(hasXPath("//user[@id = '1']"))
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", containsString("Ana Julia")))
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("//name", is("João da Silva")))
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			.body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))

			;
					
	}
	
	
}
