package com.kintiger.mall.points.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kintiger.mall.api.points.bo.PointsCard;
import com.kintiger.mall.framework.dao.impl.BaseDaoImpl;
import com.kintiger.mall.framework.util.NoUtil;
import com.kintiger.mall.points.dao.IPointsCardDao;

/**
 * 积分卡.
 * 
 * @author xujiakun
 * 
 */
public class PointsCardDaoImpl extends BaseDaoImpl implements IPointsCardDao {

	@Override
	public int getPointsCardCount(PointsCard pointsCard) {
		return (Integer) getSqlMapClientTemplate().queryForObject("points.card.getPointsCardCount", pointsCard);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PointsCard> getPointsCardList(PointsCard pointsCard) {
		return (List<PointsCard>) getSqlMapClientTemplate().queryForList("points.card.getPointsCardList", pointsCard);
	}

	@Override
	public int createPointsCard(final PointsCard pointsCard, final int quantity, final String modifyUser) {
		return (Integer) getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				int count = 0;
				executor.startBatch();

				pointsCard.setModifyUser(modifyUser);

				Random random = new Random();
				
				int minNo = 10000000;
				int maxNo = 99999999;

				int min = 100000;
				int max = 999999;

				for (int i = 0; i < quantity; i++) {
					pointsCard.setCardNo(NoUtil.generate().substring(8)
						+ (random.nextInt(maxNo) % (maxNo - minNo + 1) + minNo));
					pointsCard.setPassword(String.valueOf(random.nextInt(max) % (max - min + 1) + min));
					executor.insert("points.card.createPointsCard", pointsCard);
					count++;
				}

				executor.executeBatch();

				return count;
			}
		});
	}

	@Override
	public PointsCard getPointsCard(PointsCard pointsCard) {
		return (PointsCard) getSqlMapClientTemplate().queryForObject("points.card.getPointsCard", pointsCard);
	}

	@Override
	public int updatePointsCard(String statementName, PointsCard pointsCard) {
		return getSqlMapClientTemplate().update(statementName, pointsCard);
	}

}
