# Secstor

O objetivo é desenvolver uma biblioteca ou uma api para a utilização de algoritmos de compartilhamento de segredos e anonimização de dados para adequação de sistemas à LGPD.

## Requerimentos

- JDK 17 ou superior
- JRE 1.8.0 ou superior
- Lombok Annotations Support for VS Code (extensão para o <b>Visual Studio Code</b>)

## Para utilizar o projeto

Utilize o comando abaixo:

```
git clone https://github.com/Acacio-coding/Secstor
```

Agora siga os seguintes passos:

1. Abra o projeto com a IDE ou editor de código de sua preferência;
2. Crie um arquivo chamado "<b>config.properties</b>" dentro da pasta <i><b>resources</b></i>, localizado em "<b>src/main</b>";
3. Adicione as seguintes linhas no arquivo criado adaptando os parâmetros para o seu ambiente:
```
spring.datasource.url=jdbc:mysql://localhost:3306/NOME DO BANCO DE DADOS?createDatabaseIfNotExist=true
spring.datasource.username=USUÁRIO DO BANCO DE DADOS
spring.datasource.password=SENHA DO BANCO DE DADOS

secstor.auth-secret=SEGREDO UTILIZADO NA GERAÇÃO DOS TOKENS

secstor.admin-username=USUÁRIO ADMINISTRADOR INICIAL
secstor.admin-password=SENHA DO USUÁRIO ADMINISTRADOR INICIAL
```
Obs.: o endereço do banco de dados e a porta também podem mudar, mas para a execução em uma máquina local, pode se manter os mesmos utilizados acima.

4. A partir da ferramente realize um <i><b>build</b></i> para que os arquivos de código fonte sejam compilados e os arquivos de saída (para execução) sejam gerados;
5. Execute o projeto a partir da classe que contém o método <i><b>main</b></i>.
