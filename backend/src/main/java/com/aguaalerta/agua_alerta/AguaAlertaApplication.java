package com.aguaalerta.agua_alerta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.aguaalerta.agua_alerta",
        "br.com.aguaalerta"
})
public class AguaAlertaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AguaAlertaApplication.class, args);
    }
}