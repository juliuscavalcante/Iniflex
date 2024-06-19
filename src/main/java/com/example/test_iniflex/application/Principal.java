package com.example.test_iniflex.application;

import com.example.test_iniflex.model.entities.Funcionario;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Principal {

    public static void main(String[] args) {
        String caminhoDoArquivo = "src/main/resources/funcionarios.txt";

        try {

            System.out.println("\n3.1 - Inserir todos os funcionários, na mesma ordem e informações da tabela.");
            List<Funcionario> funcionarios = lerDadosFuncionarios(caminhoDoArquivo);

            System.out.println("\n3.2 - Remover o funcionário \"João\" da lista.");
            funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

            System.out.println("\n3.3 - Imprimir todos os funcionários com todas suas informações, com data e salário " +
                    "formatados.");
            informacoesFuncionarios(funcionarios);

            System.out.println("\n3.4 - Os funcionários receberam 10% de aumento de salário, atualizar a lista de " +
                    "funcionários com novo valor.");
            aplicarAumento(funcionarios, 10);
            System.out.println("Salários aumentados em 10%:");
            informacoesFuncionarios(funcionarios);

            System.out.println("\n3.5 - Agrupar os funcionários por função em um MAP, sendo a chave a “função” " +
                    "e o valor a “lista de funcionários”.");
            Map<String, List<Funcionario>> funcionariosPorFuncao = new HashMap<>();
            funcionariosPorFuncao = criarMapFuncionariosFuncao(funcionarios,funcionariosPorFuncao);

            System.out.println("\n3.6 - Imprimir os funcionários, agrupados por função.");
            for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {
                System.out.println("Função: " + entry.getKey());
                for (Funcionario f : entry.getValue()) {
                    System.out.println(" - " + f.getNome());
                }
            }

            System.out.println("\n3.8 - Imprimir os funcionários que fazem aniversário no mês 10 e 12.");
            getAniversariantesDoMes(funcionarios, 10);
            getAniversariantesDoMes(funcionarios, 12);

            System.out.println("\n3.9 - Imprimir o funcionário com a maior idade, exibir os atributos: nome e idade.");
            imprimirFuncionarioMaisVelho(funcionarios);

            System.out.println("\n3.10 - Imprimir a lista de funcionários por ordem alfabética.");
            listarFuncionariosAlfabetica(funcionarios);

            System.out.println("\n3.11 - Imprimir o total dos salários dos funcionários.");
            calculaSalarios(funcionarios);

            //
            System.out.println("\n3.12 - Imprimir quantos salários mínimos ganha cada funcionário, " +
                    "considerando que o salário mínimo é R$1212.00.");
            BigDecimal salarioMinimo = new BigDecimal("1212.00");
            imprimirSalariosMinimos(funcionarios, salarioMinimo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lê os dados dos funcionários de um arquivo de texto separados por ';' e cria uma lista de objetos {@code Funcionario}.
     *
     * @param caminhoDoArquivo O caminho para o arquivo de texto contendo os dados dos funcionários.
     * @return Uma lista de objetos {@code Funcionario} com os dados lidos do arquivo.
     * @throws IOException Se ocorrer um erro ao ler o arquivo.
     */
    public static List<Funcionario> lerDadosFuncionarios(String caminhoDoArquivo) throws IOException {
        List<Funcionario> funcionarios = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(caminhoDoArquivo);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1))) {

            reader.readLine();
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] campos = linha.split(";");
                String nome = campos[0];
                LocalDate dataNascimento = LocalDate.parse(campos[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                BigDecimal salario = new BigDecimal(campos[2]);
                String funcao = campos[3];

                Funcionario funcionario = new Funcionario(nome, dataNascimento, salario, funcao);
                funcionarios.add(funcionario);
            }
        }

        return funcionarios;
    }

    /**
     * Imprime a lista de funcionários com suas informações formatadas.
     *
     * @param funcionarios A lista de funcionários a ser impressa.
     */
    public static void informacoesFuncionarios(List<Funcionario> funcionarios) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        for (Funcionario funcionario : funcionarios) {
            System.out.println("Nome: " + funcionario.getNome() +
                    "       Data de Nascimento: " + funcionario.getDataNascimento().format(dateTimeFormatter) +
                    "       Salário: R$ " + decimalFormat.format(funcionario.getSalario()) +
                    "       Função: " + funcionario.getFuncao());
        }
    }

     /**
     * Aplica um aumento percentual ao salário a uma lista de funcionários.
     * @param funcionarios A lista de funcionários.
     * @param percentual O percentual de aumento a ser aplicado.
     */
     public static void aplicarAumento(List<Funcionario> funcionarios, double percentual) {

         BigDecimal fatorDeAumento = BigDecimal.valueOf(1 + percentual / 100);

         for (Funcionario funcionario : funcionarios) {
             BigDecimal aumento = funcionario.getSalario().multiply(fatorDeAumento).subtract(funcionario.getSalario());
             funcionario.setSalario(funcionario.getSalario().add(aumento));
         }
     }

    /**
     * Agrupa em um Map os funcionários por função.
     *
     * @param funcionarios A lista de funcionários.
     * @return
     */

    public static Map<String, List<Funcionario>> criarMapFuncionariosFuncao(List<Funcionario> funcionarios, Map<String,
            List<Funcionario>> funcionariosPorFuncao) {

        for (Funcionario funcionario : funcionarios) {
            if (!funcionariosPorFuncao.containsKey(funcionario.getFuncao())) {
                funcionariosPorFuncao.put(funcionario.getFuncao(), new ArrayList<>());
            }
            funcionariosPorFuncao.get(funcionario.getFuncao()).add(funcionario);
        }

        return funcionariosPorFuncao;
    }

    /**
     * Imprime os nomes dos funcionários que fazem aniversário no mês correspondente.
     * @param funcionarios A lista de funcionários.
     * @param meses Os meses para verificar aniversários (1-12).*/

    public static List<Funcionario> getAniversariantesDoMes(List<Funcionario> funcionarios, int mes) {
        List<Funcionario> aniversariantes = new ArrayList<>();

        System.out.println("Os aniversariantes do mês "+mes+" são:");

        for (Funcionario funcionario : funcionarios) {
            LocalDate dataDeNascimento = funcionario.getDataNascimento();
            if (dataDeNascimento.getMonthValue() == mes) {
                aniversariantes.add(funcionario);
                System.out.println(funcionario.getNome());
            }
        }

        return aniversariantes;
    }

    /**
     * Imprime o nome e a idade do funcionário mais velho.
     * @param funcionarios A lista de funcionários.*/

    public static void imprimirFuncionarioMaisVelho(List<Funcionario> funcionarios) {
        Funcionario maisVelho = null;
        int maiorIdade = -1;

        for (Funcionario funcionario : funcionarios) {
            LocalDate dataDeNascimento = funcionario.getDataNascimento();
            int idade = LocalDate.now().getYear() - dataDeNascimento.getYear();

            if (idade > maiorIdade) {
                maisVelho = funcionario; //Só será atualizado caso encontre um funcionário mais velho
                maiorIdade = idade;
            }
        }

        if (maisVelho != null) {
            System.out.println("Funcionário com a maior idade: ");
            System.out.println("Nome: " + maisVelho.getNome());
            System.out.println("Idade: " + maiorIdade);
        }
    }

    //3.10 – Ordem alfabética:

    /**
     * Imprime a lista de funcionários em ordem alfabética.
     * @param funcionarios A lista de funcionários.*/

    public static void listarFuncionariosAlfabetica(List<Funcionario> funcionarios) {
        funcionarios.sort((Funcionario f1, Funcionario f2) -> f1.getNome().compareTo(f2.getNome()));

        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario.getNome());
        }
    }

    //3.11 – Total dos salários:

    /**
     * Calcula e imprime o total dos salários dos funcionários.
     * @param funcionarios A lista de funcionários.*/

    public static void calculaSalarios(List<Funcionario> funcionarios) {
        BigDecimal salarioTotal = BigDecimal.ZERO;

        for (Funcionario funcionario : funcionarios) {
            salarioTotal = salarioTotal.add(funcionario.getSalario());
        }

        System.out.println(String.format("Salário total dos funcionários: R$%,.2f", salarioTotal));
    }

    //3.12 – Salários mínimos por funcionário:

    /**
     * Calcula e imprime quantos salários mínimos cada funcionário ganha.
     * @param funcionarios A lista de funcionários.
     * @param salarioMinimo O valor do salário mínimo para cálculo.*/

    public static void imprimirSalariosMinimos(List<Funcionario> funcionarios, BigDecimal salarioMinimo) {

        for (Funcionario funcionario : funcionarios) {

            BigDecimal salario = funcionario.getSalario();
            BigDecimal qntSalariosMin = salario.divide(salarioMinimo, 1, RoundingMode.HALF_UP);
            System.out.println(funcionario.getNome() + " recebe " + qntSalariosMin + " salários mínimos.");
        }
    }
}