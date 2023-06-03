package com.uptech.buanderie.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.util.*


@Configuration
@EnableWebMvc
@Import(BeanValidatorPluginsConfiguration::class)

class SpringFoxConfig : WebMvcConfigurer {
    @Bean
    fun api(): Docket {
        val authenticationScheme = HttpAuthenticationScheme.JWT_BEARER_BUILDER
            .name("Authorization")
            .build()
        return Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.uptech.buanderie"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(listOf<SecurityScheme>(authenticationScheme))
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Buanderie",
            "Documentation de la parti", "1.0", "", "support@uptech.com.tn", "", ""

        )
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Arrays.asList(SecurityReference("Authorization", authorizationScopes))
    }
}