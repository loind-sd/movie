package com.example.aws_lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class ImageResizeHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context context) {

        event.getRecords().forEach(record -> {
            String bucket = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getUrlDecodedKey();

            context.getLogger().log("Bucket: " + bucket);
            context.getLogger().log("Key: " + key);

            // TODO: download file
            // TODO: resize
            // TODO: upload lại vào resized/
        });

        return "Done";
    }
}
