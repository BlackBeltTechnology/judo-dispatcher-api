package hu.blackbelt.judo.dispatcher.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private final String type;
    private final String errorCode;
    private final Map<String, Object> details;
    private final Throwable throwable;
}
