package ts.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.HibernateCallback;

import ts.daoBase.BaseDao;
import ts.model.CustomerInfo;
import ts.model.UserInfo;

public class UserInfoDao extends BaseDao<UserInfo, Integer> {
    public UserInfoDao() {
        super(UserInfo.class);
    }

    public UserInfo get(int id) {
        UserInfo userInfo;
        try {
            userInfo = super.get(id);
        } catch (Exception e) {
            return null;
        }
        return userInfo;
    }

    public String setReceivePackageID(String UID, String receivePackageID) {
        UserInfo userInfo = get(Integer.parseInt(UID));
        userInfo.setReceivePackageID(receivePackageID);
        userInfo.setURull(1);
        super.update(userInfo);
        return userInfo.getDptID();
    }

    public UserInfo findByLimit(UserInfo userInfo) {
        String sql = "PWD = " + userInfo.getPWD() + "+ Name =" + userInfo.getName() + "+ TelCode =" + userInfo.getTelCode() + "+ DptID =" + userInfo.getDptID();
        List<UserInfo> list = new ArrayList<UserInfo>();
        list = findBy("UID", true, Restrictions.sqlRestriction(sql));
        return list.get(0);
    }
}
