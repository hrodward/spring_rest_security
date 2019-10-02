package spring_rest_security;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class BookControllerRestTemplateTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean(reset = MockReset.BEFORE)
	private BookRepository mockRepository;

	/*
	 * { "timestamp":"2019-03-05T09:34:13.280+0000", "status":400,
	 * "errors":["Author is not allowed.","Please provide a price"
	 * ,"Please provide a author"] }
	 */
	@Test
	public void save_emptyAuthor_emptyPrice_400() throws JSONException {

		String bookInJson = "{\"name\":\"ABC\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(bookInJson, headers);

		// send json with POST
		ResponseEntity<String> response = restTemplate.postForEntity("/books", entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Author is not allowed\",\"Please provide a price\",\"Please provide an author\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Book.class));

	}

	/*
	 * { "timestamp":"2019-03-05T09:34:13.207+0000", "status":400,
	 * "errors":["Author is not allowed."] }
	 */
	@Test
	public void save_invalidAuthor_400() throws JSONException {

		String bookInJson = "{\"name\":\" Spring REST tutorials\", \"author\":\"abc\",\"price\":\"9.99\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(bookInJson, headers);

		// Try exchange
		ResponseEntity<String> response = restTemplate.exchange("/books", HttpMethod.POST, entity, String.class);

		String expectedJson = "{\"status\":400,\"errors\":[\"Author is not allowed\"]}";
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(mockRepository, times(0)).save(any(Book.class));

	}

}
