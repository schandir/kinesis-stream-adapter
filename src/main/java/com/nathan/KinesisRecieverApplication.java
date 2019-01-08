package com.nathan;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.ListStreamsResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KinesisRecieverApplication implements CommandLineRunner {

    private static Log logger = LogFactory.getLog(KinesisRecieverApplication.class);

    @Autowired
    private AmazonKinesis amazonKinesis;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KinesisRecieverApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            ListStreamsResult listStreamsResult = this.amazonKinesis.listStreams();
            System.out.println(listStreamsResult.getStreamNames());
        } catch (SdkClientException e) {
            logger.warn("Exception Encountered ...", e);
        }
    }
}