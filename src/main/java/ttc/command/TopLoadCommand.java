/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttc.command;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import ttc.context.RequestContext;
import ttc.context.ResponseContext;

import ttc.util.MySqlConnectionManager;

import ttc.exception.BusinessLogicException;
import ttc.exception.IntegrationException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import ttc.util.factory.AbstractDaoFactory;
import ttc.dao.AbstractDao;
/**
 *
 * @author Masaki
 */
public class TopLoadCommand extends AbstractCommand{
	public ResponseContext execute(ResponseContext resc)throws BusinessLogicException{
        try{
            RequestContext reqc = getRequestContext();

			Map result = new HashMap();
			
			// ブログが解説しているかどうかのチェック
			MySqlConnectionManager.getInstance().beginTransaction();

			AbstractDaoFactory factory = AbstractDaoFactory.getFactory("sort");
			AbstractDao dao = factory.getAbstractDao();
			Map param1 = new HashMap();
			param1.put("sortType","0");
			List articles = dao.readAll(param1);
			
			if(articles.size() <= 5){
				result.put("article", articles);
			}else{
				List nArticles = new ArrayList();
				for(int i = 0;i < 5;i++){
					nArticles.add(articles.get(i));
				}
				result.put("article",nArticles);
			}
			
			factory = AbstractDaoFactory.getFactory("blog");
			dao = factory.getAbstractDao();
			List blogs = dao.readAll(new HashMap());
			
			if(blogs.size() <= 5){
				result.put("blog", blogs);
			}else{
				List nBlogs = new ArrayList();
				for(int i = 0;i < 5;i++){
					nBlogs.add(blogs.get(i));
				}
				
				result.put("blog",nBlogs);
			}
			
			factory = AbstractDaoFactory.getFactory("community");
			dao = factory.getAbstractDao();
			
			Map param2 = new HashMap();
			param2.put("sort", " order by communities.community_created_date desc ");
			List communities = dao.readAll(param2);
			
			if(communities.size() <= 5){
				result.put("community",communities);
			}else{
				List nCommunities = new ArrayList();
				for(int i = 0;i < 5;i++){
					nCommunities.add(communities.get(i));
				}
				
				result.put("community",nCommunities);
			}
			
			MySqlConnectionManager.getInstance().commit();
            MySqlConnectionManager.getInstance().closeConnection();


			resc.setResult(result);
            resc.setTarget("top");

            return resc;

        }catch(IntegrationException e){
            throw new BusinessLogicException(e.getMessage(), e);
        }
    }
}
