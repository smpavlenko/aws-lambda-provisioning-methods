package com.pavlenko.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunctionProcessor
{
    private static final Logger logger = LogManager.getLogger();


    public long process(final S3Event s3Event)
    {
        // TODO to implement
        return 0;
    }
}
