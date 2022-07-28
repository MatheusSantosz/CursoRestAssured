package br.ce.matheus.rest;

import static org.hamcrest.Matchers.*;
import java.io.File;

import org.junit.Test;

import static io.restassured.RestAssured.given;

public class FileTest {
	
	@Test
	public void devoObrigarEnvioDeArquivo() {
		
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //deveria ser 400
			.body("error", is("Arquivo não enviado"))
		;
		
	}

	@Test
	public void devoFazerOUploadDoArquivo() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) 
			.body("name", is("users.pdf"))
			.body("md5", is(notNullValue()))
			.body("size", is(notNullValue()))
		;
		
	}
	@Test
	public void naoDeveFazerUploadDeArquivoGrande() {
		
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/Preparatório CTFL - Slides.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(4000L))
			.statusCode(413)
			;
		
	}
}
