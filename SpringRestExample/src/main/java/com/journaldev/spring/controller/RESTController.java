package com.journaldev.spring.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class RESTController<T, ID extends Serializable> {

	private Logger logger = LoggerFactory.getLogger(RESTController.class);

	private CrudRepository<T, ID> repo;

	public RESTController(CrudRepository<T, ID> repo) {
		this.repo = repo;
	}

	@RequestMapping
	public @ResponseBody
	List<T> listAll() {
		Iterable<T> all = this.repo.findAll();
		return Lists.newArrayList(all);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> create(@RequestBody T json) {
		logger.debug("create() with body {} of type {}", json, json.getClass());

		T created = this.repo.save(json);

		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		m.put("created", created);
		return m;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	T get(@PathVariable ID id) {
		return this.repo.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> update(@PathVariable ID id, @RequestBody T json) {
		logger.debug("update() of id#{} with body {}", id, json);
		logger.debug("T json is of type {}", json.getClass());

		T entity = this.repo.findOne(id);
		try {
			BeanUtils.copyProperties(entity, json);
		} catch (Exception e) {
			logger.warn("while copying properties", e);
			throw Throwables.propagate(e);
		}

		logger.debug("merged entity: {}", entity);

		T updated = this.repo.save(entity);
		logger.debug("updated enitity: {}", updated);

		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		m.put("id", id);
		m.put("updated", updated);
		return m;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	Map<String, Object> delete(@PathVariable ID id) {
		this.repo.delete(id);
		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		return m;
	}
}