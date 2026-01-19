package com.ey.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ey.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	/* ================= CUSTOMER ================= */

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_can_view_own_orders() throws Exception {

		when(orderService.getOrders("customer")).thenReturn(List.of());

		mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_can_place_order() throws Exception {

		when(orderService.checkout("customer")).thenReturn(null);

		mockMvc.perform(post("/api/orders")).andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_can_cancel_order() throws Exception {

		when(orderService.cancelOrder("customer", 1L)).thenReturn(null);

		mockMvc.perform(post("/api/orders/1/cancel")).andExpect(status().isOk());
	}

	/* ================= SECURITY ================= */

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void admin_cannot_access_customer_orders() throws Exception {

		mockMvc.perform(get("/api/orders")).andExpect(status().isForbidden());
	}
}
