package com.depromeet.booboo.infrastructure.adapter.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.depromeet.booboo.application.adapter.storage.StorageAdapter;
import com.depromeet.booboo.application.adapter.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class AwsS3Adapter implements StorageAdapter {
    private final AmazonS3 s3Client;
    private final String bucketName;

    public AwsS3Adapter(AmazonS3 s3Client,
                        @Value("${aws.s3.bucket.name:defaultBucketName}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String save(MediaType mediaType, InputStream inputStream) {
        String filename = this.getRandomFileName();
        try {
            Bucket bucket = s3Client.listBuckets().stream()
                    .filter(it -> it.getName().equals(bucketName))
                    .findFirst()
                    .orElseThrow(() -> new StorageException("Bucket not found. name:" + bucketName));

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mediaType.toString());
            s3Client.putObject(bucket.getName(), filename, inputStream, metadata);
            return "https://s3.ap-northeast-2.amazonaws.com/" + bucketName + "/" + filename;
        } catch (Exception e) {
            throw new StorageException("Failed to save object. filename:" + filename + ", mediaType:" + mediaType);
        }
    }

    private String getRandomFileName() {
        return UUID.randomUUID().toString();
    }
}