package com.techchallenge.cliente.domain.validation;

public final class CpfValidatorUtil {
    private CpfValidatorUtil(){}

    public static String normalize(String raw){
        return raw == null ? null : raw.replaceAll("\\D","");
    }

    public static boolean isValid(String raw){
        if(raw == null) return false;
        String cpf = normalize(raw);
        if(cpf.length()!=11 || cpf.chars().distinct().count()==1) return false;
        int d1 = calcDig(cpf, 10);
        int d2 = calcDig(cpf, 11);
        return cpf.endsWith("" + d1 + d2);
    }

    private static int calcDig(String base, int pesoInicial){
        int soma=0;
        for(int i=0;i<pesoInicial-1;i++){
            soma += Character.getNumericValue(base.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return (resto < 2)?0:11-resto;
    }
}
