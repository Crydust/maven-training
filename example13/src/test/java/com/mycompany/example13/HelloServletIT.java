package com.mycompany.example13;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.mycompany.example13.boilerplate.BrowserResource;
import com.mycompany.example13.model.HelloServletPage;

class HelloServletIT {

    @RegisterExtension
    final BrowserResource browser = new BrowserResource();

    @Test
    void helloServletPageLoads() {
        assertThat(browser.openIndexPage().clickHelloServletLink().isCurrentPage(), is(true));
    }

    @Test
    void helloServletPageEchosNickname() {
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
