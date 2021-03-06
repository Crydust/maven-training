package com.mycompany.example13;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.mycompany.example13.boilerplate.BrowserResource;
import com.mycompany.example13.model.TodoJspServletPage;

class TodoJspServletIT {

    @RegisterExtension
    final BrowserResource browser = new BrowserResource();

    @Test
    void todoServletPageLoads() {
        assertThat(browser.openIndexPage().clickTodoJspServletLink().isCurrentPage(), is(true));
    }

    @Test
    void addWorks() {
        final String label1 = UUID.randomUUID().toString();
        final String label2;
        if (browser.getDriverOnlySupportsCharactersInTheBMP()) {
            label2 = UUID.randomUUID().toString() + " é ' \" _uD83E_uDD84 <plaintext>";
        } else {
            label2 = UUID.randomUUID().toString() + " é ' \" \uD83E\uDD84 <plaintext>";
        }
        TodoJspServletPage page = browser.openIndexPage().clickTodoJspServletLink();
        assertThat("is not present before adding", page.contains(label1), is(false));
        page.add(label1);
        assertThat("is present after adding", page.contains(label1), is(true));
        page.add(label2);
        assertThat("is present after adding", page.contains(label2), is(true));
        page.remove(label1);
        assertThat("is no longer present after removing", page.contains(label1), is(false));
        assertThat("is present after removing another", page.contains(label2), is(true));
        page.remove(label2);
        assertThat("is no longer present after removing", page.contains(label2), is(false));
    }

    @Test
    void saveWorks() {
        final String label1 = UUID.randomUUID().toString();
        final String label2;
        if (browser.getDriverOnlySupportsCharactersInTheBMP()) {
            label2 = UUID.randomUUID().toString() + " é ' \" _uD83E_uDD84 <plaintext>";
        } else {
            label2 = UUID.randomUUID().toString() + " é ' \" \uD83E\uDD84 <plaintext>";
        }
        TodoJspServletPage page = browser.openIndexPage().clickTodoJspServletLink();
        assertThat("is not present before adding", page.contains(label1), is(false));
        assertThat("is not present before adding", page.contains(label2), is(false));
        page.add(label1);
        page.add(label2);
        assertThat(page.isDone(label1), is(false));
        assertThat(page.isDone(label2), is(false));
        page.setDone(label1, true);
        assertThat(page.isDone(label1), is(true));
        assertThat(page.isDone(label2), is(false));
        page.setDone(label2, true);
        assertThat(page.isDone(label1), is(true));
        assertThat(page.isDone(label2), is(true));

        page = browser.openIndexPage().clickTodoJspServletLink();
        assertThat(page.isDone(label1), is(true));
        assertThat(page.isDone(label2), is(true));
        page.remove(label1);
        page.remove(label2);

    }

    @Test
    void csrfFilterStopsIntruder() throws Exception {
        TodoJspServletPage page = browser.openIndexPage().clickTodoJspServletLink();
        assertThat(page.contains("Good"), is(false));
        assertThat(page.contains("Bad"), is(false));
        assertThat(sendAddWithWrongCsrfToken(browser.getBaseURL(), browser.getSessionCookie()), is(403));
        page.add("Good");
        assertThat(page.contains("Bad"), is(false));
        assertThat(page.contains("Good"), is(true));
    }

    private static int sendAddWithWrongCsrfToken(String baseURL, String sessionCookie) throws Exception {
        String url = baseURL + "TodoServlet";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Cookie", sessionCookie);
        String urlParameters = "label=Bad&button=add";
        con.setDoOutput(true);
        try (OutputStream out = con.getOutputStream()) {
            out.write(urlParameters.getBytes(StandardCharsets.UTF_8));
        }
        int responseCode = con.getResponseCode();
        con.disconnect();
        return responseCode;
    }
}
