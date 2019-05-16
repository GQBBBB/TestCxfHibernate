package ts.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import ts.daoBase.BaseDao;
import ts.model.CustomerInfo;
import ts.model.ExpressSheet;

public class ExpressSheetDao extends BaseDao<ExpressSheet,String> {
	private RegionDao regionDao;
	public RegionDao getRegionDao() {
		return regionDao;
	}
	public void setRegionDao(RegionDao dao) {
		this.regionDao = dao;
	}

	public ExpressSheetDao(){
		super(ExpressSheet.class);
	}


	//获得指定包裹ID的所有快件列表
	public List<ExpressSheet> getListInPackage(String pkg_id) {
		String sql = "{alias}.ID in (select ExpressID from TransPackageContent where PackageID = '"+pkg_id+"')";
		List<ExpressSheet> list = new ArrayList<ExpressSheet>();
		list = findBy("ID", true, Restrictions.sqlRestriction(sql));		
		return list;
	}
	
	public List<ExpressSheet> findBySenderAndStatus(int id) {
        String sql = "Status in (0,1) and Sender = " + id;
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        list = findBy("ID", true, Restrictions.sqlRestriction(sql));
        return list;
    }
	
	public List<ExpressSheet> findBySender(int id) {
        String sql = "Sender = " + id;
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        list = findBy("ID", true, Restrictions.sqlRestriction(sql));
        return list;
    }
}
