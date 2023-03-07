package gob.regionancash.atencionciudadano;

import org.junit.jupiter.api.*;
import java.time.LocalTime;
import java.time.LocalDate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Connection;
import org.springframework.beans.factory.annotation.Autowired;
//import gob.regionancash.atencionciudadano.controller.AtencionController;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.util.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import gob.regionancash.atencionciudadano.config.*;
import gob.regionancash.atencionciudadano.repository.*;
import gob.regionancash.atencionciudadano.service.*;
import gob.regionancash.atencionciudadano.model.*;
import gob.regionancash.atencionciudadano.controller.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import gob.regionancash.atencionciudadano.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.io.*;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gob.regionancash.atencionciudadano.service.ContractService;
import org.springframework.beans.factory.annotation.Qualifier;
//@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = AtencionCiudadanaApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//@WebMvcTest
//@DataJpaTest

class AtencionCiudadanaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	//@MockBean
	@Autowired
    private PersonaRepository personaRepository;

	//@MockBean
	@Autowired
    private DependenciaRepository dependenciaRepository;

	@Autowired
    private AccessService accessService;

	@MockBean
    private CronogramaRepository cronogramaRepository;

	@MockBean
    private UserRepository userRepository;

	@MockBean
    private JwtService jwtService;
	
	@Autowired
	private AtencionController atencionController;

	@Test
	public void contextLoads() throws Exception {
		//assertThat(controller).isNotNull();
	}

	private Jedis jedis;

	@Autowired
	private ContractService contractService;
    
	//@Autowired
    //private RestTemplate restTemplate;

	private TestRestTemplate testRestTemplate;
    
	private TestRestTemplate testRestTemplateWithAuth;

	//@Value("${server.port}")
	@LocalServerPort
    private int port;

	public static MockHttpServletRequestBuilder buildRequest(MockHttpServletRequestBuilder request) {
        return request.header(HttpHeaders.AUTHORIZATION, "Bearer "+TOKEN);
    }

	@MockBean
	@Qualifier("userDetailsService2")
    private UserDetailsService userDetailsService2;


	static String TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2V4YW1wbGUuY29tL2lzc3VlciIsInVwbiI6Impkb2VAcXVhcmt1cy5pbyIsImdyb3VwcyI6WyJVc2VyIiwiQWRtaW4iXSwidWlkIjo0NDUsImZ1bGxOYW1lIjoiREFWSUxBIFJPTUVSTyBIQU5TIENFU0FSIiwiZGlyZWN0b3J5Ijo0LCJ1c2VyIjoiNDQ1NjQ1OTEiLCJiaXJ0aGRhdGUiOiIyMDAxLTA3LTEzIiwiaWF0IjoxNjc4MjIzNzM4LCJleHAiOjE2NzgyMjczMzgsImp0aSI6ImJlNzRhMmQ5LTFlYjYtNDZmOS04NjBiLTVlN2E2OWYzODI3YyJ9.A4ea1igttKx-G0mVTf_DGZR36tR9vfUnejCxloO2FTWZIKKeAfLtae2Q4q8UvU-5IvB2BHClChPnXrbu28-i5D810josTdjQd8V75D6YObU6sQohZ7afnQrSphLBVb8BsRoyvjO0uh6h2cH41kRbdCDf-BhApRH6yyRg3MLO_SZIKSlh9bru6-t3XzachNZEcK5ulOEuJ7EohzKK4reH4fvqFAY38uDvoVolWIsXR74YIi9WFO3sp19j3QCo_ZPVhjMzV7ArqeSrPx1rTD1exc4RVQLgIjF6BD4GLPs02pBkhR8mgSgEovm8YOR4e7VU2evmTpNR7Wl4MHg__KQyZw";




    @BeforeAll
    public void beforeAllTest() {
		//jwtService = Mockito.mock(JwtService.class);
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//JsonMapper jsonMapper = new JsonMapper();
		//jsonMapper.registerModule(new JavaTimeModule());



		HashMap claims=new HashMap();
		claims.put("name","admin");
		Mockito.when(jwtService.getClaims(TOKEN)).thenReturn(claims);
		Mockito.when(jwtService.getUsername(TOKEN)).thenReturn("admin");

		dependenciaRepository.save(Dependencia.builder().name("D1").build());
		dependenciaRepository.save(Dependencia.builder().name("D2").build());
		personaRepository.save(Persona.builder().tipoPersona("natur").tipoDocumento("DNI").nroDocumento("12345").apellidoNombre("ape no").build());
		User user=new User();
		user.setDirectory(1);
		user.setName("admin");

		Mockito.when(userDetailsService2.loadUserByUsername("admin")).thenReturn(user);
        testRestTemplate = new TestRestTemplate();
        testRestTemplateWithAuth = new TestRestTemplate("admin", "password", TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
		jedis = new Jedis("redis://default:75dQYsXYKR5NLT0Amf56icbrVRRmeF1X@redis-13208.c299.asia-northeast1-1.gce.cloud.redislabs.com:13208");
        Connection connection = jedis.getConnection();
		jedis.flushAll();
    }

    @BeforeEach
    public void init() {
        //mockServer = MockRestServiceServer.createServer(restTemplate);
    }

	@Test
	public void testPerms() {
		assertEquals(true, accessService.perms(TOKEN).size()>0);
	}

	@Test
	public void testRedisIsEmpty() {
		Set<String> result = jedis.keys("*");
		assertEquals(0, result.size());
	}

	//private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	


	@BeforeEach
	public void setUp() {
		HashMap claims=new HashMap();
		claims.put("name","admin");
		claims.put("uid",1);
		claims.put("directory",1);
		Mockito.when(jwtService.getClaims(TOKEN)).thenReturn(claims);
		Mockito.when(jwtService.getUsername(TOKEN)).thenReturn("admin");
		Mockito.when(jwtService.isTokenValid(org.mockito.ArgumentMatchers.eq(TOKEN),org.mockito.ArgumentMatchers.any(User.class))).thenReturn(true);
		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@AfterEach
	public void tearDown() {
		System.setOut(standardOut);
	}

	//@Test
	//public void testUnauthenticatedCantAccess() throws Exception{
		/*mockServer.expect(ExpectedCount.once(), 
          requestTo(new URI(getTestUrl())))
          .andExpect(method(HttpMethod.GET))
          .andRespond(withStatus(HttpStatus.UNAUTHORIZED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(objectMapper.writeValueAsString(""))
        );*/
      //  MvcResult result = mockMvc.perform(get("/atencion/test")).andReturn();
	//	assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());

		//ResponseEntity<String> result = testRestTemplate.getForEntity(getTestUrl(), String.class);
		//assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
	//}

	@Test
	public void testContractService() throws Exception{
		List response=contractService.getContracts(1);
		assertEquals(true, response instanceof java.util.List);
	}

	@Test
	//@WithUserDetails(value="admin", userDetailsServiceBeanName="userDetailsService23")
	//@WithMockUser(username="admin", authorities = { "ADMIN", "USER" })
	public void testRedisControlsSession() throws Exception{
		
		Set<String> redisResult = jedis.keys("*");
		Dependencia dependencia=Dependencia.builder().id(1L).build();

		Atencion atencion=Atencion.builder().dependencia(dependencia)
			.persona(Persona.builder().id(1L).build())
			.motivo("test").nroExpediente("1").fecha(LocalDate.of(2023,3,12))
			.horaIni(LocalTime.of(7, 30, 0)).build();

		mockMvc.perform(post("/atencion")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(atencion)))
                .andExpect(status().isForbidden());

		mockMvc.perform(post("/atencion")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + TOKEN)
				.content(objectMapper.writeValueAsString(atencion)))
                .andExpect(status().isOk());

		MvcResult mvcResult = mockMvc.perform(buildRequest(get("/atencion/0/10"))).andReturn();
		Map result=objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
		assertEquals(1, ((Collection)result.get("content")).size());
		//ResponseEntity<String> result =testRestTemplateWithAuth.getForEntity(getTestUrl(), String.class);
		//assertEquals("hello", result.getBody()); //login worked

		redisResult = jedis.keys("*");
		
		assertTrue(redisResult.size() > 0); //redis is populated with session data

		//if(redisResult!=null)throw new RuntimeException(""+redisResult);
		
		/*String sessionCookie = result.getHeaders().get("Set-Cookie").get(0).split(";")[0];
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", sessionCookie);
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		result = testRestTemplate.exchange(getTestUrl(), HttpMethod.GET, httpEntity, String.class);
		assertEquals("hello", result.getBody()); //access with session works worked
*/
		jedis.flushAll(); //clear all keys in redis

		//result = testRestTemplate.exchange(getTestUrl(), HttpMethod.GET, httpEntity, String.class);
		//assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
		//access denied after sessions are removed in redis
		
	
	}

	
		private String getTestUrl(){
			return "http://localhost:" + port;
		}

}
