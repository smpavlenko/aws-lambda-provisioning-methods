package com.pavlenko.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LambdaFunction implements RequestHandler<S3Event, Long> {
    private static final Logger logger = LogManager.getLogger();
    private final FunctionProcessor processor = new FunctionProcessor();

    @Override
    public Long handleRequest(S3Event s3Event, Context context) {
        logger.info("Function is invoked");
        return processor.process(s3Event);
    }
}
