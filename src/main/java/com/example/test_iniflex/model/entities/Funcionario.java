package com.example.test_iniflex.model.entities;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


public class Funcionario extends Pessoa {

    @Serial
    private static final long serialVersionUID = 5803130385852025791L;

    protected BigDecimal salario;
    protected String funcao;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = salario;
        this.funcao = funcao;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Funcionario that = (Funcionario) o;

        if (!Objects.equals(salario, that.salario)) return false;
        return Objects.equals(funcao, that.funcao);
    }

    @Override
    public int hashCode() {
        int result = salario != null ? salario.hashCode() : 0;
        result = 31 * result + (funcao != null ? funcao.hashCode() : 0);
        return result;
    }
}
