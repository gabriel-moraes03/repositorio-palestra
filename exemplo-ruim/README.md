# exemplo-ruim

> ⚠️ **Este projeto é ruim de propósito.** É o código que muita gente escreve no começo,
> copiando e colando. Ele tem bugs e testes falhando **de propósito**. Não use como modelo.

## Como rodar

```bash
./mvnw spring-boot:run      # sobe em http://localhost:8080 (H2 em memória)
./mvnw test                 # 4 testes falham de propósito (veja abaixo)
```

No Windows, use `mvnw.cmd`.

Exemplo de cadastro:

```bash
curl -X POST localhost:8080/treinadores/futebol -H 'Content-Type: application/json' \
     -d '{"pessoaId": 1, "esporteId": 1, "nivelLicenca": "A"}'
```

## Estrutura de pacotes

```
com.exemplo.treinadores
├── model/        Pessoa, Esporte e três entidades de treinador com os MESMOS campos repetidos
├── repository/   um repositório por entidade + TreinadorRepository com um findBy... por combinação de filtro
├── service/      FutebolService, VoleiService, BasqueteService: copiados um do outro
└── controller/   um controller por esporte, cada um com seu try/catch e seu formato de erro
```

## O que tem de errado aqui

| Problema | Onde |
|---|---|
| Campos comuns repetidos em três entidades, sem herança | `model/Treinador*.java` |
| Três services copiados, sem interface nem classe base | `service/*Service.java` |
| **Bug 1:** condição invertida, rejeita certificado válido | `VoleiService.validar` |
| **Bug 2:** a **causa** foi a transação ausente (`@Modifying` sem `@Transactional`), mas o que **escondeu** o erro foi o `catch` vazio | `BasqueteService.inativarAnterior` + `TreinadorBasqueteRepository.inativarAtivos` |
| Três formatos de erro: 400 texto, 200 com `Map`, 500 sem corpo | `controller/*Controller.java` |
| Sobrecarga com nomes diferentes e corpo copiado | `cadastrarSimples` / `cadastrarComInicio` |
| Um método por combinação de filtro + escada de `if/else` | `TreinadorRepository`, `TreinadorController.listar` |
| Ordenação fixa; `sort` é recebido e ignorado | `TreinadorRepository` |
| Combinação de filtro não prevista devolve tudo sem avisar | `TreinadorController.listar` |
| Filtros `nome` e `sort` ignorados em silêncio quando combinados | `TreinadorController.listar` |

### Filtros ignorados em silêncio

A escada de `if/else` só conhece algumas combinações. Quando a combinação pedida não
tem um ramo próprio, o controller cai no primeiro ramo que "quase" serve e descarta o
resto, sem erro e sem aviso:

```bash
curl 'localhost:8080/treinadores?esporte=futebol&ativo=true&nome=ana&sort=nome,desc'
```

- `nome` só é usado junto com `esporte` **e** `nivel` (`findByNomeAndEsporteAndNivel`).
  Sem `nivel`, a requisição acima cai em `findByEsporteAndAtivo` e o `nome=ana` é jogado fora.
- `sort` nunca é usado. Com `esporte`, a ordem é sempre por nome, crescente, fixa na consulta;
  sem `esporte`, a lista sai na ordem das três tabelas, uma depois da outra. O `,desc` não faz nada.

A resposta é `200` e parece certa, mas pode trazer treinadores que não se chamam "ana", em
uma ordem diferente da pedida. No `exemplo-ideal`, a mesma URL filtra por nome e ordena
como pedido, e uma chave desconhecida devolve `400`.

## Testes que falham de propósito

| Teste | Por quê |
|---|---|
| `voleiComCertificadoValidoPodeCadastrar` | Bug 1 |
| `cadastrarNovoTreinadorInativaOAnterior[volei]` | Bug 1 de novo: não dá nem para cadastrar o primeiro |
| `cadastrarNovoTreinadorInativaOAnterior[basquete]` | Bug 2: o anterior continua ativo |
| `errosTemOMesmoFormatoNosTresEsportes` | cada controller responde erro de um jeito |

Passam: `cadastrarNovoTreinadorInativaOAnterior[futebol]`, `filtroPorEsporteRestringeOResultado`
e `ordenacaoPorNomeFunciona`. A ordenação "funciona" só porque a ordem fixa é justamente por nome.

## Sonar e cobertura

JaCoCo e `sonar-maven-plugin` já estão configurados (`sonar.projectKey=palestra-ruim`).
Como há testes falhando de propósito, ignore as falhas para gerar o relatório:

```bash
./mvnw clean verify sonar:sonar -Dmaven.test.failure.ignore=true \
  -Dsonar.host.url=http://localhost:9000 -Dsonar.token=SEU_TOKEN
```
