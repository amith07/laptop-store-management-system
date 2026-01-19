package com.ey.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ey.service.CartService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_can_view_cart() throws Exception {

		mockMvc.perform(get("/api/cart")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void admin_cannot_view_cart() throws Exception {

		mockMvc.perform(get("/api/cart")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_can_add_to_cart() throws Exception {

		mockMvc.perform(post("/api/cart/items").contentType(MediaType.APPLICATION_JSON).content("""
				    {
				      "laptopId": 1,
				      "quantity": 1
				    }
				""")).andExpect(status().isOk());
	}
}
