package br.com.unifertil.glaycon.events;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import br.com.unifertil.glaycon.utils.*;

import java.math.BigDecimal;
import java.util.Collection;

public class InsereMetaisPesados implements EventoProgramavelJava {
    @Override
    public void beforeInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeUpdate(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterInsert(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void afterUpdate(PersistenceEvent persistenceEvent) throws Exception {
        InsereMetais(persistenceEvent);
    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
    private void InsereMetais(PersistenceEvent event) throws Exception{

        JapeSession.SessionHandle hnd = null;
       try{
        String analiseContaminante;
        String tpAnaliseMetal = "3";
        BigDecimal seqAmostra;
        Collection<DynamicVO> collectionAnaliseMetal;
        DynamicVO tabelaControleAmostra;
        ReturnEntity entiityUtils = new ReturnEntity();
        PerfilMetal perfilMetal = new PerfilMetal();

        tabelaControleAmostra = (DynamicVO) event.getVo();
        seqAmostra = tabelaControleAmostra.asBigDecimalOrZero("SEQAMOSTRA");
        analiseContaminante = tabelaControleAmostra.asString("ANALICONT");

        if (analiseContaminante.equals("S")) {
        JapeWrapper analiseMetalDao = JapeFactory.dao("AD_ANALIMET");
        DynamicVO analimetVO = analiseMetalDao.findOne("SEQAMOSTRA=?",new Object[]{seqAmostra});

        if(analimetVO==null){
        collectionAnaliseMetal = entiityUtils.collectionVOString("CaracteristicaAnalisavel","AD_TP_ANALISE=?",tpAnaliseMetal);
        for (DynamicVO tabelaCaracteristicaAnalisavel : collectionAnaliseMetal){
        if(tabelaCaracteristicaAnalisavel.asString("AD_TP_ANALISE").equals(tpAnaliseMetal)){

        perfilMetal.setCodElemento(tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("CODCLC"));
        perfilMetal.setMetodo(tabelaCaracteristicaAnalisavel.asString("AD_METODO"));
        perfilMetal.setObservacao(tabelaCaracteristicaAnalisavel.asString("OBSERVACAO"));
        perfilMetal.setSimbolo(tabelaCaracteristicaAnalisavel.asString("AD_SIMBOLO"));

            FluidCreateVO itemAnaliseMetal = analiseMetalDao.create();

            itemAnaliseMetal.set("SEQAMOSTRA",seqAmostra);
            itemAnaliseMetal.set("CODELEMEN",perfilMetal.getCodElemento());
            itemAnaliseMetal.set("METODO",perfilMetal.getMetodo());
            itemAnaliseMetal.set("OBSERVACAO",perfilMetal.getObservacao());
            itemAnaliseMetal.set("SIMBOLO",perfilMetal.getSimbolo());
            itemAnaliseMetal.save();
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

private void exibirErro(String mensagem) throws  Exception{
        throw new Exception(mensagem);
}

}
