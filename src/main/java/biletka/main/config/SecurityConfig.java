package biletka.main.config;

import biletka.main.Utils.PasswordEncoder;
import biletka.main.enums.RoleEnum;
import biletka.main.service.AdministratorService;
import biletka.main.service.Impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

import static org.springframework.security.web.server.authorization.IpAddressReactiveAuthorizationManager.hasIpAddress;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final AdministratorService administratorService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.getEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(300000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public WebMvcConfigurer corsMappingConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/place").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/place/hall").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/event").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers(HttpMethod.POST,"/session").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers("/client**").hasAuthority(RoleEnum.CLIENT.getAuthority())
                        .requestMatchers("/organization**").hasAuthority(RoleEnum.ORGANIZATION.getAuthority())
                        .requestMatchers("/dGlja2V0QWRtaW4=**" ).access(hasIpAddress())
                        .anyRequest().permitAll()
                );

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private AuthorizationManager<RequestAuthorizationContext> hasIpAddress() {
        ArrayList<IpAddressMatcher> ipAddressMatchers = administratorService.getMassiveIpAddress();
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            boolean statusUser = false;

            for (IpAddressMatcher ip: ipAddressMatchers) {
                if (ip.matches(request)) {
                    statusUser = true;
                    break;
                }
            }

            return new AuthorizationDecision(statusUser);
        };
    }
}
