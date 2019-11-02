package com.depromeet.booboo.application.adapter.storage;

import org.springframework.http.MediaType;

import java.io.InputStream;

public interface StorageAdapter {
    String save(MediaType mediaType, InputStream inputStream) throws StorageException;
}
