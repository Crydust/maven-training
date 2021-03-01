package com.mycompany.example13;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.mycompany.example13.boilerplate.BrowserResource;

class IndexPageIT {

    @RegisterExtension
    final BrowserResource browser = new BrowserResource();

    @Test
    void indexPageLoads() {
        assertThat(browser.openIndexPage().isCurrentPage(), is(true));
    }
}
