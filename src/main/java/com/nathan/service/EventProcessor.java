package com.nathan.service;

import com.nathan.compressor.Compressor;

import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class EventProcessor {
    @ServiceActivator(inputChannel = "kinesisReceiveChannel", poller = {@Poller(fixedRate = "1000")} )
    public void onEvent(String event) {
        System.out.println("Event Recieved by thread id : " + Thread.currentThread().getId());
        System.out.println("event: " + event);

    }

}
