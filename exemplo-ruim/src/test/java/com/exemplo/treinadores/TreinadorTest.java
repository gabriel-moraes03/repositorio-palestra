package com.exemplo.treinadores;

import com.exemplo.treinadores.model.Pessoa;
import com.exemplo.treinadores.repository.EsporteRepository;
import com.exemplo.treinadores.repository.PessoaRepository;
import com.exemplo.treinadores.repository.TreinadorBasqueteRepository;
import com.exemplo.treinadores.repository.TreinadorFutebolRepository;
import com.exemplo.treinadores.repository.TreinadorVoleiRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Sem @Transactional de propósito: os testes rodam como uma requisição de verdade.
@SpringBootTest
@AutoConfigureMockMvc
class TreinadorTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    PessoaRepository pessoas;

    @Autowired
    EsporteRepository esportes;

    @Autowired
    TreinadorFutebolRepository futebol;

    @Autowired
    TreinadorVoleiRepository volei;

    @Autowired
    TreinadorBasqueteRepository basquete;

    @BeforeEach
    void limpar() {
        futebol.deleteAll();
        volei.deleteAll();
        basquete.deleteAll();
    }

    @Test
    void voleiComCertificadoValidoPodeCadastrar() throws Exception {
        Long pessoa = novaPessoa("Vera");

        mvc.perform(post("/treinadores/volei").contentType(MediaType.APPLICATION_JSON).content(corpoValido("volei", pessoa)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"futebol", "volei", "basquete"})
    void cadastrarNovoTreinadorInativaOAnterior(String esporte) throws Exception {
        Long pessoa = novaPessoa("Pedro");
        Long anterior = cadastrar(esporte, pessoa);
        Long novo = cadastrar(esporte, pessoa);

        mvc.perform(get("/treinadores/" + anterior)).andExpect(jsonPath("$.ativo").value(false));
        mvc.perform(get("/treinadores/" + novo)).andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void errosTemOMesmoFormatoNosTresEsportes() throws Exception {
        Long pessoa = novaPessoa("Erro");

        for (String esporte : List.of("futebol", "volei", "basquete")) {
            mvc.perform(post("/treinadores/" + esporte).contentType(MediaType.APPLICATION_JSON).content(corpoInvalido(esporte, pessoa)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.mensagem").isString());
        }
    }

    @Test
    void filtroPorEsporteRestringeOResultado() throws Exception {
        Long pessoa = novaPessoa("Filtro");
        Long doFutebol = cadastrar("futebol", pessoa);
        cadastrar("basquete", pessoa);

        mvc.perform(get("/treinadores").param("esporte", "futebol"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(doFutebol));
    }

    @Test
    void ordenacaoPorNomeFunciona() throws Exception {
        Long zeca = cadastrar("futebol", novaPessoa("Zeca"));
        Long alice = cadastrar("futebol", novaPessoa("Alice"));

        mvc.perform(get("/treinadores").param("esporte", "futebol").param("sort", "nome"))
                .andExpect(jsonPath("$[0].id").value(alice))
                .andExpect(jsonPath("$[1].id").value(zeca));
    }

    // ---- apoio ----

    private Long novaPessoa(String nome) {
        Pessoa p = new Pessoa();
        p.setNome(nome);
        p.setNascimento(LocalDate.of(1990, 1, 1));
        p.setContato(nome.toLowerCase() + "@exemplo.com");
        return pessoas.save(p).getId();
    }

    private Long cadastrar(String esporte, Long pessoa) throws Exception {
        String resposta = mvc.perform(post("/treinadores/" + esporte).contentType(MediaType.APPLICATION_JSON).content(corpoValido(esporte, pessoa)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(resposta, "$.id")).longValue();
    }

    private String corpoValido(String esporte, Long pessoa) {
        return corpo(esporte, pessoa, switch (esporte) {
            case "futebol" -> "\"nivelLicenca\": \"A\"";
            case "volei" -> "\"certificadoCbv\": \"CBV-123\", \"validadeCertificado\": \"" + LocalDate.now().plusYears(1) + "\"";
            default -> "\"registroFederacao\": \"FB-77\"";
        });
    }

    private String corpoInvalido(String esporte, Long pessoa) {
        return corpo(esporte, pessoa, switch (esporte) {
            case "futebol" -> "\"nivelLicenca\": \"Z\"";
            case "volei" -> "\"certificadoCbv\": \"\", \"validadeCertificado\": \"" + LocalDate.now().minusDays(1) + "\"";
            default -> "\"registroFederacao\": \"\"";
        });
    }

    private String corpo(String esporte, Long pessoa, String campoProprio) {
        Long esporteId = esportes.findByNome(esporte).orElseThrow().getId();
        return "{\"pessoaId\": " + pessoa + ", \"esporteId\": " + esporteId + ", " + campoProprio + "}";
    }
}
