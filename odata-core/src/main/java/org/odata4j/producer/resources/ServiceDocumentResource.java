package org.odata4j.producer.resources;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.odata4j.core.ODataConstants;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.FormatWriter;
import org.odata4j.format.FormatWriterFactory;
import org.odata4j.producer.ODataProducer;

@Path("")
public class ServiceDocumentResource {

  @GET
  @Produces({ ODataConstants.APPLICATION_XML_CHARSET_UTF8, 
	  ODataConstants.TEXT_JAVASCRIPT_CHARSET_UTF8, 
	  ODataConstants.APPLICATION_JSON_CHARSET_UTF8, 
	  ODataConstants.APPLICATION_XML,
	  ODataConstants.APPLICATION_JSON})
  public Response getServiceDocument(
      @Context HttpHeaders httpHeaders,
      @Context UriInfo uriInfo,
      @Context Providers providers,
      @QueryParam("$format") String format,
      @QueryParam("$callback") String callback) {

    ODataProducer producer = BaseResource.getODataProducer(providers);

    EdmDataServices metadata = producer.getMetadata();

    StringWriter w = new StringWriter();
    FormatWriter<EdmDataServices> fw = FormatWriterFactory.getFormatWriter(EdmDataServices.class, httpHeaders.getAcceptableMediaTypes(), format, callback);
    fw.write(uriInfo, w, metadata);

    return Response.ok(w.toString(), fw.getContentType())
        .header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER)
        .build();
  }

}
