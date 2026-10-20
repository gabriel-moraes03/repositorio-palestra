# exemplo-ideal

O mesmo comportamento e os mesmos endpoints do [`exemplo-ruim`](../exemplo-ruim), organizado com
padronização, polimorfismo, modelagem genérica e filtros dinâmicos com Specification.

## Como rodar

```bash
./mvnw spring-boot:run      # sobe em http://localhost:8080 (H2 em memória)
./mvnw test                 # todos os testes passam
```

No Windows, use `mvnw.cmd`.

```bash
curl -X POST localhost:8080/treinadores/futebol -H 'Content-Type: application/json' \
     -d '{"pessoaId": 1, "esporteId": 1, "nivelLicenca": "A"}'

curl 'localhost:8080/treinadores?esporte=futebol&ativo=true&nome=ana&sort=nome,desc'
```

## Estrutura de pacotes

```
com.exemplo.treinadores
├── controller/   um controller por esporte (POST) + TreinadorController (GET), sem try/catch
├── service/      BaseService<T, ID>, TreinadorService<T, R> (Template Method),
│                 FutebolService / VoleiService / BasqueteService, TreinadorConsultaService
├── model/        Treinador abstrato (JOINED) + uma subclasse por esporte só com o campo próprio
├── dto/          records de request (implementam TreinadorRequest) e TreinadorResponse
├── mapper/       TreinadorMapper (manual)
├── repository/   TreinadorRepository (JpaRepository + JpaSpecificationExecutor), Pessoa, Esporte
├── filtro/       TreinadorFiltro (tabela chave -> Specification) e Ordenacao (chaves permitidas)
└── erro/         ErroDeDominio, ApiError, ApiExceptionHandler
```

## Problema do ruim → como o ideal resolve

| Problema no `exemplo-ruim` | Como o `exemplo-ideal` resolve |
|---|---|
| Campos comuns repetidos em três entidades | `Treinador` abstrato com `@Inheritance(JOINED)`; subclasses só com o campo próprio |
| Três services copiados | `TreinadorService<T, R>` com Template Method: `cadastrar` chama `validarComum` → `validarEspecifico` → `inativarAnterior` → `salvar(criar(...))`; cada esporte só implementa `validarEspecifico` e `criar` |
| **Bug 1** (condição invertida no vôlei) | A regra vive num só lugar (`VoleiService.validarEspecifico`) e tem teste; o fluxo comum não é reescrito por esporte |
| **Bug 2** (update sem transação + `catch` vazio) | `@Transactional` no `TreinadorService` vale para os três esportes, e nenhuma exceção é engolida |
| Três formatos de erro | `ApiExceptionHandler` + `ApiError`: sempre `{status, mensagem, campo}` |
| `try/catch` em cada controller | Controllers sem `try/catch`; exceção de negócio é `ErroDeDominio` |
| `descrever(Object t)` com `instanceof` por esporte | Sobrescrita: `Treinador.habilitacao()` abstrato, cada subclasse com `@Override` |
| `cadastrarSimples` / `cadastrarComInicio` com corpo copiado | Sobrecarga que delega: `cadastrar(req)` chama `cadastrar(req, inicio)` |
| Um `findBy...` por combinação + escada de `if/else` | `TreinadorFiltro`: tabela `Map<String, Regra>`; `repo.findAll(spec, ordem)` |
| Combinação não prevista devolve tudo sem avisar | Chave desconhecida lança `ErroDeDominio` → 400 |
| Ordenação fixa, `sort` ignorado | `Ordenacao.criar(sort)` com chaves permitidas (`nome`, `dataInicio`, `,desc` opcional) |
| `GET /{id}` procurando em três tabelas | Um `findById` na hierarquia |
| Entidade exposta direto no JSON | DTOs `record` + `TreinadorMapper` |

## Testes

`TreinadorTest` tem os mesmos casos do ruim, com os mesmos nomes, mais
`chaveDeFiltroDesconhecidaRetorna400`. Aqui todos passam.

## Sonar e cobertura

JaCoCo e `sonar-maven-plugin` já estão configurados (`sonar.projectKey=palestra-ideal`):

```bash
./mvnw clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=SEU_TOKEN
```

O relatório do JaCoCo fica em `target/site/jacoco/index.html` depois do `verify`.
