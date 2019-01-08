package com.nathan.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.inbound.kinesis.KinesisMessageDrivenChannelAdapter;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.ErrorMessage;

import javax.validation.constraints.NotNull;

import java.io.ByteArrayInputStream;

@Configuration
@EnableIntegration
public class KenesisStreamReciever {

    @Value("${kinesis.user.access.key}")
    @NotNull
    private String accessKey;

    @Value("${kinesis.user.secret.key}")
    @NotNull
    private String secretKey;

    @Value("${kinesis.service.endpoint}")
    @NotNull
    private String serviceEndPoint;

    @Value("${kinesis.signing.region}")
    @NotNull
    private String signingRegion;

    @Bean
    public AmazonKinesis amazonKinesis(){
        AmazonKinesis amazonKinesis;
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        amazonKinesis = AmazonKinesisAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withClientConfiguration(
                        new ClientConfiguration()
                                .withMaxErrorRetry(0)
                                .withConnectionTimeout(1000))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                serviceEndPoint, signingRegion))
                .build();

        //System.clearProperty(SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY);

        return amazonKinesis;
    }


    @Bean
    public PollableChannel kinesisReceiveChannel() {
        QueueChannel queueChannel = new QueueChannel();
        return queueChannel;
    }

    @Bean
    public KinesisMessageDrivenChannelAdapter kinesisInboundChannel(AmazonKinesis amazonKinesis) {
        KinesisMessageDrivenChannelAdapter adapter =
                new KinesisMessageDrivenChannelAdapter(amazonKinesis, "TestStream");
        adapter.setOutputChannel(kinesisReceiveChannel());
        adapter.setConcurrency(10);
        //adapter.setConverter(ByteArrayInputStream::new);
        adapter.setConverter(String::new);
        return adapter;
    }



    @Bean
    public PollableChannel errorChannel() {
        QueueChannel queueChannel = new QueueChannel();
        queueChannel.addInterceptor(new ChannelInterceptorAdapter() {

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                super.postSend(message, channel, sent);

                if (message instanceof ErrorMessage) {
                    throw (RuntimeException) ((ErrorMessage) message).getPayload();
                }
            }
        });
        return queueChannel;
    }
}
