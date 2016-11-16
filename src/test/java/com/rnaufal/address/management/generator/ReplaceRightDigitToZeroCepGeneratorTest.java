package com.rnaufal.address.management.generator;

import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by rnaufal on 16/11/16.
 */
public class ReplaceRightDigitToZeroCepGeneratorTest {

    @Test
    public void shouldReplaceRightDigitToZeroWithSuccess() {
        CepGenerator cepGenerator = new ReplaceRightDigitToZeroCepGenerator("78715155");
        Optional<String> actual = cepGenerator.nextAvailable();
        assertThat(actual.isPresent(), is(equalTo(true)));
        actual.ifPresent(cep -> assertThat(cep, is(equalTo("78715150"))));
    }

    @Test
    public void shouldReplaceRightDigitToZeroUntilNewCepGenerationFail() {
        CepGenerator cepGenerator = new ReplaceRightDigitToZeroCepGenerator("12345678");

        Map<String, String> cepValues = buildCepValues();

        Optional<String> actual;
        do {
            actual = cepGenerator.nextAvailable();
            if (actual.isPresent()) {
                String cep = actual.get();
                assertThat(cep, is(equalTo(cepValues.get(cep))));
            }
        } while (actual.isPresent());
        assertThat(actual.isPresent(), is(equalTo(false)));
    }

    private Map<String, String> buildCepValues() {
        return Stream.of(new SimpleEntry<>("12345670", "12345670"),
                new SimpleEntry<>("12345600", "12345600"),
                new SimpleEntry<>("12345000", "12345000"),
                new SimpleEntry<>("12340000", "12340000"),
                new SimpleEntry<>("12300000", "12300000"),
                new SimpleEntry<>("12000000", "12000000"),
                new SimpleEntry<>("10000000", "10000000"),
                new SimpleEntry<>("00000000", "00000000"))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }
}