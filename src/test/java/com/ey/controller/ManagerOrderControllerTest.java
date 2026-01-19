package com.ey.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.ey.enums.OrderStatus;
import com.ey.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ManagerOrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@Test
	@WithMockUser(username = "manager", roles = "MANAGER")
	void manager_can_view_orders_by_status() throws Exception {

		when(orderService.getOrdersByStatus(OrderStatus.COMPLETED)).thenReturn(List.of());

		mockMvc.perform(get("/api/manager/orders/status/COMPLETED")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "customer", roles = "CUSTOMER")
	void customer_cannot_access_manager_orders() throws Exception {

		mockMvc.perform(get("/api/manager/orders/status/COMPLETED")).andExpect(status().isForbidden());
	}
}
