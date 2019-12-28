package com.hetengjiao.executor.parameter;

import java.sql.PreparedStatement;

public interface ParameterHandler {
	Object getParameterObject();

	void setParameters(PreparedStatement ps)
			throws Exception;
}
