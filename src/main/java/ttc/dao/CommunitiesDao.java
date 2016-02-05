package ttc.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import ttc.util.MySqlConnectionManager;
import ttc.bean.Bean;
import ttc.bean.CommunityBean;
import ttc.exception.IntegrationException;

public class CommunitiesDao implements AbstractDao{

    public Bean read(Map map)throws IntegrationException{
        CommunityBean cb=new CommunityBean();
        PreparedStatement pst = null;
        try{
            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();
            StringBuffer sql=new StringBuffer();
            sql.append("select community_id,community_name,community_profile,community_icon_path,community_header_path,");
            sql.append("community_delete_flag,communities.fk_user_id from communities ");

            if(map.containsKey("where")){
                sql.append((String)map.get("where"));
            }

            String ssql=new String(sql);

            pst = cn.prepareStatement(new String(ssql));

            if(map.containsKey("commId")){
                pst.setString(1,(String)map.get("commId"));
            }

            ResultSet rs = pst.executeQuery();

            rs.next();
			cb.setName(rs.getString("community_name"));
            System.out.println("comName="+cb.getName());
            cb.setProfile(rs.getString("community_profile"));
            cb.setHeaderPath(rs.getString("community_header_path"));
            cb.setIconPath(rs.getString("community_icon_path"));
            cb.setDeleteFlag(rs.getString("community_delete_flag"));
            cb.setCreateUserId(rs.getString(7));
        }catch(SQLException e){
            throw new IntegrationException(e.getMessage(),e);
        }finally{
            try{
                if(pst!=null){
                    pst.close();
                }
            }catch(SQLException e){
                throw new IntegrationException(e.getMessage(),e);
            }
        }
        return cb;

    }

    public int update(Map map)throws IntegrationException{
        PreparedStatement pst = null;
        int result = 0;
        try{
            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("update communities set ");
            sql.append("community_name=?,community_profile=?,");
            sql.append("community_icon_path=?,community_header_path=?,");
            sql.append("community_delete_flag=? where community_id=? ");
            String upSql=new String(sql);
            System.out.println(upSql);
            pst = cn.prepareStatement(new String(upSql));

            pst.setString(1,(String)map.get("communityName"));
            pst.setString(2,(String)map.get("communityProfile"));
            pst.setString(3,(String)map.get("iconPath"));
            pst.setString(4,(String)map.get("headerPath"));
            pst.setString(5,(String)map.get("deleteFlag"));
            pst.setString(6,(String)map.get("commId"));



            result = pst.executeUpdate();
        }catch(SQLException e){
            MySqlConnectionManager.getInstance().rollback();
            throw new IntegrationException(e.getMessage(),e);
        }finally{
            try{
                if(pst!=null){
                    pst.close();
                }
            }catch(SQLException e){
                throw new IntegrationException(e.getMessage(),e);
            }
        }

        return result;
    }

    public int insert(Map map)throws IntegrationException{
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        int result = 0;
        try{
            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append("insert into communities(");
            sql.append("community_name,community_profile,");
            sql.append("community_icon_path,community_header_path,");
            sql.append("community_created_date,fk_user_id,");
            sql.append("community_delete_flag) ");
            sql.append("values(?,?,?,?,sysdate(),?,'0')");

            pst = cn.prepareStatement(new String(sql));

            pst.setString(1,(String)map.get("commName"));
            pst.setString(2,(String)map.get("commProfile"));

            if(map.containsKey("commIcon")){
                pst.setString(3,(String)map.get("commIcon"));
            }else{
                // デフォルト画像のパス
                // プロパティファイルを使って書き換えられるようにするのもありかも
                pst.setString(3,"/images/icon/icon.jpg");
            }

            if(map.containsKey("commHeader")){
                pst.setString(4,(String)map.get("commHeader"));
            }else{
                pst.setString(4,"/images/header/header.jpg");
            }


            pst.setString(5,(String)map.get("userId"));


            result = pst.executeUpdate();


            StringBuffer selectSql=new StringBuffer();
            selectSql.append("select community_id from communities ");
            selectSql.append("where fk_user_id = ? order by community_id desc");
            pst1 = cn.prepareStatement(new String(selectSql));
            pst1.setString(1,(String)map.get("userId"));
            ResultSet rs = pst1.executeQuery();
            CommunityBean cb=new CommunityBean();
            rs.next();
			cb.setId(rs.getString("community_id"));
            System.out.println("コミュid"+cb.getId());

            StringBuffer insMemSql=new StringBuffer();
            insMemSql.append("insert into community_members_list(fk_user_id,fk_community_id,");
            insMemSql.append("community_admin_flag,community_withdrawal_flag) ");
            insMemSql.append("values(?,?,'1','0')");

            pst2 = cn.prepareStatement(new String(insMemSql));
            pst2.setString(1,(String)map.get("userId"));
            pst2.setString(2,cb.getId());


            result=pst2.executeUpdate();
            System.out.println("メンバーリストにインサート");

        }catch(SQLException e){
            MySqlConnectionManager.getInstance().rollback();
            throw new IntegrationException(e.getMessage(),e);
        }finally{
            try{
                if(pst!=null){
                    pst.close();
                }
            }catch(SQLException e){
                throw new IntegrationException(e.getMessage(),e);
            }
        }

        return result;

    }

    public List readAll(Map map)throws IntegrationException{
        List result = new ArrayList();
        PreparedStatement pst = null;

        try{
            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();

            StringBuffer sql=new StringBuffer();
            sql.append("select communities.community_id,communities.community_name,");
            sql.append("communities.community_profile,count(community_members_list.fk_user_id),communities.fk_user_id from ");
            sql.append("communities left outer join community_members_list ");
            sql.append("on communities.community_id=community_members_list.fk_community_id ");

            boolean flag = map.containsKey("where");

            if(flag){
                sql.append((String)map.get("where"));
            }

            sql.append(" group by communities.community_id");

			if(map.containsKey("sort")){
				sql.append((String)map.get("sort"));
			}
			
            pst = cn.prepareStatement(new String(sql));

            if(map.containsKey("value")){
                pst.setString(1,(String)map.get("value"));
            }


            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                CommunityBean cb = new CommunityBean();
                cb.setId(rs.getString(1));
                cb.setName(rs.getString(2));
                cb.setProfile(rs.getString(3));
                cb.setCountMember(rs.getInt(4));
                cb.setCreateUserId(rs.getString(5));

                result.add(cb);
            }

        }catch(SQLException e){
            throw new IntegrationException(e.getMessage(),e);
        }finally{
            try{
                if(pst!=null){
                    pst.close();
                }
            }catch(SQLException e){
                throw new IntegrationException(e.getMessage(),e);
            }
        }

        return result;
    }

}
