package com.kintiger.mall.favorite.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.kintiger.mall.api.favorite.IFavoriteService;
import com.kintiger.mall.api.favorite.bo.Favorite;
import com.kintiger.mall.api.tree.bo.Tree4Ajax;
import com.kintiger.mall.api.user.bo.User;
import com.kintiger.mall.framework.action.BaseAction;
import com.kintiger.mall.framework.bo.BooleanResult;
import com.kintiger.mall.framework.webwork.annotations.JsonResult;

/**
 * 收藏夹.
 * 
 * @author xujiakun
 * 
 */
public class FavoriteAction extends BaseAction {

	private static final long serialVersionUID = -8402045612008147896L;

	private IFavoriteService favoriteService;

	private String node;

	private String id;

	private String pid;

	private String name;

	private String url;

	private String result;

	private List<Tree4Ajax> treeList = new ArrayList<Tree4Ajax>();

	private List<Favorite> favoriteList;

	@JsonResult(field = "treeList", include = { "id", "text", "href", "hrefTarget", "leaf" })
	public String getFavoriteJsonList() {
		if (StringUtils.isBlank(node)) {
			return JSON_RESULT;
		}

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		List<Favorite> favorites = favoriteService.getFavoriteList(user.getUserId(), node);

		if (favorites == null || favorites.size() == 0) {
			return JSON_RESULT;
		}

		for (Favorite f : favorites) {
			Tree4Ajax tree = new Tree4Ajax();
			tree.setId(f.getId());
			tree.setText(f.getName());
			if (StringUtils.isNotEmpty(f.getUrl())) {
				// 拼接href地址
				tree.setHref("/" + env.getProperty("appName") + f.getUrl());
			}
			tree.setHrefTarget(f.getTarget());
			if ("NA".equals(tree.getHrefTarget())) {
				tree.setHrefTarget("mainLeft");
				tree.setLeaf(Boolean.FALSE);
			} else {
				tree.setLeaf(Boolean.TRUE);
			}
			// tree.setCls("folder")
			treeList.add(tree);
		}

		return JSON_RESULT;
	}

	@JsonResult(field = "result")
	public String createFavorite() {
		if (StringUtils.isBlank(pid) || StringUtils.isBlank(name)) {
			return JSON_RESULT;
		}

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		Favorite favorite = new Favorite();
		favorite.setPid(pid.trim());
		favorite.setName(name.trim());
		favorite.setUserId(user.getUserId());
		favorite.setModifyUser(user.getUserId());

		if (StringUtils.isBlank(url)) {
			favorite.setTarget("NA");
		}

		BooleanResult res = favoriteService.createFavorite(favorite);
		if (res.getResult()) {
			StringBuilder s = new StringBuilder();
			s.append("{id:'");
			s.append(res.getCode());
			s.append("',text:'");
			s.append(favorite.getName());
			s.append("',href:'");
			s.append(favorite.getUrl());
			s.append("',hrefTarget:'");
			s.append(favorite.getTarget());
			s.append("'}");

			result = s.toString();
		}

		return JSON_RESULT;
	}

	/**
	 * 修改收藏夹名称.
	 * 
	 * @return
	 */
	@JsonResult(field = "result")
	public String updateFavorite() {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(name)) {
			return JSON_RESULT;
		}

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		Favorite favorite = new Favorite();
		favorite.setId(id.trim());
		favorite.setName(name.trim());
		favorite.setUserId(user.getUserId());
		favorite.setModifyUser(user.getUserId());

		BooleanResult res = favoriteService.updateFavorite(favorite);
		if (res.getResult()) {
			result = "Y";
		}

		return JSON_RESULT;
	}

	/**
	 * 删除收藏夹.
	 * 
	 * @return
	 */
	@JsonResult(field = "result")
	public String deleteFavorite() {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(pid)) {
			return JSON_RESULT;
		}

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		Favorite f = new Favorite();
		f.setId(id.trim());
		f.setPid(pid.trim());
		f.setUserId(user.getUserId());

		BooleanResult res = favoriteService.deleteFavorite(f);
		if (res.getResult()) {
			result = "Y";
		}

		return JSON_RESULT;
	}

	/**
	 * 调整收藏夹层级.
	 * 
	 * @return
	 */
	public String adjustFavoriteLevel() {
		if (favoriteList == null || favoriteList.size() == 0) {
			this.setFailMessage("请先单击选中收藏夹节点，拖动完成收藏夹层级调整！");
			return RESULT_MESSAGE;
		}

		User u = this.getUser();
		if (u == null) {
			this.setFailMessage(IFavoriteService.ERROR_MESSAGE);
			return RESULT_MESSAGE;
		}

		BooleanResult r = favoriteService.adjustFavoriteLevel(favoriteList, u.getUserId());
		if (r.getResult()) {
			this.setSuccessMessage("收藏夹层级调整成功！");
		} else {
			this.setFailMessage(r.getCode());
		}

		return RESULT_MESSAGE;
	}

	/**
	 * 添加菜单树到收藏夹.
	 * 
	 * @return
	 */
	@JsonResult(field = "result")
	public String addFavorite() {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(pid)) {
			return JSON_RESULT;
		}

		User user = this.getUser();
		if (user == null) {
			return JSON_RESULT;
		}

		Favorite favorite = new Favorite();
		favorite.setMenuId(id.trim());
		favorite.setPid(pid.trim());
		favorite.setUserId(user.getUserId());
		favorite.setModifyUser(user.getUserId());

		BooleanResult res = favoriteService.addFavorite(favorite);
		if (res.getResult()) {
			StringBuilder s = new StringBuilder();
			s.append("{id:'");
			s.append(res.getCode());
			s.append("'}");

			result = s.toString();
		}

		return JSON_RESULT;
	}

	public IFavoriteService getFavoriteService() {
		return favoriteService;
	}

	public void setFavoriteService(IFavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<Tree4Ajax> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<Tree4Ajax> treeList) {
		this.treeList = treeList;
	}

	public List<Favorite> getFavoriteList() {
		return favoriteList;
	}

	public void setFavoriteList(List<Favorite> favoriteList) {
		this.favoriteList = favoriteList;
	}

}
