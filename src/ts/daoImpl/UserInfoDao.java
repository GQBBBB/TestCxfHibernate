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

    public String setReceivePackageID(String UID, String receivePackageID, int URull) {
        UserInfo userInfo = get(Integer.parseInt(UID));
        userInfo.setReceivePackageID(receivePackageID);
        userInfo.setURull(URull);
        super.update(userInfo);
        return userInfo.getDptID();
    }

    public UserInfo findByLimit(UserInfo userInfo) {
        String sql = "PWD = '" + userInfo.getPWD() + "' and Name = '" + userInfo.getName() + "' and TelCode = '" + userInfo.getTelCode() + "'";
        List<UserInfo> list = new ArrayList<UserInfo>();
        list = findBy("UID", true, Restrictions.sqlRestriction(sql));//System.out.print(list);
        return list.get(list.size() - 1);
    }
}
