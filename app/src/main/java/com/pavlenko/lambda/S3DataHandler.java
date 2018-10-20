package com.pavlenko.lambda;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class S3DataHandler
{
    private static final Logger logger = LogManager.getLogger();
    private final AmazonS3 amazonS3;


    public S3DataHandler(final AmazonS3 amazonS3)
    {
        this.amazonS3 = amazonS3;
    }


    public String retrieve(
        final String s3Key,
        final String s3Bucket)
    {
        logger.info("Retrieving objects from {}/{}", s3Bucket, s3Key);
        final S3Object object = amazonS3.getObject(new GetObjectRequest(s3Bucket, s3Key));
        if (object == null)
        {
            return null;
        }
        try (final InputStream objectData = object.getObjectContent())
        {
            return IOUtils.toString(objectData);
        }
        catch (final IOException ex)
        {
            logger.error("Failed to read log file" + ex.getMessage(), ex);
        }
        return null;
    }


    public void put(
        final String s3Key,
        final String s3Bucket, List<String> sessionIds)
    {
        logger.info("Putting objects to {}/{}", s3Bucket, s3Key);
        amazonS3.putObject(s3Bucket, s3Key, String.join(System.getProperty("line.separator"), sessionIds));
    }


}
