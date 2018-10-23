package com.pavlenko.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FunctionProcessor {
    private static final Logger logger = LogManager.getLogger();
    private final S3DataHandler s3DataHandler = new S3DataHandler(AmazonS3ClientBuilder.standard().build());
    private final AccessLogParser accessLogParser = new AccessLogParser();

    public long process(final S3Event s3Event) {
        final S3EventNotification.S3EventNotificationRecord record = s3Event.getRecords().get(0);
        final String s3Bucket = record.getS3().getBucket().getName();
        final String s3InputKey = record.getS3().getObject().getKey();

        String log = s3DataHandler.retrieve(s3InputKey, s3Bucket);
        List<String> sessionIds = new ArrayList<>(accessLogParser.parse(log));

        if (sessionIds.isEmpty()) {
            return 0;
        }

        logger.info("Read {} session ids", sessionIds.size());
        final String s3OutputKey = "output/" + System.currentTimeMillis() + ".txt";
        s3DataHandler.put(s3OutputKey, s3Bucket, sessionIds);

        return sessionIds.size();
    }
}
