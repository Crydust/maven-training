package com.mycompany.example13;

import com.mycompany.example13.boilerplate.BrowserResource;
import com.mycompany.example13.model.HelloServletPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloServletIT {

    @RegisterExtension
    public final BrowserResource browser = new BrowserResource();

    @Test
    public void helloServletPageLoads() throws Exception {
        assertThat(browser.openIndexPage().clickHelloServletLink().isCurrentPage(), is(true));
    }

    @Test
    public void helloServletPageEchosNickname() throws Exception {
        final String name;
        if (browser.getDriverOnlySupportsCharactersInTheBMP()) {
            name = UUID.randomUUID().toString() + " é ' \" _uD83E_uDD84 <plaintext>";
        } else {
            name = UUID.randomUUID().toString() + " é ' \" \uD83E\uDD84 <plaintext>";
        }
        final HelloServletPage page = browser
                .openIndexPage()
                .clickHelloServletLink()
                .sayHello(name);
        assertThat(page.getNicknameOutput(), is(name));
    }
}
