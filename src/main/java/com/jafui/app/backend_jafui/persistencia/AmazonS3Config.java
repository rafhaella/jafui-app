package com.jafui.app.backend_jafui.persistencia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Value("${amazon.s3.region}")
    private String amazonAWSRegion;

    @Bean
    public AmazonS3 amazonS3Client() {

    AWSCredentials amazonAWSCredentialsS3 = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    return AmazonS3ClientBuilder.standard().withRegion(amazonAWSRegion).withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentialsS3)).build();
        

    }
    
}
