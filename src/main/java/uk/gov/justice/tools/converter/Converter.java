package uk.gov.justice.tools.converter;

public interface Converter<To, From> {
    To convert(From from) ;
}
