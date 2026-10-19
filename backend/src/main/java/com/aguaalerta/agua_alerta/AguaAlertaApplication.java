package com.aguaalerta.agua_alerta;

import com.aguaalerta.agua_alerta.model.TipoOcorrencia;
import com.aguaalerta.agua_alerta.repository.TipoOcorrenciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = "br.com.aguaalerta")
@EntityScan("com.aguaalerta.agua_alerta.model")
@EnableJpaRepositories("com.aguaalerta.agua_alerta.repository")
public class AguaAlertaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AguaAlertaApplication.class, args);
    }

    @Bean
    CommandLineRunner carregarTipos(TipoOcorrenciaRepository repository) {
        return args -> {
            String[] tipos = {
                    "Vazamento de água",
                    "Esgoto a céu aberto",
                    "Água contaminada ou alteração na qualidade",
                    "Falta de água ou desabastecimento",
                    "Poluição de rios e mananciais"
            };
            for (String nome : tipos) {
                if (!repository.existsByNome(nome)) {
                    repository.save(new TipoOcorrencia(nome));
                }
            }
        };
    }
}