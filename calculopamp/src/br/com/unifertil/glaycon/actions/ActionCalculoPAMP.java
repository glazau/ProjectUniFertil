package br.com.unifertil.glaycon.actions;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.PersistenceException;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import java.math.BigDecimal;
import java.sql.ResultSet;

public class ActionCalculoPAMP implements AcaoRotinaJava {

    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JapeSession.SessionHandle hnd = null;
        JdbcWrapper jdbcWrapper;
        NativeSql nativeSql = null;
        try {
            hnd = JapeSession.open();
            Registro[] ordens = contextoAcao.getLinhas();
            if (ordens.length == 0) {
                contextoAcao.mostraErro("Selecione pelo menos uma linha.");
            } else {
                for (Registro registroSelcionado : ordens) {
                    BigDecimal seqAmostra = (BigDecimal) registroSelcionado.getCampo("SEQAMOSTRA");
                    BigDecimal sequencia = (BigDecimal) registroSelcionado.getCampo("SEQUENCIA");
                    String resultadoAnaliseQuimicaStringComVirgula = (String) registroSelcionado.getCampo("RESULTADO");
                    if(resultadoAnaliseQuimicaStringComVirgula==null){
                        throw new PersistenceException("Preencha o campo resultado");
                    }
                    String resultadoAnaliseQuimicaString = resultadoAnaliseQuimicaStringComVirgula.replace(',','.');
                    BigDecimal tolerancia;
                    DynamicVO controleAmostra = getControleAmostra(seqAmostra);
                    BigDecimal codprod = controleAmostra.asBigDecimalOrZero("CODPROD");

                    if (seqAmostra != null && sequencia != null && codprod != null) {
                        EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
                        jdbcWrapper = dwfFacade.getJdbcWrapper();
                        nativeSql = new NativeSql(jdbcWrapper);
                        nativeSql.loadSql(getClass(), "ActionCalculoPAMP.sql");
                        nativeSql.setNamedParameter("CODPROD", codprod);
                        nativeSql.setNamedParameter("SEQAMOSTRA", seqAmostra);
                        nativeSql.setNamedParameter("SEQUENCIA", sequencia);
                        ResultSet rs = nativeSql.executeQuery();
                        while (rs.next()) {
                            BigDecimal resultadoAnaliseQuimica = new BigDecimal(resultadoAnaliseQuimicaString);
                            String garantiaString = rs.getString("PERCFORMULA");
                            BigDecimal garantia = new BigDecimal(garantiaString);
                            BigDecimal formula = rs.getBigDecimal("FORMULA");
                            BigDecimal formulaDois = rs.getBigDecimal("FORMULADOIS");
                            BigDecimal resultadoDentroTolerancia;
                            boolean  teorMaior40 = garantia.compareTo(BigDecimal.valueOf(40))==0 || garantia.compareTo(BigDecimal.valueOf(40))==1;
                            if ((garantia.compareTo(BigDecimal.valueOf(40)) == 1) || garantia.compareTo(BigDecimal.valueOf(40)) == 0) {
                                resultadoDentroTolerancia =  garantia.subtract(formula);
                                tolerancia = garantia.subtract(resultadoDentroTolerancia);
                            } else {
                                tolerancia = garantia.multiply(formula).add(formulaDois);
                                resultadoDentroTolerancia = garantia.subtract(tolerancia);
                            }
                            BigDecimal deficienciaGrave = garantia.subtract(tolerancia.multiply(BigDecimal.valueOf(1.5)));
                            BigDecimal deficienciaGravissima = garantia.subtract(tolerancia.multiply(BigDecimal.valueOf(3)));
                            if (((resultadoAnaliseQuimica.compareTo(resultadoDentroTolerancia) == 1) || (resultadoAnaliseQuimica.compareTo(resultadoDentroTolerancia) == 0)) && !teorMaior40) {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '1');
                            }else if((resultadoAnaliseQuimica.compareTo(resultadoDentroTolerancia) == 1) && teorMaior40) {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '1');
                            }else if((resultadoAnaliseQuimica.compareTo(resultadoDentroTolerancia) == 0) && teorMaior40) {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '2');
                            }else if ((resultadoAnaliseQuimica.compareTo(resultadoDentroTolerancia) == -1) && resultadoAnaliseQuimica.compareTo(deficienciaGrave) == 1) {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '2');
                            } else if (((resultadoAnaliseQuimica.compareTo(deficienciaGrave) == -1) && resultadoAnaliseQuimica.compareTo(deficienciaGravissima) == 1) || resultadoAnaliseQuimica.compareTo(deficienciaGrave) == 0) {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '3');
                            } else {
                                registroSelcionado.setCampo("TOLERANCIA", tolerancia);
                                registroSelcionado.setCampo("CLASSIFICACAO", '4');
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            JapeSession.close(hnd);
        }


    }


    private DynamicVO getControleAmostra(BigDecimal seqAmostra) throws Exception {
        JapeWrapper DAO = JapeFactory.dao("AD_CTLAMOSTRA");
        DynamicVO Vo = DAO.findOne("SEQAMOSTRA = ?", new Object[]{seqAmostra});
        return Vo;
    }
}
