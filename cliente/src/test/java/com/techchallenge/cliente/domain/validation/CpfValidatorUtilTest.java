package com.techchallenge.cliente.domain.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorUtilTest {

    @Test
    void deveValidarCpfCorreto(){
        assertTrue(CpfValidatorUtil.isValid("390.533.447-05"));
    }

    @Test
    void deveInvalidarCpfComDigitosRepetidos(){
        assertFalse(CpfValidatorUtil.isValid("111.111.111-11"));
    }

    @Test
    void deveInvalidarCpfComTamanhoErrado(){
        assertFalse(CpfValidatorUtil.isValid("1234567890"));
    }

    @Test
    void normalizeRemoveMascara(){
        assertEquals("39053344705", CpfValidatorUtil.normalize("390.533.447-05"));
    }

    @Test
    void deveRetornarFalseQuandoNull(){
        assertFalse(CpfValidatorUtil.isValid(null));
    }
}
