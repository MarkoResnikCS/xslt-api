package com.igd.xsltapi;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Date;

public class XsltTransformerApplication {

    public static void main(String[] args) throws SaxonApiException {

        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        XsltExecutable stylesheet = compiler.compile(new StreamSource(new File("src/main/resources/stylesheet.xsl")));
        Serializer out = processor.newSerializer(new File("output-" + new Date().getTime() + ".xml"));
        Xslt30Transformer transformer = stylesheet.load30();
        transformer.transform(new StreamSource(new File("input.xml")), out);

    }

}
