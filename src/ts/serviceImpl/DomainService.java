package ts.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.scene.control.Tab;
import ts.daoImpl.CustomerInfoDao;
import ts.daoImpl.ExpressSheetDao;
import ts.daoImpl.PackageRouteDao;
import ts.daoImpl.TransHistoryDao;
import ts.daoImpl.TransNodeDao;
import ts.daoImpl.TransPackageContentDao;
import ts.daoImpl.TransPackageDao;
import ts.daoImpl.UserInfoDao;
import ts.daoImpl.UsersPackageDao;
import ts.model.CustomerInfo;
import ts.model.ExpressSheet;
import ts.model.PackageRoute;
import ts.model.TransHistory;
import ts.model.TransNode;
import ts.model.TransPackage;
import ts.model.TransPackageContent;
import ts.model.UserInfo;
import ts.model.UsersPackage;
import ts.serviceInterface.IDomainService;

public class DomainService implements IDomainService {

    private ExpressSheetDao expressSheetDao;
    private TransPackageDao transPackageDao;
    private TransHistoryDao transHistoryDao;
    private TransPackageContentDao transPackageContentDao;
    private PackageRouteDao packageRouteDao;
    private UserInfoDao userInfoDao;
    private UsersPackageDao usersPackageDao;
    private CustomerInfoDao customerInfoDao;
    private TransNodeDao transNodeDao;

    public TransNodeDao getTransNodeDao() {
        return transNodeDao;
    }

    public void setTransNodeDao(TransNodeDao dao) {
        this.transNodeDao = dao;
    }

    public CustomerInfoDao getCustomerInfoDao() {
        return customerInfoDao;
    }

    public void setCustomerInfoDao(CustomerInfoDao dao) {
        this.customerInfoDao = dao;
    }

    public UsersPackageDao getUsersPackageDao() {
        return usersPackageDao;
    }

    public void setUsersPackageDao(UsersPackageDao dao) {
        this.usersPackageDao = dao;
    }

    public PackageRouteDao getPackageRouteDao() {
        return packageRouteDao;
    }

    public void setPackageRouteDao(PackageRouteDao dao) {
        this.packageRouteDao = dao;
    }

    public ExpressSheetDao getExpressSheetDao() {
        return expressSheetDao;
    }

    public void setExpressSheetDao(ExpressSheetDao dao) {
        this.expressSheetDao = dao;
    }

    public TransPackageDao getTransPackageDao() {
        return transPackageDao;
    }

    public void setTransPackageDao(TransPackageDao dao) {
        this.transPackageDao = dao;
    }

    public TransHistoryDao getTransHistoryDao() {
        return transHistoryDao;
    }

    public void setTransHistoryDao(TransHistoryDao dao) {
        this.transHistoryDao = dao;
    }

    public TransPackageContentDao getTransPackageContentDao() {
        return transPackageContentDao;
    }

    public void setTransPackageContentDao(TransPackageContentDao dao) {
        this.transPackageContentDao = dao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public void setUserInfoDao(UserInfoDao dao) {
        this.userInfoDao = dao;
    }

    public Date getCurrentDate() {
        // 产生一个不带毫秒的时间,不然,SQL时间和JAVA时间格式不一致
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date tm = new Date();
        try {
            tm = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return tm;
    }

    // 获得快件列表
    @Override
    public List<ExpressSheet> getExpressList(String property, String restrictions, String value) {
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        switch (restrictions.toLowerCase()) {
        case "eq":
            list = expressSheetDao.findBy(property, value, "ID", true);
            break;
        case "like":
            list = expressSheetDao.findLike(property, value + "%", "ID", true);
            break;
        }
        return list;
    }
//	@Override
//	public List<ExpressSheet> getExpressList(String property,
//			String restrictions, String value) {
//		Criterion cr1;
//		Criterion cr2 = Restrictions.eq("Status", 0);
//
//		List<ExpressSheet> list = new ArrayList<ExpressSheet>();
//		switch(restrictions.toLowerCase()){
//		case "eq":
//			cr1 = Restrictions.eq(property, value);
//			break;
//		case "like":
//			cr1 = Restrictions.like(property, value);
//			break;
//		default:
//			cr1 = Restrictions.like(property, value);
//			break;
//		}
//		list = expressSheetDao.findBy("ID", true,cr1,cr2);		
//		return list;
//	}

    // gqb获得包裹中的所有快递单
    @Override
    public List<ExpressSheet> getExpressListInPackage(String packageId) {
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        list = expressSheetDao.getListInPackage(packageId);
        return list;
    }

    // 获得快递单
    @Override
    public Response getExpressSheet(String id) {
        ExpressSheet es = expressSheetDao.get(id);
        return Response.ok(es).header("EntityClass", "ExpressSheet").build();
    }

    // 新建快递单
    @Override
    public Response newExpressSheet(String id, int uid) {
        ExpressSheet es = null;
        try {
            es = expressSheetDao.get(id);
        } catch (Exception e1) {
        }

        if (es != null) {
//			if(es.getStatus() != 0)
//				return Response.ok(es).header("EntityClass", "L_ExpressSheet").build(); //已经存在,且不能更改
//			else
//            return Response.ok("快件运单信息已经存在!\n无法创建!").header("EntityClass", "E_ExpressSheet").build(); // 已经存在
        }
        try {
            String pkgId = userInfoDao.get(uid).getReceivePackageID();
            ExpressSheet nes = new ExpressSheet();
            nes.setID(id);
            nes.setType(0);
            nes.setAccepter(String.valueOf(uid));
            nes.setAccepteTime(getCurrentDate());
            nes.setStatus(ExpressSheet.STATUS.STATUS_CREATED);
//			TransPackageContent pkg_add = new TransPackageContent();
//			pkg_add.setPkg(transPackageDao.get(pkgId));
//			pkg_add.setExpress(nes);
//			nes.getTransPackageContent().add(pkg_add);
            expressSheetDao.save(nes);
            // 放到收件包裹中
            MoveExpressIntoPackage(nes.getID(), pkgId);
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // gqb保存快递单
    @Override
    public Response saveExpressSheet(ExpressSheet obj) {
        // System.out.println("ExpressSheet---" +obj);
        try {
            String id = obj.getID();
            // System.out.println("---------------");
            while (true) {
                id = String.valueOf(System.currentTimeMillis());
                // if (expressSheetDao.get(id) == null) {
                obj.setID(id);
                break;
                // }
            }

            // System.out.println("ExpressSheet---" + obj);
            if (obj.getStatus() != ExpressSheet.STATUS.STATUS_CREATED) {
                return Response.ok("ok!").header("EntityClass", "E_ExpressSheet").build();
            }
            expressSheetDao.save(obj);
            return Response.ok(obj).header("EntityClass", "R_ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // gqb揽收快件
    @Override
    public Response receiveExpressSheet(ExpressSheet es, int uid) {
        // System.out.println("sss" + es + "dddd" + uid);
        try {
            if (es.getStatus() != ExpressSheet.STATUS.STATUS_CREATED) {
                return Response.ok("快件运单状态错误!无法收件!").header("EntityClass", "ER_ExpressSheet").build();
            }
            es.setAccepter(String.valueOf(uid));
            es.setAccepteTime(getCurrentDate());
            // 更改快件状态为揽收状态
            es.setStatus(ExpressSheet.STATUS.STATUS_COLLECT);// System.out.println(es);
            expressSheetDao.update(es);

            TransPackageContent transPackageContent = new TransPackageContent();
            transPackageContent.setExpress(es);
            transPackageContent.setPkg(transPackageDao.get(userInfoDao.get(uid).getReceivePackageID()));
            // 更改包裹内容为移入包裹状态
            transPackageContent.setStatus(TransPackageContent.STATUS.STATUS_ACTIVE);
            transPackageContentDao.save(transPackageContent);

            return Response.ok(es).header("EntityClass", "R_ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // 分发快件
    @Override
    public Response DispatchExpressSheet(String id, int uid) {
        // TODO Auto-generated method stub
        return null;
    }

    // gqb快件移入包裹
    public Response MoveExpressIntoPackage(String id, String targetPkgId) {
        TransPackage targetPkg = transPackageDao.get(targetPkgId);
        ExpressSheet expressSheet = expressSheetDao.get(id);
        if (expressSheet.getStatus() == ExpressSheet.STATUS.STATUS_SORTING) {
            // 快件打进包裹后需要设为转运状态
            expressSheet.setStatus(ExpressSheet.STATUS.STATUS_TRANSPORT);
            expressSheetDao.update(expressSheet);

            TransPackageContent pkg_add = new TransPackageContent();
            pkg_add.setPkg(targetPkg);
            pkg_add.setExpress(expressSheet);
            pkg_add.setStatus(TransPackageContent.STATUS.STATUS_ACTIVE);
            transPackageContentDao.save(pkg_add);
            return Response.ok("该快件已打包").header("EntityClass", "P_ExpressSheet").build();
        } else {
            return Response.ok("该快件无法打包").header("EntityClass", "P_ExpressSheet").build();
        }
    }

    // gqb快件从包裹中移除
    @Override
    public Response MoveExpressFromPackage(String expressSheetID, String sourcePkgId) {
        ExpressSheet expressSheet = expressSheetDao.get(expressSheetID);
        if (expressSheet == null) {
            return Response.ok("快件不存在").header("EntityClass", "U_ExpressSheet").build();
        }
        int expressSheetStatus = expressSheet.getStatus();

        // 当包裹为新建，交付状态时
        if (expressSheetStatus == ExpressSheet.STATUS.STATUS_CREATED
                || expressSheetStatus == ExpressSheet.STATUS.STATUS_PAY) {
            return Response.ok("该快件无法从包裹中移出").header("EntityClass", "U_ExpressSheet").build();
        }
        // 当包裹为分拣状态时
        if (expressSheetStatus == ExpressSheet.STATUS.STATUS_SORTING) {
            return Response.ok("该快件已经从包裹中移出").header("EntityClass", "U_ExpressSheet").build();
        }

        expressSheet.setStatus(ExpressSheet.STATUS.STATUS_SORTING);
        expressSheetDao.update(expressSheet);

        TransPackageContent transPackageContent = transPackageContentDao.get(expressSheetID, sourcePkgId);
        if (transPackageContent == null) {
            return Response.ok("快件不在此包裹中").header("EntityClass", "U_ExpressSheet").build();
        }
        // 更改包裹内容为移出包裹状态
        transPackageContent.setStatus(TransPackageContent.STATUS.STATUS_OUTOF_PACKAGE);
        transPackageContentDao.update(transPackageContent);
        return Response.ok("该快件从包裹中移出").header("EntityClass", "U_ExpressSheet").build();
    }

    // 快件在包裹间移动
    public boolean MoveExpressBetweenPackage(String id, String sourcePkgId, String targetPkgId) {
        // 需要加入事务机制
        MoveExpressFromPackage(id, sourcePkgId);
        MoveExpressIntoPackage(id, targetPkgId);
        return true;
    }

    //
    @Override
    public Response DeliveryExpressSheetId(String id, int uid) {
        try {
            String pkgId = userInfoDao.get(uid).getDelivePackageID();
            ExpressSheet nes = expressSheetDao.get(id);
            if (nes.getStatus() != ExpressSheet.STATUS.STATUS_TRANSPORT) {
                return Response.ok("快件运单状态错误!无法交付").header("EntityClass", "E_ExpressSheet").build();
            }

            if (transPackageContentDao.getSn(id, pkgId) == 0) {
                // 临时的一个处理方式,断路了包裹的传递过程,自己的货篮倒腾一下
                MoveExpressBetweenPackage(id, userInfoDao.get(uid).getReceivePackageID(), pkgId);
                return Response.ok("快件运单状态错误!\n快件信息没在您的派件包裹中!").header("EntityClass", "E_ExpressSheet").build();
            }

            nes.setDeliver(String.valueOf(uid));
            nes.setDeliveTime(getCurrentDate());
            nes.setStatus(ExpressSheet.STATUS.STATUS_DELIVERY);
            expressSheetDao.save(nes);
            // 从派件包裹中删除
            MoveExpressFromPackage(nes.getID(), pkgId);
            // 快件没有历史记录,很难给出收件和交付的记录
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // 获得包裹列表 有错误，会循环引用
    @Override
    public List<TransPackage> getTransPackageList(String property, String restrictions, String value) {
        List<TransPackage> list = new ArrayList<TransPackage>();
        switch (restrictions.toLowerCase()) {
        case "eq":
            list = transPackageDao.findBy(property, value, "ID", true);
            break;
        case "like":
            list = transPackageDao.findLike(property, value + "%", "ID", true);
            break;
        }
        return list;
    }

    // gqb获得包裹
    @Override
    public Response getTransPackage(String id) {
        TransPackage es = transPackageDao.get(id);
        return Response.ok(es).header("EntityClass", "TransPackage").build();
    }

    // 新建包裹
    @Override
    public Response newTransPackage(String id, int uid) {
        try {
            TransPackage npk = new TransPackage();
            npk.setID(id);
            // npk.setStatus(value);
            npk.setCreateTime(new Date());
            transPackageDao.save(npk);
            return Response.ok(npk).header("EntityClass", "TransPackage").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // gqb打包保存包裹
    @Override
    public Response saveTransPackage(TransPackage obj) {
        try {
            obj.setStatus(TransPackage.STATUS.STATUS_PACK);
            transPackageDao.update(obj);
            return Response.ok(obj).header("EntityClass", "R_TransPackage").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // gqb获得包裹坐标信息
    @Override
    public List<PackageRoute> getPackageRouteList(String packageID) {
        return packageRouteDao.getPackageRouteList(packageID);
    }

    // gqb设置包裹坐标信息
    @Override
    public Response setPackageRoute(String packageID, float x, float y) {
        PackageRoute packageRoute = new PackageRoute();
        packageRoute.setPkg(transPackageDao.get(packageID));
        packageRoute.setX(x);
        packageRoute.setY(y);
        packageRoute.setTm(getCurrentDate());
        packageRouteDao.save(packageRoute);
        return Response.ok("ok").header("EntityClass", "PackageRoute").build();
    }

    // gqb揽收 打包根据UID建立包裹，获得receivePackageID
    @Override
    public Response getReceivePackageID(String UID, int URull) {
        // 设置receivePackageID为 UID + 时间 共23位
        String receivePackageID = new StringBuilder(UID).append(System.currentTimeMillis()).toString();
        // 设置userinfo表的receivePackageID, URull并返回DptID
        String dptID = userInfoDao.setReceivePackageID(UID, receivePackageID, URull);
        // System.out.println(dptID);
        // 设置transpackage并赋值ID和TargetNode(=DptID) 并保存
        TransPackage transPackage = new TransPackage();
        transPackage.setID(receivePackageID);
        if (URull == UserInfo.URull.URull_COLLECT) {
            transPackage.setSourceNode(dptID);
            transPackage.setTargetNode(dptID);
            // 设置包裹为揽收状态
            transPackage.setStatus(TransPackage.STATUS.STATUS_COLLECT);
        } else if (URull == UserInfo.URull.URull_PACKING) {
            // 设置包裹为打包状态
            transPackage.setStatus(TransPackage.STATUS.STATUS_CREATED);
        }
        transPackage.setCreateTime(getCurrentDate());
        transPackageDao.save(transPackage);
        // 设置userspackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfoDao.get(Integer.parseInt(UID)));
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);
        if (URull == UserInfo.URull.URull_PACKING) {
            // 打包返回
            return Response.ok(transPackage).header("EntityClass", "PackingPackageID").build();
        }
        // 默认揽收返回
        return Response.ok(receivePackageID).header("EntityClass", "ReceivePackageID").build();
    }

    // gqb清理快递员三个PackageID
    @Override
    public Response cleanPackageID(String UID, String flag) {
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        userInfo.setURull(0);
        if (String.valueOf(flag.charAt(0)).equals("1")) {
            userInfo.setReceivePackageID(null);
        }
        if (String.valueOf(flag.charAt(1)).equals("1")) {
            userInfo.setDelivePackageID(null);
        }
        if (String.valueOf(flag.charAt(2)).equals("1")) {
            userInfo.setTransPackageID(null);
        }
        userInfoDao.update(userInfo);
        return Response.ok("删除成功").header("EntityClass", "PackageID").build();

    }

    // gqb快递员获得任务列表 待完成。。。。。。。。。。
    @Override
    public List<ExpressSheet> getTaskList(String UID) {
        List<ExpressSheet> expressSheetList = null;
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        // 获得用户角色
        int uRull = userInfo.getURull();

//        // 获取快递员所在区域regionCode
//        String regionCode = userInfo.getDptID().substring(0, 6);
//        List<CustomerInfo> customerInfoList = customerInfoDao.findByRegionCode(regionCode);
//        for (CustomerInfo customerInfo : customerInfoList) {
//            int ID = customerInfo.getID();
//            // 获取sender为id 和 status为0 的快件
//            List<ExpressSheet> list = expressSheetDao.findBySenderAndStatus(ID);
//            expressSheetList.addAll(list);
//        }
        return expressSheetList;

    }

    // gqb快递员拆包接口
    @Override
    public Response unpacking(String UID, String PackageID, float x, float y) {
        // 根据packageID提取派送人员UID
        int lastUID = usersPackageDao.getUIDByPackageID(PackageID);
        if (lastUID == 0) {
            return Response.ok("包裹不存在").header("EntityClass", "UnpackPackageID").build();
        }
        // 设置拆包人员
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        userInfo.setDelivePackageID(PackageID);
        userInfo.setURull(UserInfo.URull.URull_UNPACKING);
        userInfoDao.update(userInfo);
        // 更改包裹状态
        TransPackage transPackage = transPackageDao.get(PackageID);
        // 包裹为分拣状态
        transPackage.setStatus(TransPackage.STATUS.STATUS_SORTING);
        transPackageDao.update(transPackage);
        // 添加到usersPackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfo);
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);
        // 添加到transHistory
        TransHistory transHistory = new TransHistory();
        transHistory.setPkg(transPackage);
        transHistory.setActTime(getCurrentDate());
        transHistory.setUIDFrom(lastUID);
        transHistory.setUIDTo(Integer.parseInt(UID));
        transHistory.setX(x);
        transHistory.setY(y);
        transHistoryDao.save(transHistory);

        return Response.ok(PackageID).header("EntityClass", "UnpackPackageID").build();
    }

    // gqb快递员修改信息
    @Override
    public Response changeUserInfo(UserInfo userInfo) {
        UserInfo oldUserInfo = userInfoDao.get(userInfo.getUID());
        oldUserInfo.setName(userInfo.getName());
        oldUserInfo.setPWD(userInfo.getPWD());
        oldUserInfo.setTelCode(userInfo.getTelCode());
        oldUserInfo.setDptID(userInfo.getDptID());
        userInfoDao.update(oldUserInfo);
        return Response.ok(oldUserInfo).header("EntityClass", "UserInfo").build();
    }

    // gqb获取快递员信息列表
    @Override
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> list = userInfoDao.getAll("UID", true);
        return list;
    }

    // gqb根据手机号查询历史运单
    public HashSet<ExpressSheet> getExpressSheetByTelCode(String telCode) {
        HashSet<ExpressSheet> list = new HashSet<ExpressSheet>();
        List<CustomerInfo> customerInfos = customerInfoDao.findByTelCode(telCode);
        int sender = 0;
        for (CustomerInfo customerInfo : customerInfos) {
            sender = customerInfo.getID();
            List<ExpressSheet> expressSheets = expressSheetDao.findBySender(sender);
            list.addAll(expressSheets);
        }
        return list;
    }

    // gqb转运根据ID获取并更新包裹
    public Response getTransportPackage(int UID, String PackageId) {
        TransPackage transPackage = transPackageDao.get(PackageId);
        if (transPackage == null) {
            return Response.ok("包裹不存在").header("EntityClass", "TransPackage").build();
        }
        if (transPackage.getStatus() != TransPackage.STATUS.STATUS_PACK) {
            return Response.ok("包裹状态错误").header("EntityClass", "TransPackage").build();
        }
        TransHistory transHistory = new TransHistory();
        transPackage.setStatus(TransPackage.STATUS.STATUS_TRANSPORT);
        transPackageDao.update(transPackage);

        transHistory.setActTime(getCurrentDate());
        transHistory.setPkg(transPackage);
        transHistory.setUIDFrom(usersPackageDao.getUIDByPackageID(PackageId));
        transHistory.setUIDTo(UID);
        // 根据包裹中sourceNode获得节点x，y
        TransNode transNode = transNodeDao.get(transPackage.getSourceNode());
        transHistory.setX(transNode.getX());
        transHistory.setY(transNode.getY());
        transHistoryDao.save(transHistory);

        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setPkg(transPackage);
        usersPackage.setUserU(userInfoDao.get(UID));
        usersPackageDao.save(usersPackage);

        PackageRoute packageRoute = new PackageRoute();
        packageRoute.setPkg(transPackage);
        packageRoute.setTm(getCurrentDate());
        packageRoute.setX(transNode.getX());
        packageRoute.setY(transNode.getY());
        packageRouteDao.save(packageRoute);

//        List<TransPackageContent> transPackageContents = transPackageContentDao.getExpressSheetList(PackageId);
//        ExpressSheet expressSheet = new ExpressSheet();
//        for (TransPackageContent transPackageContent : transPackageContents) {
//            expressSheet = transPackageContent.getExpress();
//            expressSheet.setStatus(ExpressSheet.STATUS.STATUS_TRANSPORT);
//            expressSheetDao.update(expressSheet);
//        }

        return Response.ok(transPackage).header("EntityClass", "TransPackage").build();
    }

    // gqb 获得转运包裹
    @Override
    public HashSet<TransPackage> getTransPackageList(int UID) {
        HashSet<TransPackage> set = new HashSet<TransPackage>();
        List<UsersPackage> usersPackages = usersPackageDao.getPackageByUID(UID);
        for (UsersPackage usersPackage : usersPackages) {
            if (usersPackage.getPkg().getStatus() == TransPackage.STATUS.STATUS_TRANSPORT) {
                set.add(usersPackage.getPkg());
            }
        }
        return set;
    }

    // gqb 获得派送包裹
    @Override
    public Response getDeliverPackageID(int UID, String PackageId) {
        TransPackage transPackage = transPackageDao.get(PackageId);
        if (transPackage == null) {
            return Response.ok("包裹不存在").header("EntityClass", "DeliverPackage").build();
        }
        if (transPackage.getStatus() != TransPackage.STATUS.STATUS_PACK) {
            return Response.ok("包裹状态错误").header("EntityClass", "DeliverPackage").build();
        }
        transPackage.setStatus(TransPackage.STATUS.STATUS_DELIVERY);
        transPackageDao.update(transPackage);
        
        UserInfo userInfo = userInfoDao.get(UID);
        userInfo.setDelivePackageID(PackageId);
        userInfo.setURull(UserInfo.URull.URull_DELIVERY);
        userInfoDao.update(userInfo);
        
        TransHistory transHistory = new TransHistory();
        transHistory.setActTime(getCurrentDate());
        transHistory.setPkg(transPackage);
        transHistory.setUIDFrom(usersPackageDao.getUIDByPackageID(PackageId));
        transHistory.setUIDTo(UID);
        // 根据包裹中sourceNode获得节点x，y
        TransNode transNode = transNodeDao.get(transPackage.getSourceNode());
        transHistory.setX(transNode.getX());
        transHistory.setY(transNode.getY());
        transHistoryDao.save(transHistory);

        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setPkg(transPackage);
        usersPackage.setUserU(userInfoDao.get(UID));
        usersPackageDao.save(usersPackage);

        PackageRoute packageRoute = new PackageRoute();
        packageRoute.setPkg(transPackage);
        packageRoute.setTm(getCurrentDate());
        packageRoute.setX(transNode.getX());
        packageRoute.setY(transNode.getY());
        packageRouteDao.save(packageRoute);

        List<TransPackageContent> transPackageContents = transPackageContentDao.getExpressSheetList(PackageId);
        ExpressSheet expressSheet = new ExpressSheet();
        for (TransPackageContent transPackageContent : transPackageContents) {
            expressSheet = transPackageContent.getExpress();
            System.out.println(expressSheet);
            expressSheet.setStatus(ExpressSheet.STATUS.STATUS_DELIVERY);
            expressSheetDao.update(expressSheet);
        }
        return Response.ok(transPackage).header("EntityClass", "DeliverPackage").build();
    }
}
