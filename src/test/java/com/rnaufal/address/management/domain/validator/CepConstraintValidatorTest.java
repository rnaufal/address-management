package com.rnaufal.address.management.domain.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by rnaufal on 16/11/16.
 */
@RunWith(Parameterized.class)
public class CepConstraintValidatorTest {

    @Parameterized.Parameters(name = "input:{0} - valid:{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"78715155", true},
                {"", false},
                {"-", false},
                {"abc", false},
                {"1234567-", false},
                {"456a7890", false},
                {"7890", false}
        });
    }

    private final String input;

    private final boolean expected;

    public CepConstraintValidatorTest(String input, boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void validateCep() {
        CepConstraintValidator validator = new CepConstraintValidator();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean actual = validator.isValid(input, context);
        assertThat(actual, is(equalTo(expected)));
    }
}