package br.com.unifertil.glaycon.utils;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.util.FinderWrapper;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

import java.math.BigDecimal;
import java.util.Collection;

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

    public DynamicVO getTabelaByFindOneString (String entidade, String condicao,String parametro) throws Exception{
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


    public Collection<DynamicVO> collectionVOBigDecimal (String entidade, String condicao, BigDecimal parametro) throws Exception {
        EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
        FinderWrapper finder = new FinderWrapper(entidade, condicao, new Object[]{parametro});
        Collection<DynamicVO> collection = dwfFacade.findByDynamicFinderAsVO(finder);
        return collection;
    }

    public Collection<DynamicVO> collectionVOBigDecimalString (String entidade, String condicao, BigDecimal parametro, String parametro2) throws Exception {
        EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
        FinderWrapper finder = new FinderWrapper(entidade, condicao, new Object[] { parametro,parametro2 });
        Collection<DynamicVO> collection = dwfFacade.findByDynamicFinderAsVO(finder);
        return  collection;
    }

    public Collection<DynamicVO> collectionVOString (String entidade, String condicao, String parametro) throws Exception {
        EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();
        FinderWrapper finder = new FinderWrapper(entidade, condicao, new Object[] { parametro });
        Collection<DynamicVO> collection = dwfFacade.findByDynamicFinderAsVO(finder);
        return  collection;
    }
}
