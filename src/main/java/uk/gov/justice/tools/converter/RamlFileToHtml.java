package uk.gov.justice.tools.converter;


import uk.gov.justice.tools.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import raml.tools.html.Raml2HtmlConverter;

public class RamlFileToHtml implements Converter<Html, File> {
    private Config config;

    public RamlFileToHtml(Config config) {
        this.config = config;
    }

    @Override
    public Html convert(File file) {
        InputStream ramlInput = null;
        try {
            ramlInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Raml2HtmlConverter raml2HtmlConverter = new Raml2HtmlConverter();
        raml2HtmlConverter.withRamlBasePath(file.getParent());
        return new Html(raml2HtmlConverter.convert(ramlInput, new ByteArrayOutputStream()).toString(),
                config.getRamlReportDirectory() + file.getName().replace("raml", "html"));
    }
}
