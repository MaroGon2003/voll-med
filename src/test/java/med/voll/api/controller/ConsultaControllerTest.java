package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DatosAgendarConsulta> agendarConsultaJacksonTester;

    @Autowired
    private JacksonTester<DatosDetalleConsulta> detalleConsultaJacksonTester;

    @MockBean
    private AgendaDeConsultaService agendaDeConsultaService;

    @Test
    @DisplayName("Deberia retornar 400 si los datos son incorrectos")
    @WithMockUser
    void agendarEsenario1() throws Exception {

        var response = mvc.perform(post("/consulta")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deberia retornar 200 si los datos son correctos")
    @WithMockUser
    void agendarEsenario2() throws Exception {

        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.ORTOPEDIA;
        var datos = new DatosDetalleConsulta(null,2L,1L,fecha);

        when(agendaDeConsultaService.agendar(any())).thenReturn(datos);

        var response = mvc.perform(post("/consulta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(agendarConsultaJacksonTester.write(new DatosAgendarConsulta(null,2L,1L,fecha,especialidad)).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = detalleConsultaJacksonTester.write(new DatosDetalleConsulta(null,2L,1L,fecha)).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }
}