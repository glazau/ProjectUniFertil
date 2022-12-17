package br.com.unifertil.glaycon.actions;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidUpdateVO;
import br.com.unifertil.glaycon.utils.ReturnEntity;

import java.math.BigDecimal;
import java.util.Collection;

public class ActionFinalizarAnaliseMet implements AcaoRotinaJava {

    @Override
    public void doAction(ContextoAcao contextoAcao) throws Exception {
        JapeSession.SessionHandle hnd = null;
        Registro[] ordens = contextoAcao.getLinhas();
        ReturnEntity Entity = new ReturnEntity();
        Boolean  resultado_limitenull = null;
        if (ordens.length >1) {
            contextoAcao.mostraErro("Selecione somente uma linha");
        } else {
            hnd = JapeSession.open();
            for (Registro registroSelcionado : ordens) {
                BigDecimal seqAmostra = (BigDecimal) registroSelcionado.getCampo("SEQAMOSTRA");
                Collection<DynamicVO> collectionAnaliseMetal;
                collectionAnaliseMetal = Entity.collectionVOBigDecimal("AD_ANALIMET","SEQAMOSTRA=?",seqAmostra);
                for (DynamicVO tabelaCaracteristicaAnalisavel : collectionAnaliseMetal) {
                    if (tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("RESULTADO") == null || tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("LIMITE") == null) {
                        resultado_limitenull = true;
                    }
                    if (tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("RESULTADO").compareTo(BigDecimal.valueOf(0))==0 || tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("LIMITE").compareTo(BigDecimal.valueOf(0))==0) {
                        resultado_limitenull = true;
                    }
                    if(tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("RESULTADO").compareTo(BigDecimal.valueOf(0))==1 && tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("LIMITE").compareTo(BigDecimal.valueOf(0))==1){
                        resultado_limitenull = false;
                    }
                    if(tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("RESULTADO").compareTo(BigDecimal.valueOf(0))==0 && tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("LIMITE").compareTo(BigDecimal.valueOf(0))==1){
                        resultado_limitenull = true;
                    }
                    if(tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("RESULTADO").compareTo(BigDecimal.valueOf(0))==1 && tabelaCaracteristicaAnalisavel.asBigDecimalOrZero("LIMITE").compareTo(BigDecimal.valueOf(0))==0){
                        resultado_limitenull = true;
                    }
                }
                    String parametro = contextoAcao.getParam("OBSERVACAO").toString();
                    JapeWrapper CtrlAmostraDao = JapeFactory.dao("AD_CTLAMOSTRA");
                    DynamicVO ctrlamostravo = CtrlAmostraDao.findOne("SEQAMOSTRA=?", seqAmostra);
                    if (resultado_limitenull == false && ctrlamostravo.asString("OBSERVMET") == null ) {
                        FluidUpdateVO updateFluidVo = CtrlAmostraDao.prepareToUpdateByPK(new Object[]{seqAmostra});
                        updateFluidVo.set("OBSERVMET", parametro);
                        updateFluidVo.update();
                        contextoAcao.setMensagemRetorno("Análise finalizada com sucesso!");
                    } else
                    if (ctrlamostravo.asString("OBSERVMET")!=null && resultado_limitenull == false){
                        contextoAcao.mostraErro("Análise já concluída!");
                    } else
                    if (resultado_limitenull == true){
                        contextoAcao.mostraErro("Favor preencher os campos: Limite e Resultado dos Metais");
                    }
            }
        }
        JapeSession.close(hnd);
    }
}
