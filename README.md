# Qualidade e reuso no back-end Java

Material de apoio de uma palestra para estudantes. São **dois projetos Spring Boot independentes**
com o mesmo domínio (cadastro de treinadores de futebol, vôlei e basquete) e os mesmos endpoints:

| Projeto | O que é |
|---|---|
| [`exemplo-ruim/`](exemplo-ruim) | O jeito que muita gente escreve no começo: copiar e colar. |
| [`exemplo-ideal/`](exemplo-ideal) | O mesmo comportamento, organizado. |

> ⚠️ **O `exemplo-ruim` é ruim de propósito** e tem **testes falhando de propósito**.
> Os bugs estão lá para serem encontrados na palestra. Não corrija e não use como modelo.

O domínio é inventado e não reproduz nenhum sistema real.

## Tópicos da palestra

| Tópico | Onde ver no `exemplo-ideal` |
|---|---|
| **Padronização** | `erro/ApiExceptionHandler`, `ApiError`, `ErroDeDominio`; DTOs `record` + mappers; mesmas camadas (controller, service, repository, mapper) |
| **Polimorfismo** | sobrecarga `cadastrar(req)` / `cadastrar(req, inicio)`; sobrescrita de `validarEspecifico`; generics em `BaseService<T, ID>` e `TreinadorService<T, R>` |
| **Modelagem genérica** | `Treinador` abstrato com `@Inheritance(JOINED)`; Template Method em `TreinadorService.cadastrar` |
| **Filtros dinâmicos** | `filtro/TreinadorFiltro` (tabela chave → `Specification`) e `filtro/Ordenacao` |
| **SonarQube** | JaCoCo + `sonar-maven-plugin` configurados nos dois `pom.xml` |

## Endpoints (iguais nos dois projetos)

- `POST /treinadores/futebol`, `POST /treinadores/volei`, `POST /treinadores/basquete`
- `GET /treinadores/{id}`
- `GET /treinadores?esporte=&ativo=&nome=&sort=` (`sort`: `nome` ou `dataInicio`, com `,desc` opcional)

O banco é H2 em memória e já sobe com três esportes (`futebol`, `volei`, `basquete`) e três pessoas.

## Como rodar

Requer Java 21 ou mais novo. O Maven vem junto (Maven Wrapper), não precisa instalar.

```bash
cd exemplo-ideal          # ou exemplo-ruim
./mvnw spring-boot:run    # http://localhost:8080
./mvnw test
```

No Windows, use `mvnw.cmd`.

Resultado esperado dos testes:

- `exemplo-ideal`: tudo verde.
- `exemplo-ruim`: falham **de propósito** `voleiComCertificadoValidoPodeCadastrar`,
  `cadastrarNovoTreinadorInativaOAnterior` para vôlei e basquete, e `errosTemOMesmoFormatoNosTresEsportes`.
  Veja o [README do ruim](exemplo-ruim/README.md) para o porquê de cada um.

## SonarQube (opcional)

Requer Docker. Sobe um SonarQube local e analisa os dois projetos:

```bash
docker compose up -d      # http://localhost:9000 (admin / admin); demora ~1 min para subir
./sonar.sh                # cria o token e o Quality Gate "Palestra", depois analisa os dois
docker compose down       # para tudo (os dados ficam nos volumes; use -v para apagar)
```

O gate "Palestra" avalia o código todo (confiabilidade A, segurança A, cobertura ≥ 70%,
duplicação ≤ 3%). O "Sonar way" padrão só olha código novo e, na primeira análise, aprovaria os dois.
Se você trocar a senha do admin, rode `SONAR_PASSWORD=nova ./sonar.sh`.

Para analisar manualmente, com outro servidor:

```bash
cd exemplo-ideal
./mvnw clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=SEU_TOKEN

cd ../exemplo-ruim         # testes falhando de propósito: ignore as falhas para gerar o relatório
./mvnw clean verify sonar:sonar -Dmaven.test.failure.ignore=true \
  -Dsonar.host.url=http://localhost:9000 -Dsonar.token=SEU_TOKEN
```

## Licença

[MIT](LICENSE)
