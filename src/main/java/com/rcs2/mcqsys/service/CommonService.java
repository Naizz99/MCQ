package com.rcs2.mcqsys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
@Transactional
public class CommonService {
	
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommonService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Map<String, Object>> executeNativeQuery(String sqlQuery) {
        return jdbcTemplate.queryForList(sqlQuery);
    }
        
}
