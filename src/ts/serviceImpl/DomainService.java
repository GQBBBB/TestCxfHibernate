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
        // ����һ�����������ʱ��,��Ȼ,SQLʱ���JAVAʱ���ʽ��һ��
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date tm = new Date();
        try {
            tm = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return tm;
    }

    // ��ÿ���б�
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

    // gqb��ð����е����п�ݵ�
    @Override
    public List<ExpressSheet> getExpressListInPackage(String packageId) {
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        list = expressSheetDao.getListInPackage(packageId);
        return list;
    }

    // ��ÿ�ݵ�
    @Override
    public Response getExpressSheet(String id) {
        ExpressSheet es = expressSheetDao.get(id);
        return Response.ok(es).header("EntityClass", "ExpressSheet").build();
    }

    // �½���ݵ�
    @Override
    public Response newExpressSheet(String id, int uid) {
        ExpressSheet es = null;
        try {
            es = expressSheetDao.get(id);
        } catch (Exception e1) {
        }

        if (es != null) {
//			if(es.getStatus() != 0)
//				return Response.ok(es).header("EntityClass", "L_ExpressSheet").build(); //�Ѿ�����,�Ҳ��ܸ���
//			else
//            return Response.ok("����˵���Ϣ�Ѿ�����!\n�޷�����!").header("EntityClass", "E_ExpressSheet").build(); // �Ѿ�����
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
            // �ŵ��ռ�������
            MoveExpressIntoPackage(nes.getID(), pkgId);
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // gqb�����ݵ�
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

    // gqb���տ��
    @Override
    public Response receiveExpressSheet(ExpressSheet es, int uid) {
        // System.out.println("sss" + es + "dddd" + uid);
        try {
            if (es.getStatus() != ExpressSheet.STATUS.STATUS_CREATED) {
                return Response.ok("����˵�״̬����!�޷��ռ�!").header("EntityClass", "ER_ExpressSheet").build();
            }
            es.setAccepter(String.valueOf(uid));
            es.setAccepteTime(getCurrentDate());
            // ���Ŀ��״̬Ϊ����״̬
            es.setStatus(ExpressSheet.STATUS.STATUS_COLLECT);// System.out.println(es);
            expressSheetDao.update(es);

            TransPackageContent transPackageContent = new TransPackageContent();
            transPackageContent.setExpress(es);
            transPackageContent.setPkg(transPackageDao.get(userInfoDao.get(uid).getReceivePackageID()));
            // ���İ�������Ϊ�������״̬
            transPackageContent.setStatus(TransPackageContent.STATUS.STATUS_ACTIVE);
            transPackageContentDao.save(transPackageContent);

            return Response.ok(es).header("EntityClass", "R_ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // �ַ����
    @Override
    public Response DispatchExpressSheet(String id, int uid) {
        // TODO Auto-generated method stub
        return null;
    }

    // gqb����������
    public Response MoveExpressIntoPackage(String id, String targetPkgId) {
        TransPackage targetPkg = transPackageDao.get(targetPkgId);
        ExpressSheet expressSheet = expressSheetDao.get(id);
        if (expressSheet.getStatus() == ExpressSheet.STATUS.STATUS_SORTING) {
            // ��������������Ҫ��Ϊת��״̬
            expressSheet.setStatus(ExpressSheet.STATUS.STATUS_TRANSPORT);
            expressSheetDao.update(expressSheet);

            TransPackageContent pkg_add = new TransPackageContent();
            pkg_add.setPkg(targetPkg);
            pkg_add.setExpress(expressSheet);
            pkg_add.setStatus(TransPackageContent.STATUS.STATUS_ACTIVE);
            transPackageContentDao.save(pkg_add);
            return Response.ok("�ÿ���Ѵ��").header("EntityClass", "P_ExpressSheet").build();
        } else {
            return Response.ok("�ÿ���޷����").header("EntityClass", "P_ExpressSheet").build();
        }
    }

    // gqb����Ӱ������Ƴ�
    @Override
    public Response MoveExpressFromPackage(String expressSheetID, String sourcePkgId) {
        ExpressSheet expressSheet = expressSheetDao.get(expressSheetID);
        if (expressSheet == null) {
            return Response.ok("���������").header("EntityClass", "U_ExpressSheet").build();
        }
        int expressSheetStatus = expressSheet.getStatus();

        // ������Ϊ�½�������״̬ʱ
        if (expressSheetStatus == ExpressSheet.STATUS.STATUS_CREATED
                || expressSheetStatus == ExpressSheet.STATUS.STATUS_PAY) {
            return Response.ok("�ÿ���޷��Ӱ������Ƴ�").header("EntityClass", "U_ExpressSheet").build();
        }
        // ������Ϊ�ּ�״̬ʱ
        if (expressSheetStatus == ExpressSheet.STATUS.STATUS_SORTING) {
            return Response.ok("�ÿ���Ѿ��Ӱ������Ƴ�").header("EntityClass", "U_ExpressSheet").build();
        }

        expressSheet.setStatus(ExpressSheet.STATUS.STATUS_SORTING);
        expressSheetDao.update(expressSheet);

        TransPackageContent transPackageContent = transPackageContentDao.get(expressSheetID, sourcePkgId);
        if (transPackageContent == null) {
            return Response.ok("������ڴ˰�����").header("EntityClass", "U_ExpressSheet").build();
        }
        // ���İ�������Ϊ�Ƴ�����״̬
        transPackageContent.setStatus(TransPackageContent.STATUS.STATUS_OUTOF_PACKAGE);
        transPackageContentDao.update(transPackageContent);
        return Response.ok("�ÿ���Ӱ������Ƴ�").header("EntityClass", "U_ExpressSheet").build();
    }

    // ����ڰ������ƶ�
    public boolean MoveExpressBetweenPackage(String id, String sourcePkgId, String targetPkgId) {
        // ��Ҫ�����������
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
                return Response.ok("����˵�״̬����!�޷�����").header("EntityClass", "E_ExpressSheet").build();
            }

            if (transPackageContentDao.getSn(id, pkgId) == 0) {
                // ��ʱ��һ������ʽ,��·�˰����Ĵ��ݹ���,�Լ��Ļ�������һ��
                MoveExpressBetweenPackage(id, userInfoDao.get(uid).getReceivePackageID(), pkgId);
                return Response.ok("����˵�״̬����!\n�����Ϣû�������ɼ�������!").header("EntityClass", "E_ExpressSheet").build();
            }

            nes.setDeliver(String.valueOf(uid));
            nes.setDeliveTime(getCurrentDate());
            nes.setStatus(ExpressSheet.STATUS.STATUS_DELIVERY);
            expressSheetDao.save(nes);
            // ���ɼ�������ɾ��
            MoveExpressFromPackage(nes.getID(), pkgId);
            // ���û����ʷ��¼,���Ѹ����ռ��ͽ����ļ�¼
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // ��ð����б� �д��󣬻�ѭ������
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

    // gqb��ð���
    @Override
    public Response getTransPackage(String id) {
        TransPackage es = transPackageDao.get(id);
        return Response.ok(es).header("EntityClass", "TransPackage").build();
    }

    // �½�����
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

    // gqb����������
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

    // gqb��ð���������Ϣ
    @Override
    public List<PackageRoute> getPackageRouteList(String packageID) {
        return packageRouteDao.getPackageRouteList(packageID);
    }

    // gqb���ð���������Ϣ
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

    // gqb���� �������UID�������������receivePackageID
    @Override
    public Response getReceivePackageID(String UID, int URull) {
        // ����receivePackageIDΪ UID + ʱ�� ��23λ
        String receivePackageID = new StringBuilder(UID).append(System.currentTimeMillis()).toString();
        // ����userinfo���receivePackageID, URull������DptID
        String dptID = userInfoDao.setReceivePackageID(UID, receivePackageID, URull);
        // System.out.println(dptID);
        // ����transpackage����ֵID��TargetNode(=DptID) ������
        TransPackage transPackage = new TransPackage();
        transPackage.setID(receivePackageID);
        if (URull == UserInfo.URull.URull_COLLECT) {
            transPackage.setSourceNode(dptID);
            transPackage.setTargetNode(dptID);
            // ���ð���Ϊ����״̬
            transPackage.setStatus(TransPackage.STATUS.STATUS_COLLECT);
        } else if (URull == UserInfo.URull.URull_PACKING) {
            // ���ð���Ϊ���״̬
            transPackage.setStatus(TransPackage.STATUS.STATUS_CREATED);
        }
        transPackage.setCreateTime(getCurrentDate());
        transPackageDao.save(transPackage);
        // ����userspackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfoDao.get(Integer.parseInt(UID)));
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);
        if (URull == UserInfo.URull.URull_PACKING) {
            // �������
            return Response.ok(transPackage).header("EntityClass", "PackingPackageID").build();
        }
        // Ĭ�����շ���
        return Response.ok(receivePackageID).header("EntityClass", "ReceivePackageID").build();
    }

    // gqb������Ա����PackageID
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
        return Response.ok("ɾ���ɹ�").header("EntityClass", "PackageID").build();

    }

    // gqb���Ա��������б� ����ɡ�������������������
    @Override
    public List<ExpressSheet> getTaskList(String UID) {
        List<ExpressSheet> expressSheetList = null;
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        // ����û���ɫ
        int uRull = userInfo.getURull();

//        // ��ȡ���Ա��������regionCode
//        String regionCode = userInfo.getDptID().substring(0, 6);
//        List<CustomerInfo> customerInfoList = customerInfoDao.findByRegionCode(regionCode);
//        for (CustomerInfo customerInfo : customerInfoList) {
//            int ID = customerInfo.getID();
//            // ��ȡsenderΪid �� statusΪ0 �Ŀ��
//            List<ExpressSheet> list = expressSheetDao.findBySenderAndStatus(ID);
//            expressSheetList.addAll(list);
//        }
        return expressSheetList;

    }

    // gqb���Ա����ӿ�
    @Override
    public Response unpacking(String UID, String PackageID, float x, float y) {
        // ����packageID��ȡ������ԱUID
        int lastUID = usersPackageDao.getUIDByPackageID(PackageID);
        if (lastUID == 0) {
            return Response.ok("����������").header("EntityClass", "UnpackPackageID").build();
        }
        // ���ò����Ա
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        userInfo.setDelivePackageID(PackageID);
        userInfo.setURull(UserInfo.URull.URull_UNPACKING);
        userInfoDao.update(userInfo);
        // ���İ���״̬
        TransPackage transPackage = transPackageDao.get(PackageID);
        // ����Ϊ�ּ�״̬
        transPackage.setStatus(TransPackage.STATUS.STATUS_SORTING);
        transPackageDao.update(transPackage);
        // ��ӵ�usersPackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfo);
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);
        // ��ӵ�transHistory
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

    // gqb���Ա�޸���Ϣ
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

    // gqb��ȡ���Ա��Ϣ�б�
    @Override
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> list = userInfoDao.getAll("UID", true);
        return list;
    }

    // gqb�����ֻ��Ų�ѯ��ʷ�˵�
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

    // gqbת�˸���ID��ȡ�����°���
    public Response getTransportPackage(int UID, String PackageId) {
        TransPackage transPackage = transPackageDao.get(PackageId);
        if (transPackage == null) {
            return Response.ok("����������").header("EntityClass", "TransPackage").build();
        }
        if (transPackage.getStatus() != TransPackage.STATUS.STATUS_PACK) {
            return Response.ok("����״̬����").header("EntityClass", "TransPackage").build();
        }
        TransHistory transHistory = new TransHistory();
        transPackage.setStatus(TransPackage.STATUS.STATUS_TRANSPORT);
        transPackageDao.update(transPackage);

        transHistory.setActTime(getCurrentDate());
        transHistory.setPkg(transPackage);
        transHistory.setUIDFrom(usersPackageDao.getUIDByPackageID(PackageId));
        transHistory.setUIDTo(UID);
        // ���ݰ�����sourceNode��ýڵ�x��y
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

    // gqb ���ת�˰���
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

    // gqb ������Ͱ���
    @Override
    public Response getDeliverPackageID(int UID, String PackageId) {
        TransPackage transPackage = transPackageDao.get(PackageId);
        if (transPackage == null) {
            return Response.ok("����������").header("EntityClass", "DeliverPackage").build();
        }
        if (transPackage.getStatus() != TransPackage.STATUS.STATUS_PACK) {
            return Response.ok("����״̬����").header("EntityClass", "DeliverPackage").build();
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
        // ���ݰ�����sourceNode��ýڵ�x��y
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
