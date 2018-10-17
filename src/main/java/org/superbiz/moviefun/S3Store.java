package org.superbiz.moviefun;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class S3Store implements BlobStore {
    private final AmazonS3Client s3Client;
    private final String photoStorageBucket;
    private final Tika tika = new Tika();

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        String key = blob.getName();

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                photoStorageBucket,
                key,
                blob.getInputStream(),
                null
        );

        s3Client.putObject(putObjectRequest);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(
                photoStorageBucket,
                name
        );

        S3Object s3Object = null;
        try {
            s3Object = s3Client.getObject(getObjectRequest);
        } catch (AmazonS3Exception e) {
            return Optional.empty();
        }

        InputStream objectContent = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectContent);

        String contentType = tika.detect(objectContent);

        Blob blob = new Blob(
                name,
                new ByteArrayInputStream(bytes),
                contentType
        );

        return Optional.of(blob);
    }

    @Override
    public void deleteAll() {

    }
}
