package br.com.unifertil.glaycon.utils;

import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;

import java.math.BigDecimal;

public class ReturnEntity  {

    public DynamicVO getTabelaByPkString (String entidade, String condicao,String parametro) throws Exception{
        JapeWrapper entityDao = JapeFactory.dao(entidade);
        DynamicVO EntityDynamicVO = entityDao.findByPK(condicao,new Object[]{parametro});
        return EntityDynamicVO;
    }

    public DynamicVO getTabelaByPkBigDecimal (String entidade, String condicao,BigDecimal parametro) throws Exception{
        JapeWrapper entityDao = JapeFactory.dao(entidade);
        DynamicVO EntityDynamicVO = entityDao.findByPK(condicao,new Object[]{parametro});
        return EntityDynamicVO;
    }
    public DynamicVO getTabelaByFindOneBigDecimal (String entidade, String condicao,BigDecimal parametro) throws Exception{
        JapeWrapper entityDao = JapeFactory.dao(entidade);
        DynamicVO EntityDynamicVO = entityDao.findOne(condicao,new Object[]{parametro});
        return EntityDynamicVO;
    }
    public DynamicVO getTabelaByFindOneBigDecimalTwoParameters (String entidade, String condicao,BigDecimal parametro,BigDecimal parametro2) throws Exception{
        JapeWrapper entityDao = JapeFactory.dao(entidade);
        DynamicVO EntityDynamicVO = entityDao.findOne(condicao,new Object[]{parametro,parametro2});
        return EntityDynamicVO;
    }

    public DynamicVO getTabelaByFindOneBigDecimalString (String entidade, String condicao,BigDecimal parametro,String parametro2) throws Exception{
        JapeWrapper entityDao = JapeFactory.dao(entidade);
        DynamicVO EntityDynamicVO = entityDao.findOne(condicao,new Object[]{parametro,parametro2});
        return EntityDynamicVO;
    }
}
