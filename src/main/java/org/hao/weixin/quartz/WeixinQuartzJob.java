package org.hao.weixin.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class WeixinQuartzJob extends QuartzJobBean {
	private RefreshAccessTokenTask refreshAccessTokenTask;
	
	public void setRefreshAccessTokenTask(
			RefreshAccessTokenTask refreshAccessTokenTask) {
		this.refreshAccessTokenTask = refreshAccessTokenTask;
	}

	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		refreshAccessTokenTask.refreshToken();
	}

}
