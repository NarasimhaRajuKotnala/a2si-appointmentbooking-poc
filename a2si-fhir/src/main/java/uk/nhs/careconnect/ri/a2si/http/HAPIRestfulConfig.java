package uk.nhs.careconnect.ri.a2si.http;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.VersionUtil;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import uk.nhs.careconnect.ri.gatewaylib.provider.*;
import uk.nhs.careconnect.ri.lib.ServerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.TimeZone;


public class HAPIRestfulConfig extends RestfulServer {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HAPIRestfulConfig.class);

	private WebApplicationContext myAppCtx;

    @Override
	public void addHeadersToResponse(HttpServletResponse theHttpResponse) {
		theHttpResponse.addHeader("X-Powered-By", "HAPI FHIR " + VersionUtil.getVersion() + " RESTful Server (NHS Care Connect STU3)");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));


		FhirVersionEnum fhirVersion = FhirVersionEnum.DSTU3;
		setFhirContext(new FhirContext(fhirVersion));

		// Get the spring context from the web container (it's declared in web.xml)
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		Config cfg = myAppCtx.getBean(Config.class);
  
		String serverBase = cfg.getServerBase();
		String serverName = cfg.getServerName();
		String serverVersion = cfg.getServerVersion();

		log.info("serverBase: " + serverBase);
		log.info("serverName: " + serverName);
		log.info("serverVersion: " + serverVersion);

        if (serverBase != null && !serverBase.isEmpty()) {
            setServerAddressStrategy(new HardcodedServerAddressStrategy(serverBase));
        }


		setResourceProviders(Arrays.asList(

				// Care Connect API providers START


				// UEC appointment prototype
				myAppCtx.getBean(ScheduleResourceProvider.class)

				// Support for NHS Digital National Projects  END

				// NOT FOR LIVE


		));

        // Replace built in conformance provider (CapabilityStatement)
        setServerConformanceProvider(new CareConnectConformanceProvider( ));

        setServerName(serverName);
        setServerVersion(serverVersion);


		// This is the format for each line. A number of substitution variables may
		// be used here. See the JavaDoc for LoggingInterceptor for information on
		// what is available.

		ServerInterceptor gatewayInterceptor = new ServerInterceptor(log);
		registerInterceptor(gatewayInterceptor);
		//gatewayInterceptor.setLoggerName("ccri.FHIRGateway");
		//gatewayInterceptor.setLogger(ourLog);

		// This paging provider is not robust KGM 24/11/2017

		// Mocking of a database Paging Provider is in server

		FifoMemoryPagingProvider pp = new FifoMemoryPagingProvider(10);
		pp.setDefaultPageSize(10);
		pp.setMaximumPageSize(100);
		setPagingProvider(pp);

		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		FhirContext ctx = getFhirContext();
		// Remove as believe due to issues on docker ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
	}
}
