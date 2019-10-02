package spring_rest_security;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean(reset = MockReset.BEFORE)
	private BookRepository mockRepository;

	@Test
	public void save_emptyAuthor_emptyPrice_400() throws Exception {

		String bookInJson = "{\"name\":\"ABC\"}";

		mockMvc.perform(post("/books").content(bookInJson)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))

				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(3)))
				.andExpect(jsonPath("$.errors", hasItem("Author is not allowed")))
				.andExpect(jsonPath("$.errors", hasItem("Please provide an author")))
				.andExpect(jsonPath("$.errors", hasItem("Please provide a price")));

		verify(mockRepository, times(0)).save(any(Book.class));
	}

	@Test
	public void save_invalidAuthor_400() throws Exception {

		String bookInJson = "{\"name\":\" Spring REST tutorials\", \"author\":\"abc\",\"price\":\"9.99\"}";

		mockMvc.perform(post("/books").content(bookInJson)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))

				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.errors").isArray())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors", hasItem("Author is not allowed")));

		verify(mockRepository, times(0)).save(any(Book.class));

	}
}
