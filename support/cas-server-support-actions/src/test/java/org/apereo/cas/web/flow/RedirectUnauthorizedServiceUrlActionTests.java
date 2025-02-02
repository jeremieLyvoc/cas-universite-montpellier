package org.apereo.cas.web.flow;

import org.apereo.cas.services.RegisteredServiceTestUtils;
import org.apereo.cas.web.support.WebUtils;
import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.test.MockRequestContext;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link RedirectUnauthorizedServiceUrlActionTests}.
 *
 * @author Misagh Moayyed
 * @since 6.3.0
 */
@Tag("WebflowServiceActions")
class RedirectUnauthorizedServiceUrlActionTests extends AbstractWebflowActionsTests {

    @Autowired
    @Qualifier(CasWebflowConstants.ACTION_ID_REDIRECT_UNAUTHORIZED_SERVICE_URL)
    private Action action;

    @Test
    void verifyUrl() throws Exception {
        val context = getMockRequestContext();
        val url = new URI("https://github.com/apereo/cas");
        WebUtils.putUnauthorizedRedirectUrlIntoFlowScope(context, url);
        assertNull(action.execute(context));
        assertEquals(url.toString(), WebUtils.getUnauthorizedRedirectUrlFromFlowScope(context).toASCIIString());
    }

    @Test
    void verifyScript() throws Exception {
        val context = getMockRequestContext();
        WebUtils.putRegisteredService(context, RegisteredServiceTestUtils.getRegisteredService());
        WebUtils.putAuthentication(RegisteredServiceTestUtils.getAuthentication(), context);
        val url = new URI("classpath:UnauthorizedServiceUrl.groovy");
        WebUtils.putUnauthorizedRedirectUrlIntoFlowScope(context, url);
        assertNull(action.execute(context));
        assertEquals("https://apereo.org/cas", WebUtils.getUnauthorizedRedirectUrlFromFlowScope(context).toASCIIString());
    }

    private MockRequestContext getMockRequestContext() {
        val context = new MockRequestContext();
        val request = new MockHttpServletRequest();
        val response = new MockHttpServletResponse();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, response));
        RequestContextHolder.setRequestContext(context);
        ExternalContextHolder.setExternalContext(context.getExternalContext());
        context.setCurrentEvent(new EventFactorySupport().success(this));
        return context;
    }

}
