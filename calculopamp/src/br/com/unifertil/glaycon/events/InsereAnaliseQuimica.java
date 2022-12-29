package br.com.unifertil.glaycon.events;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.PersistenceException;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import br.com.unifertil.glaycon.utils.ReturnEntity;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.Collection;

public class InsereAnaliseQuimica implements EventoProgramavelJava {
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
        InsereQuimico(persistenceEvent);
    }

    @Override
    public void afterDelete(PersistenceEvent persistenceEvent) throws Exception {

    }

    @Override
    public void beforeCommit(TransactionContext transactionContext) throws Exception {

    }
    private void InsereQuimico(PersistenceEvent event) throws Exception {
        JapeSession.SessionHandle hnd = null;

        Boolean entrouIfAnaliseQuimica  = false;


        DynamicVO tabelaResultadoAnalise;
        DynamicVO resultadoVO;

        String garantiaString;
        String BR = null;
        String resultadoString;

        BigDecimal seqAmostra;
        BigDecimal B = BigDecimal.valueOf(0);
        BigDecimal S= BigDecimal.valueOf(0);
        BigDecimal Ca= BigDecimal.valueOf(0);
        BigDecimal Zn= BigDecimal.valueOf(0);
        BigDecimal Mn= BigDecimal.valueOf(0);
        BigDecimal nTotal= BigDecimal.valueOf(0);
        BigDecimal pCnaH2o= BigDecimal.valueOf(0);
        BigDecimal potassio= BigDecimal.valueOf(0);
        BigDecimal somaNpk= BigDecimal.valueOf(0);
        BigDecimal garantia = null;
        BigDecimal bResultado = BigDecimal.valueOf(0);
        BigDecimal sResultado = BigDecimal.valueOf(0);
        BigDecimal caResultado = BigDecimal.valueOf(0);
        BigDecimal znResultado = BigDecimal.valueOf(0);
        BigDecimal mnResultado = BigDecimal.valueOf(0);
        BigDecimal nTotalResultado = BigDecimal.valueOf(0);
        BigDecimal pCnaH2oResultado= BigDecimal.valueOf(0);
        BigDecimal potassioResultado= BigDecimal.valueOf(0);
        BigDecimal somaNpkResultado= BigDecimal.valueOf(0);
        BigDecimal resultadoBigDecimal = BigDecimal.valueOf(0);

        Collection<DynamicVO> collectionAnaliseQuimica;

        ReturnEntity entiityUtils = new ReturnEntity();

        tabelaResultadoAnalise = (DynamicVO) event.getVo();
        seqAmostra = tabelaResultadoAnalise.asBigDecimalOrZero("SEQAMOSTRA");

        JapeWrapper resultadoDao = JapeFactory.dao("AD_RESULTANA");
        resultadoVO = resultadoDao.findOne("SEQAMOSTRA=?", new Object[]{seqAmostra});

        if (resultadoVO == null) {
        collectionAnaliseQuimica = entiityUtils.collectionVOBigDecimal("AD_ANALIQUI", "SEQAMOSTRA=?", seqAmostra);
        for (DynamicVO tabelaAnaliMQui : collectionAnaliseQuimica) {
            if(tabelaAnaliMQui.asString("FORMULA")!=null){
            if(tabelaAnaliMQui.asString("PERCFORMULA")!= null ) {
                garantiaString = tabelaAnaliMQui.asString("PERCFORMULA").replace("%", "").replace(",", ".");
                garantia = new BigDecimal(garantiaString);
            } else {
             //   ReturnEntity.exibirErro("Favor preencher o campo Garantia, do elemento: " + tabelaAnaliMQui.asString("FORMULA"));
            }
            if (tabelaAnaliMQui.asString("RESULTADO") != null ){
                resultadoString = tabelaAnaliMQui.asString("RESULTADO").replace("%", "").replace(",", ".");
                resultadoBigDecimal = new BigDecimal(resultadoString);
                } else {
               // ReturnEntity.exibirErro("Favor preencher o campo Resultado do elemento: " + tabelaAnaliMQui.asString("FORMULA") );
            }
                if (tabelaAnaliMQui.asString("FORMULA").equals("Ca")) {
                    Ca = Ca.add(garantia);
                    caResultado = caResultado.add(resultadoBigDecimal);
                    entrouIfAnaliseQuimica = true;
                }


                if (tabelaAnaliMQui.asString("FORMULA").equals("B")) {
                    B = B.add(garantia);
                    bResultado = bResultado.add(resultadoBigDecimal);
                    entrouIfAnaliseQuimica = true;
                }

                if (tabelaAnaliMQui.asString("FORMULA").equals("S(T)")) {
                    S = S.add(garantia);
                    sResultado = sResultado.add(resultadoBigDecimal);
                    entrouIfAnaliseQuimica = true;
                }
                if (tabelaAnaliMQui.asString("FORMULA").equals("Zn")) {
                    Zn = Zn.add(garantia);
                    znResultado = znResultado.add(resultadoBigDecimal);
                    entrouIfAnaliseQuimica = true;
                }

                if (tabelaAnaliMQui.asString("FORMULA").equals("Mn")) {
                    Mn = Mn.add(garantia);
                    mnResultado = mnResultado.add(resultadoBigDecimal);
                    entrouIfAnaliseQuimica = true;
                    }
                if(tabelaAnaliMQui.asBigDecimalOrZero("CODELEMEN")!=null) {
                    if (tabelaAnaliMQui.asBigDecimalOrZero("CODELEMEN").compareTo(BigDecimal.valueOf(1)) == 0) {
                        nTotal = nTotal.add(garantia);
                        nTotalResultado = nTotalResultado.add(resultadoBigDecimal);
                        entrouIfAnaliseQuimica = true;
                    }
                    if (tabelaAnaliMQui.asBigDecimalOrZero("CODELEMEN").compareTo(BigDecimal.valueOf(5)) == 0) {
                        pCnaH2o = pCnaH2o.add(garantia);
                        pCnaH2oResultado = pCnaH2oResultado.add(resultadoBigDecimal);
                        entrouIfAnaliseQuimica = true;
                    }
                    if (tabelaAnaliMQui.asBigDecimalOrZero("CODELEMEN").compareTo(BigDecimal.valueOf(6)) == 0) {
                        potassio = potassio.add(garantia);
                        potassioResultado = potassioResultado.add(resultadoBigDecimal);
                        entrouIfAnaliseQuimica = true;
                    }
                    if (tabelaAnaliMQui.asBigDecimalOrZero("CODELEMEN").compareTo(BigDecimal.valueOf(7)) == 0) {
                        somaNpk = somaNpk.add(garantia);
                        somaNpkResultado = somaNpkResultado.add(resultadoBigDecimal);
                        entrouIfAnaliseQuimica = true;
                    }
                }

            }

        }

        if (entrouIfAnaliseQuimica == true){
            FluidCreateVO itemAnaliseQuimica = resultadoDao.create();

            itemAnaliseQuimica.set("SEQAMOSTRA", seqAmostra);
            itemAnaliseQuimica.set("CAG",Ca);
            itemAnaliseQuimica.set("CAR",caResultado);
            itemAnaliseQuimica.set("BG",B);
            itemAnaliseQuimica.set("BR",bResultado);
            itemAnaliseQuimica.set("SG",S);
            itemAnaliseQuimica.set("SR",sResultado);
            itemAnaliseQuimica.set("ZNG",Zn);
            itemAnaliseQuimica.set("ZNR",znResultado);
            itemAnaliseQuimica.set("MNG",Mn);
            itemAnaliseQuimica.set("MNR",mnResultado);


            itemAnaliseQuimica.set("NTOTG",nTotal);
            itemAnaliseQuimica.set("NTOTR",nTotalResultado);

            itemAnaliseQuimica.set("PCNAH2OG",pCnaH2o);
            itemAnaliseQuimica.set("PCNAH2OR",pCnaH2oResultado);

            itemAnaliseQuimica.set("K2OG",potassio);
            itemAnaliseQuimica.set("K2OR",potassioResultado);


            itemAnaliseQuimica.set("SOMANPKG",somaNpk);
            itemAnaliseQuimica.set("SOMANPKR",somaNpkResultado);




            itemAnaliseQuimica.save();
                }
            }
        }
}
