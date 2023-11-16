package com.cs.queuedvthreads.service;

import com.cs.queuedvthreads.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BaseService<T> {
    private final BaseRepository<T> repository;

    public List<T> findAll() {
        return repository.findAll();
    }

    public void save(T entity) {
        repository.save(entity);
        log.info("{} is saved successfully: {}", entity.getClass().getSimpleName(), entity);
    }
}
