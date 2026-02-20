package br.com.desafio.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:application.properties"
})
public interface Configuration extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("auth.username")
    String username();

    @Key("auth.password")
    String password();
}