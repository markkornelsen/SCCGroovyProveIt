package com.example.ConsumerSCCProveIt;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(ids = { "com.example:Producer-SCCProveIt:+:stubs:6565" }, workOffline = true)
public class ConsumerSccProveItTests {

    private static String baseUrl = "http://localhost:6565";
//    private static String baseUrl = "http://localhost:8070";

    Logger log = Logger.getLogger(ConsumerSccProveItApplication.class.getName());

    @Test
    public void testExchangeGetDefaultMessageString() {
        HttpEntity<String> request = new HttpEntity<>(new String());
        ResponseEntity<String> response = new RestTemplate().exchange(baseUrl + "/value", HttpMethod.GET, request,
                String.class);
        assertThat(response.getBody(), containsString("Producer Entity Value Not Set"));
    }

    @Test
    public void testGetForObjectGetDefaultMessagePOJO() {
        ConsumerEntity entity = new RestTemplate().getForObject(baseUrl + "/value", ConsumerEntity.class);
        log.info("POJO: " + entity.getValue());
        assertThat(entity.getValue(), is("Producer Entity Value Not Set"));
    }

    @Test
    public void testGetForObjectGetDefaultMessageString() {
        String entity = new RestTemplate().getForObject(baseUrl + "/value", String.class);
        assertThat(entity, containsString("Producer Entity Value Not Set"));
    }

    @Test
    public void testExchangeGetDefaultMessagePOJO() {
        HttpEntity<ConsumerEntity> request = new HttpEntity<>(new ConsumerEntity());
        ResponseEntity<ConsumerEntity> response = new RestTemplate().exchange(baseUrl + "/value", HttpMethod.GET,
                request, ConsumerEntity.class);
        ConsumerEntity entity = response.getBody();

        log.info("POJO: " + entity.getValue());
        assertThat(entity.getValue(), is("Producer Entity Value Not Set"));
    }

    @Test
    public void testPostForObjectPOJOCorrectTimeFormat() throws JSONException {

        JSONObject request = new JSONObject();
        request.put("value", "booyah");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<ConsumerEntity> response = new RestTemplate().exchange(baseUrl + "/createEntity",
                HttpMethod.POST, httpEntity, ConsumerEntity.class);

        assertThat(response.getBody().getTime(), RegexMatcher.matchesRegex("[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]+"));
    }

    @Test
    public void testPostForObjectPOJOValueIsLetters() throws JSONException {

        JSONObject request = new JSONObject();
        request.put("value", "booyah");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<ConsumerEntity> response = new RestTemplate().exchange(baseUrl + "/createEntity",
                HttpMethod.POST, httpEntity, ConsumerEntity.class);

        assertThat(response.getBody().getValue(), RegexMatcher.matchesRegex("[a-z]+"));
        log.info("##### createEntity actually returned: " + response.getBody().getValue());
    }
}
