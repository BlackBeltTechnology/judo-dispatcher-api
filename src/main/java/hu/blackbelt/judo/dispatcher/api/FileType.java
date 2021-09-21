package hu.blackbelt.judo.dispatcher.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileType {

    private String id;
    private String fileName;
    private Long size;
    private String mimeType;

}
