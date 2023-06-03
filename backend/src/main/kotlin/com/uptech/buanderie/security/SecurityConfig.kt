package com.uptech.buanderie.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import com.uptech.buanderie.services.implementations.UserDetailsServiceImpl

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter()/*, WebMvcConfigurer*/ {



    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    private lateinit var unauthorizedHandler: AuthEntryPointJwt
    @Bean
    fun authenticationJwtTokenFilter(): AuthenticationJwtTokenFilter {
        return AuthenticationJwtTokenFilter()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http?.exceptionHandling()?.authenticationEntryPoint(unauthorizedHandler)
        http?.authorizeRequests()?.antMatchers(*SecurityUtils.AUTH_WHITELIST)?.permitAll()
        http?.authorizeRequests()?.antMatchers("/api/v1/user/**")?.hasAuthority("SuperAdmin")
        http?.authorizeRequests()?.antMatchers("/api/v1/user/**")?.hasAuthority("Admin")
        http?.authorizeRequests()?.anyRequest()?.authenticated()
        http?.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers(*SecurityUtils.AUTH_WHITELIST)
    }

//    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
//    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}