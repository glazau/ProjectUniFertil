package br.com.unifertil.glaycon.events;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.PersistenceException;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.unifertil.glaycon.utils.ReturnEntity;

import java.math.BigDecimal;

public class ValidaExclusaoContaminantes implements EventoProgramavelJava {
    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {
        validaExclusao(persistenceEvent);
    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }

    private void validaExclusao(PersistenceEvent event) throws Exception {
        JapeSession.SessionHandle hnd = null;
        JapeSession.open();
        try{
        DynamicVO analiseMetal = (DynamicVO) event.getVo();
        ReturnEntity entiityUtils = new ReturnEntity();
        BigDecimal seqAmostra = analiseMetal.asBigDecimalOrZero("SEQAMOSTRA");
        BigDecimal sequencia = analiseMetal.asBigDecimalOrZero("SEQUENCIA");
        DynamicVO tabelaControleAmostra = entiityUtils.getTabelaByFindOneBigDecimal("AD_CTLAMOSTRA", "SEQAMOSTRA=? AND CODPROD>0", seqAmostra);
        String analiseContaminante;
        analiseContaminante = tabelaControleAmostra.asString("ANALICONT");

            if (analiseMetal != null) {

                if (tabelaControleAmostra != null) {
                    if (seqAmostra != null) {
                        if (analiseContaminante.equals("S") && sequencia != null) {
                             throw new PersistenceException();
                        }
                    }
                }
            }

        } catch(Exception e){
            throw new PersistenceException("Não é possível excluir o Metal, o campo <b>Analisar Contaminantes</b> está marcado como <b>sim</b>");
        } finally {
            JapeSession.close(hnd);
        }

    }

}
