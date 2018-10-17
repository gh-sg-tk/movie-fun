package org.superbiz.moviefun;

import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.nio.file.Files.newInputStream;

@Repository
public class FileStore implements BlobStore {
    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.getName());
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.getInputStream(), outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File coverFile = new File(name);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            try {
                coverFilePath = Paths.get(getSystemResource("default-cover.jpg").toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        InputStream inputStream = newInputStream(coverFilePath);
        String contentType = new Tika().detect(coverFilePath);

        Blob blob = new Blob(
                name,
                inputStream,
                contentType
        );

        return Optional.of(blob);
    }

    @Override
    public void deleteAll() {

    }
}
