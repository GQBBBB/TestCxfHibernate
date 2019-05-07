package ts.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import ts.daoImpl.CustomerInfoDao;
import ts.daoImpl.UserInfoDao;
import ts.daoImpl.RegionDao;
import ts.daoImpl.TransNodeDao;
import ts.model.CodeNamePair;
import ts.model.CustomerInfo;
import ts.model.Region;
import ts.model.TransNode;
import ts.model.UserInfo;
import ts.serviceInterface.IMiscService;

public class MiscService implements IMiscService {
    // TransNodeCatalog nodes; //自己做的缓存和重定向先不要了,用Hibernate缓存对付一下，以后加上去
    // RegionCatalog regions;
    private TransNodeDao transNodeDao;
    private RegionDao regionDao;
    private CustomerInfoDao customerInfoDao;
    private UserInfoDao userInfoDao;

    public TransNodeDao getTransNodeDao() {
        return transNodeDao;
    }

    public void setTransNodeDao(TransNodeDao dao) {
        this.transNodeDao = dao;
    }

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public void setRegionDao(RegionDao dao) {
        this.regionDao = dao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public void setUserInfoDao(UserInfoDao dao) {
        this.userInfoDao = dao;
    }

    public CustomerInfoDao getCustomerInfoDao() {
        return customerInfoDao;
    }

    public void setCustomerInfoDao(CustomerInfoDao dao) {
        this.customerInfoDao = dao;
    }

    public MiscService() {
//		nodes = new TransNodeCatalog();
//		nodes.Load();
//		regions = new RegionCatalog();
//		regions.Load();
    }

    @Override
    public TransNode getNode(String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TransNode> getNodesList(String regionCode, int type) {
        // TODO Auto-generated method stub
        return null;
    }

    // 根据名称获取用户信息
    @Override
    public List<CustomerInfo> getCustomerListByName(String name) {
//		List<CustomerInfo> listci = customerInfoDao.findByName(name);
//		List<CodeNamePair> listCN = new ArrayList<CodeNamePair>();
//		for(CustomerInfo ci : listci){
//			CodeNamePair cn = new CodeNamePair(String.valueOf(ci.getID()),ci.getName());
//			listCN.add(cn);
//		}
//		return listCN;
        return customerInfoDao.findByName(name);
    }

    // 根据tele获取用户信息
    @Override
    public List<CustomerInfo> getCustomerListByTelCode(String TelCode) {
//		List<CustomerInfo> listci = customerInfoDao.findByTelCode(TelCode);
//		List<CodeNamePair> listCN = new ArrayList<CodeNamePair>();
//		for(CustomerInfo ci : listci){
//			CodeNamePair cn = new CodeNamePair(String.valueOf(ci.getID()),ci.getName());
//			listCN.add(cn);
//		}
//		return listCN;
        return customerInfoDao.findByTelCode(TelCode);
    }

    // 根据id获取用户信息
    @Override
    public Response getCustomerInfo(String id) {
        CustomerInfo cstm = customerInfoDao.get(Integer.parseInt(id));
//		try{
//			cstm.setRegionString(regionDao.getRegionNameByID(cstm.getRegionCode()));	//这部分功能放到DAO里去了
//		}catch(Exception e){}
        return Response.ok(cstm).header("EntityClass", "CustomerInfo").build();
    }

    // 根据id删除用户
    @Override
    public Response deleteCustomerInfo(int id) {
        customerInfoDao.removeById(id);
        return Response.ok("Deleted").header("EntityClass", "D_CustomerInfo").build();
    }

    // 根据添加用户
    @Override
    public Response saveCustomerInfo(CustomerInfo obj) {
        try {
            customerInfoDao.save(obj);
            return Response.ok(obj).header("EntityClass", "R_CustomerInfo").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // 获取省列表
    @Override
    public List<CodeNamePair> getProvinceList() {
        List<Region> listrg = regionDao.getProvinceList();
        List<CodeNamePair> listCN = new ArrayList<CodeNamePair>();
        for (Region rg : listrg) {
            CodeNamePair cn = new CodeNamePair(rg.getORMID(), rg.getPrv());
            listCN.add(cn);
        }
        return listCN;
    }

    // 获取市列表
    @Override
    public List<CodeNamePair> getCityList(String prv) {
        List<Region> listrg = regionDao.getCityList(prv);
        List<CodeNamePair> listCN = new ArrayList<CodeNamePair>();
        for (Region rg : listrg) {
            CodeNamePair cn = new CodeNamePair(rg.getORMID(), rg.getCty());
            listCN.add(cn);
        }
        return listCN;
    }

    // 获取县列表
    @Override
    public List<CodeNamePair> getTownList(String city) {
        List<Region> listrg = regionDao.getTownList(city);
        List<CodeNamePair> listCN = new ArrayList<CodeNamePair>();
        for (Region rg : listrg) {
            CodeNamePair cn = new CodeNamePair(rg.getORMID(), rg.getTwn());
            listCN.add(cn);
        }
        return listCN;
    }

    // 以字符串形式获取region的全限定名信息 乱码
    @Override
    public String getRegionString(String code) {
        return regionDao.getRegionNameByID(code);
    }

    // 获取Region，包含region的全限定名信息
    @Override
    public Region getRegion(String code) {
        return regionDao.getFullNameRegionByID(code);
    }

    @Override
    public void CreateWorkSession(int uid) {
        // TODO Auto-generated method stub

    }

    @Override
    public Response doLogin(int uid, String pwd) {
        UserInfo userInfo = userInfoDao.get(uid);
        if (userInfo != null) {
            if (userInfo.getPWD().equals(pwd)) {
                return Response.ok(userInfo).header("EntityClass", "login").build();
            }
        }
        return Response.ok("false").header("EntityClass", "login").build();
    }

    @Override
    public void doLogOut(int uid) {
        // TODO Auto-generated method stub

    }

    @Override
    public void RefreshSessionList() {
        // TODO Auto-generated method stub

    }
}
