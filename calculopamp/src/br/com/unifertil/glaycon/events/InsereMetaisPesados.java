package br.com.unifertil.glaycon.events;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.unifertil.glaycon.utils.*;

import java.math.BigDecimal;

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
        String tp_analisavel;
        BigDecimal codProd;
        BigDecimal sequenciaPerfilAnalisavel;
        DynamicVO getPerfilAnalisavel;
        DynamicVO tabelaPerfilMetal;
        DynamicVO ad_ctlamostra;
        ReturnEntity entiityUtils = new ReturnEntity();
        PerfilMetal perfilMetal = new PerfilMetal();

        ad_ctlamostra = (DynamicVO) event.getVo();

        codProd = ad_ctlamostra.asBigDecimalOrZero("CODPROD");
        analiseContaminante = ad_ctlamostra.asString("ANALICONT");

        getPerfilAnalisavel =  entiityUtils.getTabelaByFindOneBigDecimal("AD_PERFILANALISAVEL","CODPROD=?",codProd);
        sequenciaPerfilAnalisavel = getPerfilAnalisavel.asBigDecimalOrZero("SEQUENCIA");

        tabelaPerfilMetal = entiityUtils.getTabelaByFindOneBigDecimal("AD_PERFILANALISAVEL","SEQUENCIA=?",sequenciaPerfilAnalisavel);

        perfilMetal.setGarantia(tabelaPerfilMetal.asString("GARANTIA"));
        perfilMetal.setCodElemento(tabelaPerfilMetal.asBigDecimalOrZero("CODELEMENTO"));
        perfilMetal.setSequencia(tabelaPerfilMetal.asBigDecimalOrZero("SEQUENCIA"));
        perfilMetal.setMetodo(tabelaPerfilMetal.asString("METODO"));
        perfilMetal.setGarantia(tabelaPerfilMetal.asString("GARANTIA"));
        perfilMetal.setDescNutri(tabelaPerfilMetal.asString("DESCNUTRI"));
        perfilMetal.setVlrmax(tabelaPerfilMetal.asBigDecimalOrZero("VLRMAX"));
        perfilMetal.setVlrmin(tabelaPerfilMetal.asBigDecimalOrZero("VLRMIN"));



    }



}
