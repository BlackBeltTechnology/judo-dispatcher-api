package hu.blackbelt.judo.dispatcher.api;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class FileType {

    private String stringValue;

    private Map<String, Object> data;

    public FileType(final String stringValue) {
        requireNonNull(stringValue, "Missing value");
        this.stringValue = stringValue;
    }

    public FileType(final Map<String, Object> value) {
        data = value;
    }

    public Map<String, Object> getData() {
        if (data == null) {
            data = new Gson().fromJson(stringValue, Map.class);
        }
        return Collections.unmodifiableMap(data);
    }

    public String getId() {
        return String.valueOf(getData().get(Part.FILE_ID.getKey()));
    }

    public String getFileName() {
        return (String) getData().get(Part.FILE_NAME.getKey());
    }

    public Long getSize() {
        final Object size = getData().get(Part.FILE_SIZE.getKey());
        if (size == null || size instanceof Long) {
            return (Long) size;
        } else {
            return Long.parseLong(size.toString());
        }
    }

    public String getMimeType() {
        return (String) getData().get(Part.FILE_MIME_TYPE.getKey());
    }

    @Getter
    @AllArgsConstructor
    public enum Part {

        FILE_ID("sub"),
        FILE_NAME("fileName"),
        FILE_SIZE("fileSize"),
        FILE_MIME_TYPE("mimeType"),
        CONTEXT("ctx");

        private String key;
    }
}
