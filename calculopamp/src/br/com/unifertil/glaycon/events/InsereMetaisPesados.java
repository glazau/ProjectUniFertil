package br.com.unifertil.glaycon.events;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
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
        String analiseContaminante;
        String tpAnaliseMetal = "3";
        BigDecimal codProd;
        BigDecimal seqAmostra;
        BigDecimal sequenciaPerfilAnalisavel;
        Collection<DynamicVO> collectionAnaliseMetal;
        DynamicVO tabelaControleAmostra;
        ReturnEntity entiityUtils = new ReturnEntity();
        PerfilMetal perfilMetal = new PerfilMetal();

        tabelaControleAmostra = (DynamicVO) event.getVo();

        codProd = tabelaControleAmostra.asBigDecimalOrZero("CODPROD");
        seqAmostra = tabelaControleAmostra.asBigDecimalOrZero("SEQAMOSTRA");
        analiseContaminante = tabelaControleAmostra.asString("ANALICONT");
        if (analiseContaminante.equals("S")) {

        collectionAnaliseMetal = entiityUtils.collectionVOString("CaracteristicaAnalisavel","AD_TP_ANALISE=?",tpAnaliseMetal);
        for (DynamicVO tabelaPerfilMetal : collectionAnaliseMetal){

        perfilMetal.setCodElemento(tabelaPerfilMetal.asBigDecimalOrZero("CODCLC"));
        perfilMetal.setMetodo(tabelaPerfilMetal.asString("AD_METODO"));
        perfilMetal.setObservacao(tabelaPerfilMetal.asString("OBSERVACAO"));
        perfilMetal.setSimbolo(tabelaPerfilMetal.asString("AD_SIMBOLO"));

        JapeWrapper analiseMetalDao = JapeFactory.dao("AD_ANALIMET");

        FluidCreateVO itemAnaliseQuimica = analiseMetalDao.create();
        itemAnaliseQuimica.set("PERCFORMULA",perfilMetal.getGarantia());
        itemAnaliseQuimica.set("SEQAMOSTRA",seqAmostra);
        itemAnaliseQuimica.set("CODELEMEN",perfilMetal.getCodElemento());
        itemAnaliseQuimica.set("METODO",perfilMetal.getMetodo());
        itemAnaliseQuimica.save();

            }

        }
    }



}
