package com.mycompany.example13;

import com.mycompany.example13.boilerplate.BrowserResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IndexPageIT {

    @RegisterExtension
    public final BrowserResource browser = new BrowserResource();

    @Test
    public void indexPageLoads() throws Exception {
        assertThat(browser.openIndexPage().isCurrentPage(), is(true));
    }
}
