package ttc.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ttc.bean.BlogBean;
import ttc.bean.Bean;

import ttc.exception.IntegrationException;

import ttc.util.MySqlConnectionManager;

public class BlogDao implements AbstractDao{
    public int insert(Map map)throws IntegrationException{
        return 0;
    }

    public int update(Map map)throws IntegrationException{
        PreparedStatement pst = null;
        int result = 0;
		BlogBean bb = null;

        try{
			if(map.containsKey("blogbean")){
				bb = (BlogBean)map.get("blogbean");
			}

            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();
            String sql = "update users set blog_title=?,blog_header_path=?,blog_explanation=?,blog_status_flag=? where user_id=?";
            pst = cn.prepareStatement(sql);

            if(map.containsKey("title")){
				pst.setString(1,(String)map.get("title"));
			}else{
				pst.setString(1,bb.getTitle());
			}

			if(map.containsKey("headerPath")){
				pst.setString(2,(String)map.get("headerPath"));
			}else{
				pst.setString(2,bb.getHeaderPath());
			}

            if(map.containsKey("explanation")){
				pst.setString(3,(String)map.get("explanation"));
			}else{
				pst.setString(3,bb.getExplanation());
			}

			if(map.containsKey("status")){
				pst.setString(4,(String)map.get("status"));
			}else{
				pst.setString(4,"0");
			}
            pst.setString(5,(String)map.get("userId"));

            result =  pst.executeUpdate();

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
            StringBuffer sql = new StringBuffer();
			
			sql.append("select blog_title,");
			sql.append("blog_header_path,blog_explanation ");
			sql.append("from users where blog_status_flag = 1");
			
			if(map.containsKey("sort")){
				sql.append((String)map.get("sort"));
			}
			
			
            pst = cn.prepareStatement(new String(sql));

            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                BlogBean blog = new BlogBean();
                blog.setTitle(rs.getString(1));
                blog.setHeaderPath(rs.getString(2));
                blog.setExplanation(rs.getString(3));

                result.add(blog);
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

    public Bean read(Map map)throws IntegrationException{
        BlogBean blog = null;
        PreparedStatement pst = null;
        try{
            Connection cn = null;
            cn = MySqlConnectionManager.getInstance().getConnection();
            String sql = "select blog_title,blog_header_path,blog_explanation,blog_status_flag from users where user_id=?";

            pst = cn.prepareStatement(sql);

            pst.setInt(1,Integer.parseInt((String)map.get("userId")));

            ResultSet rs = pst.executeQuery();

            rs.next();

            blog = new BlogBean();
            blog.setTitle(rs.getString(1));
            blog.setHeaderPath(rs.getString(2));
            blog.setExplanation(rs.getString(3));
			blog.setStatus(rs.getString(4));


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

        return blog;
    }
}
