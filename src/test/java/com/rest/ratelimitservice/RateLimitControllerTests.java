package com.rest.ratelimitservice;

import com.alibaba.fastjson.JSON;
import com.rest.ratelimitservice.controller.RateLimitController;
import com.rest.ratelimitservice.dto.HelloDTO;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RateLimitController.class)
public class RateLimitControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @org.junit.Test
    public void basicApiTest() throws Exception {
        RequestBuilder requestBuilder= MockMvcRequestBuilders.get("/rateLimitRestApi");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        HelloDTO res=JSON.parseObject(result.getResponse().getContentAsString(),HelloDTO.class);
        assertEquals(res.getMsg(),"hello");
    }
}
