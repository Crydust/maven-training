package com.mycompany.example13;

import com.mycompany.example13.boilerplate.BrowserResource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.ClassRule;
import org.junit.Test;

public class IndexPageIT {

    @ClassRule
    public static BrowserResource browser = new BrowserResource();

    @Test
    public void indexPageLoads() throws Exception {
        assertThat(browser.openIndexPage().isCurrentPage(), is(true));
    }
}
