package com.example.partner.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.partner.demo.service.PostService;

@RestController
@RequestMapping("/api")
public class PaymentController {

	@Autowired
    private PostService service;
	
	@RequestMapping(value = "/payment", method =  RequestMethod.POST)
    public String post() throws Exception
    {
        return service.sendMessageToAWS();
    }
}
