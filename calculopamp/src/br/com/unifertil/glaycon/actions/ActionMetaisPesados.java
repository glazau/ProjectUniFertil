package br.com.unifertil.glaycon.actions;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.DynamicEntityNames;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import br.com.unifertil.glaycon.utils.ReturnEntity;

import java.math.BigDecimal;
import java.util.Collection;

public class ActionMetaisPesados implements AcaoRotinaJava {
    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JapeSession.SessionHandle hnd = null;
        Registro[] ordens = contextoAcao.getLinhas();
        if (ordens.length == 0) {
            contextoAcao.mostraErro("Selecione pelo menos uma linha.");
        } else {
            hnd = JapeSession.open();

            for (Registro registroSelcionado : ordens) {
                BigDecimal seqAmostra;
                BigDecimal sequencia;
                BigDecimal codprod;
                BigDecimal formula;
                BigDecimal formuladois;
                BigDecimal elementoMetalPesado;
                BigDecimal elementoFormula;
                BigDecimal vlrTotMicroNutri = BigDecimal.valueOf(0);
                BigDecimal vlrB = BigDecimal.valueOf(0);
                BigDecimal vlrZn = BigDecimal.valueOf(0) ;
                BigDecimal vlrMn = BigDecimal.valueOf(0) ;
                BigDecimal vlrP = BigDecimal.valueOf(0) ;
                BigDecimal garantia;
                BigDecimal resultadoFormula;
                BigDecimal valorMaximo;
                BigDecimal resultadoAnaliseMetal;
                BigDecimal elementoQuimico = BigDecimal.valueOf(0);

                String tipo;
                String faixa;
                String garantiaString;
                String formulaTabelaQuimica;
                String nomeClc;

                DynamicVO ctlAmostra;
                DynamicVO tgfPro;
                DynamicVO formulaTabela;
                DynamicVO caracteristicaAnalisavel;
                Collection<DynamicVO> analiQuimica;

                seqAmostra = (BigDecimal) registroSelcionado.getCampo("SEQAMOSTRA");
                sequencia = (BigDecimal) registroSelcionado.getCampo("SEQUENCIA");
                elementoMetalPesado = (BigDecimal) registroSelcionado.getCampo("CODELEMEN");
                resultadoAnaliseMetal = (BigDecimal) registroSelcionado.getCampo("RESULTADO");
                ReturnEntity Entity = new ReturnEntity();

                //cabeçalho controle de amostra
                ctlAmostra = Entity.getTabelaByFindOneBigDecimal("AD_CTLAMOSTRA", "SEQAMOSTRA=?", seqAmostra);
                codprod = ctlAmostra.asBigDecimalOrZero("CODPROD");

                //tabela produtos
                tgfPro = Entity.getTabelaByFindOneBigDecimal("Produto", "CODPROD=?", codprod);
                faixa = tgfPro.asString("AD_FAIXA");
                if (seqAmostra != null && sequencia != null && codprod != null) {
                    // dados formula tolerancia
                    formulaTabela = Entity.getTabelaByFindOneBigDecimalString("AD_FORMULATOL", "CODCLC = ? and TIPO=?", elementoMetalPesado, faixa);
                    formula = formulaTabela.asBigDecimalOrZero("FORMULA");
                    formuladois = formulaTabela.asBigDecimalOrZero("FORMULADOIS");
                    elementoFormula = formulaTabela.asBigDecimalOrZero("CODCLC");
                    tipo = formulaTabela.asString("TIPO");
                    valorMaximo = formulaTabela.asBigDecimalOrZero("VALORMAXIMO");

                    //dados analise quimica
                    analiQuimica = Entity.collectionVOBigDecimal("AD_ANALIQUI", "SEQAMOSTRA =? AND SEQUENCIA > 0", seqAmostra);
                    for (DynamicVO tabelaAnaliQuimica : analiQuimica) {

                        //
                        formulaTabelaQuimica = tabelaAnaliQuimica.asString("FORMULA").replace(" ","");
                        garantiaString = tabelaAnaliQuimica.asString("PERCFORMULA").replace("%", "").replace(",", ".");
                        garantia = new BigDecimal(garantiaString);
                        elementoQuimico = tabelaAnaliQuimica.asBigDecimalOrZero("CODELEMEN");
                        if (tabelaAnaliQuimica.asString("FORMULA").equals("B")) {
                            vlrB = vlrB.add(garantia);
                        } else if (tabelaAnaliQuimica.asString("FORMULA").equals("Zn")) {
                            vlrZn = vlrZn.add(garantia);
                        } else if (tabelaAnaliQuimica.asString("FORMULA").equals("Mn")) {
                            vlrMn = vlrMn.add(garantia);
                        } else if (tabelaAnaliQuimica.equals("P (CNA + H2O)")) {
                            vlrP = vlrP.add(garantia);
                           // contextoAcao.mostraErro(" " + vlrP);
                        }
                    }

                    //caracteristica analisavel
                    JapeWrapper caracteristicaDao = JapeFactory.dao("CaracteristicaAnalisavel");
                    caracteristicaAnalisavel = caracteristicaDao.findOne("CODCLC=?",elementoQuimico);
                    nomeClc = caracteristicaAnalisavel.asString("NOMECLC");
                    // vlr micro nutrientes
                    vlrTotMicroNutri = vlrTotMicroNutri.add(vlrB).add(vlrZn).add(vlrMn);

                    // faixa 1
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX")) {
                        resultadoFormula = vlrTotMicroNutri.multiply(formula);
                        if (valorMaximo.compareTo(BigDecimal.valueOf(0)) == 0) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == -1) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 1) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 0) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        }
                    }
                    //faixa 2
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX2")) {
                        resultadoFormula = vlrP.multiply(formula);
                        registroSelcionado.setCampo("LIMITE", resultadoFormula);
                        registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                    }

                    //faixa 3
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX3")) {
                        resultadoFormula = vlrP.multiply(formula);
                        if (valorMaximo.compareTo(BigDecimal.valueOf(0)) == 0) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == -1) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 1) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 0) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        }
                    }

                    //faixa 4
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX4")) {
                        resultadoFormula = vlrTotMicroNutri.multiply(formula).add(vlrP.multiply(formuladois));
                      //  contextoAcao.mostraErro("resutlado formula" + resultadoFormula + "\n vlrtotmicronutr" + vlrTotMicroNutri + "\n formula" + formula + "\n vlrP" + vlrP + "\nformuladois" + formuladois);
                        if (valorMaximo.compareTo(BigDecimal.valueOf(0)) == 0) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == -1) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 1) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 0) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        }
                    }

                    //faixa 5
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX5")) {
                        resultadoFormula = vlrTotMicroNutri.multiply(formula).add(formuladois);
                        if (valorMaximo.compareTo(BigDecimal.valueOf(0)) == 0) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == -1) {
                            registroSelcionado.setCampo("LIMITE", resultadoFormula);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 1) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        } else if (resultadoFormula.compareTo(valorMaximo) == 0) {
                            registroSelcionado.setCampo("LIMITE", valorMaximo);
                            registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,valorMaximo));
                        }
                    }

                    //faixa 6
                    if (elementoFormula.compareTo(elementoMetalPesado) == 0 && faixa.equals(tipo) && faixa.equals("FX6")) {
                        resultadoFormula = formula;
                        registroSelcionado.setCampo("LIMITE", resultadoFormula);
                        registroSelcionado.setCampo("CONTAMINADOS",verificaContaminados(resultadoAnaliseMetal,resultadoFormula));

                    }
                }
            }
        }
    }
    private String verificaContaminados (BigDecimal resultadoAnaliseMetal, BigDecimal resultadoFormula){
        if(resultadoAnaliseMetal.compareTo(resultadoFormula)==1){
            return "1";
        } else if (resultadoAnaliseMetal.compareTo(resultadoFormula)==-1){
            return "2";
        } else {
           return "1";
        }
    }


}
