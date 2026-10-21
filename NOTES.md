## Como rodar o back

1. Ter o PostgreSQL instalado e criar o banco `agua_alerta`
2. No terminal, dentro da pasta `backend`, definir as variáveis:
   $env:SPRING_DATASOURCE_PASSWORD="senha-do-seu-postgres"
   $env:GEMINI_API_KEY="sua-chave-do-gemini"
3. Rodar: .\mvnw spring-boot:run
4. Esperar aparecer "Started AguaAlertaApplication"

Cada pessoa usa o próprio banco e a própria chave. Não commitar senha nem chave.
para criar a chave entre no aistudio e escreva no gemini get API keys.
lembrando que pra funcionar tem que estar o back e o banco conectados.
NÃO COMMITAR SENHA NEM CHAVE, não escrever senha nem chave no codigo fonte, tem que ser tudo pelo terminal pq é uma variável, e só vai funcionar no terminal enquanto estiver rodando, dps de dar o `.\mvnw spring-boot:run` adiciona outro terminal, e faz os testes pelo terminal mesmo. 