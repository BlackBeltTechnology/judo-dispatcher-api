package hu.blackbelt.judo.dispatcher.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.security.Principal;
import java.util.Map;

@Getter
@Builder
public class JudoPrincipal implements Principal {

    @NonNull
    private String name;

    private String realm;

    private String client;

    @NonNull
    private Map<String, Object> attributes;

    @Override
    public String getName() {
        return name;
    }
}
