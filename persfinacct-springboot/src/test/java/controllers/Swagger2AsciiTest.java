package controllers;

import com.esrx.services.personfinancialaccounts.PersonFinancialAccountApplication;
import com.esrx.services.personfinancialaccounts.config.SwaggerConfiguration;
import io.github.robwin.swagger2markup.GroupBy;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PersonFinancialAccountApplication.class,SwaggerConfiguration.class },
        properties = { "mongo.ssl.enabled=false" })
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class Swagger2AsciiTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void convertSwaggerToAsciiDocViaMockMvc() throws Exception {
        Swagger2MarkupResultHandler.Builder builder = Swagger2MarkupResultHandler
                .outputDirectory("target/generated-sources");

        builder.withPathsGroupedBy(GroupBy.TAGS);

        MvcResult mvcResult = this.mockMvc.perform(get("/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        builder.build().handle(mvcResult);
    }

    @Test
    public void includeJavaScriptToAdocFiles() throws IOException {
        appendJavaScript("paths.adoc");
        appendJavaScript("definitions.adoc");
    }

    private void appendJavaScript(String fileName) throws IOException {

        BufferedWriter bw = Files.newBufferedWriter(Paths.get("target/generated-sources", fileName), StandardOpenOption.APPEND);

        Assert.assertNotNull(getSwaggerAsciiJavaScriptAsString());
        bw.write(getSwaggerAsciiJavaScriptAsString());
        bw.close();
    }

    private String getSwaggerAsciiJavaScriptAsString() throws IOException {

        String fileName = "static/swaggerascii.js";
        File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());

        return new StringBuilder()
                .append("\n++++\n<script type=\"text/javascript\">\n")
                .append(FileUtils.readFileToString(file, "UTF-8"))
                .append("</script>\n++++\n")
                .toString();
    }


}
