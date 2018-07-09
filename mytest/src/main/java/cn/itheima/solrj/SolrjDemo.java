/**   
* @Title: SolrjDemo.java 
* @Package cn.itheima.solrj 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-7-3 下午12:23:37 
* @version V1.0   
*/
package cn.itheima.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/** 
 * @ClassName: SolrjDemo 
 * @Description: solrj案例程序
 * @author 传智 小杨老师  
 * @date 2018-7-3 下午12:23:37 
 *  
 */
public class SolrjDemo {
	
	/**
	 * 添加（更新）索引
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void addOrUpdateIndex() throws SolrServerException, IOException{
		
//		1.建立HttpSolrServer对象，连接solr服务
		/**
		 * baseURL：solr服务地址
		 */
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:8082/solr/");
		
//		2.建立文档对象（SolrInputDocument）
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "9528");
		//doc.addField("name", "solr is a good things");
		
		// 测试更新
		doc.addField("name", "solr and lucene both are  good things");
		
//		3.使用HttpSolrServer对象，执行添加（更新）
		server.add(doc);
		
//		4.提交
		server.commit();
		
	}
	
	/**
	 * 根据Id删除索引
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void deleteIndexById() throws SolrServerException, IOException{
//		1.建立HttpSolrServer对象，连接solr服务
		/**
		 * baseURL：solr服务地址
		 */
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:8082/solr/");
		
//		2.使用HttpSolrServer对象，执行删除
		server.deleteById("9528");
		
//		3.提交
		server.commit();
	}
	
	/**
	 * 根据条件删除索引
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void deleteIndexByQuery() throws SolrServerException, IOException{
//		1.建立HttpSolrServer对象，连接solr服务
		/**
		 * baseURL：solr服务地址
		 */
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:8082/solr/");
		
//		2.使用HttpSolrServer对象，执行删除
		server.deleteByQuery("name:solr");
		
//		3.提交
		server.commit();
	}
	
	
	/**
	 * 查询索引
	 * @throws SolrServerException 
	 */
	@Test
	public void queryIndex() throws SolrServerException{
//		1.建立HttpSolrServer对象，连接solr服务
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:8082/solr/");
		
//		2.建立查询对象（SolrQuery）
		SolrQuery sq = new SolrQuery("*:*");
		
//		3.使用HttpSolrServer对象，执行搜索，返回搜索结果响应（QueryResponse）
		QueryResponse queryResponse = server.query(sq);
		
//		4.从QueryResponse中，获取查询的结果集（SolrDocumentList）
		SolrDocumentList results = queryResponse.getResults();
		
//		5.处理结果集
		// 5.1.实际搜索到的数量
		System.out.println("实际搜索到的数量："+results.getNumFound());
		
		// 5.2.打印结果
		for(SolrDocument doc:results){
			System.out.println("-----------华丽丽的分割线----------");
			System.out.println("id域值："+doc.get("id"));
			System.out.println("name域值："+doc.get("name"));
		}
		
	}
	
	
	/**
	 * =====================第三天课程内容：solrj高级查询============================
	 * @throws SolrServerException 
	 */
	@Test
	public void seniorQuery() throws SolrServerException{
//		1.建立HttpSolrServer对象，连接solr服务
		HttpSolrServer server = new HttpSolrServer("http://127.0.0.1:8082/solr/");
		
//		2.建立查询对象（SolrQuery）
		SolrQuery sq = new SolrQuery();
		// 设置条件
//		查询表达式q
		sq.setQuery("花儿朵朵");
		
//		过滤条件fq
		sq.setFilterQueries("product_price:[ * TO 20]");
		
//		排序 sort
		sq.setSort("product_price", ORDER.asc);
		
//		分页显示start rows
		sq.setStart(0);
		sq.setRows(10);
		
//		显示域列表fl
		// id,product_name,product_price,product_catalog
		sq.setFields("id","product_name","product_price","product_catalog");
		
//		默认搜索域df
		sq.set("df", "product_name");
		
//		响应格式wt
		sq.set("wt", "json");
		
//		高亮显示hl
		sq.setHighlight(true);//开启高亮显示
		sq.addHighlightField("product_name");// 添加高亮显示的域
		sq.setHighlightSimplePre("<font color='red'>");// 设置html高亮显示标签的开始
		sq.setHighlightSimplePost("</font>");// 设置html高亮显示标签的结尾
		
//		分片统计facet
		sq.setFacet(true);// 开启分片统计
		sq.addFacetField("product_catalog_name");// 添加分片统计域
		
		
//		3.使用HttpSolrServer对象，执行搜索，返回结果响应（QueryResponse）
		QueryResponse queryResponse = server.query(sq);
		
//		4.从QueryResponse中获取搜索结果数据，SolrDocumentList
		// 4.1.获取搜索结果集
		SolrDocumentList results = queryResponse.getResults();
		
		// 4.2.获取高亮数据
		/**
		 *  "highlighting": {
			    "1": {
			      "product_name": [
			        "<font color='red'>花儿</font><font color='red'>朵朵</font>彩色金属门后挂 8钩免钉门背挂钩2066"
			      ]
			    },
		 */
		Map<String, Map<String, List<String>>> highlighting = 
				queryResponse.getHighlighting();
		
		// 4.3.获取分片统计数据
		List<FacetField> facetFields = queryResponse.getFacetFields();
		// 查看分片统计数据===========================start
		System.out.println("查看分片统计数据===========================start");
		for(FacetField f:facetFields){
			System.out.println("分片统计域："+f.getName()+",有多少个分类："+f.getValueCount());
			
			// 查看组内信息
			List<Count> values = f.getValues();
			for(Count c:values){
				System.out.println("分类名称："+c.getName()+",满足条件的数量："+c.getCount());
			}
		}

		System.out.println("查看分片统计数据===========================end");
		// 查看分片统计数据===========================end
//		5.处理结果集
		// 5.1.实际搜索到的数量
		System.out.println("实际搜索到的数量："+results.getNumFound());
		
		// 5.2.获取搜索结果数据
		for(SolrDocument doc:results){
			System.out.println("----------------华丽丽分割线------------------");
			// id,product_name,product_price,product_catalog
			// 商品编号
			String pid = doc.get("id").toString();
			
			// 商品名称
			String pname = "";
			List<String> list = highlighting.get(pid).get("product_name");
			if(list != null &&
					list.size()>0){
				pname = list.get(0);
			}else{
				pname = doc.get("product_name").toString();
			}
			
			// 商品价格
			String pprice = doc.get("product_price").toString();
			
			// 商品分类Id
			String pcatalog = doc.get("product_catalog").toString();
			
			System.out.println("商品Id："+pid);
			System.out.println("商品名称："+pname);
			System.out.println("商品价格："+pprice);
			System.out.println("商品分类Id："+pcatalog);
			
		}
	}
	
	
	
	
	
	
	
	
	
	

}
