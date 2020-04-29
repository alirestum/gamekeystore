package hu.restumali.gamekeystore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfig {

    @Bean
    public FormattingConversionService conversionService(){
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        registrar.registerFormatters(conversionService);

        return conversionService;
    }
}
