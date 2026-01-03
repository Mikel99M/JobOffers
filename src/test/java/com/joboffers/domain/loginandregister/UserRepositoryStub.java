package com.joboffers.domain.loginandregister;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class UserRepositoryStub implements UserRepository {

    private final Map<String, User> database = new HashMap<>();
    private long idSequence = 1L;

    @Override
    public boolean existsByEmail(String email) {
        return database.containsKey(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(database.get(email));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idSequence++);
        }
        database.put(user.getEmail(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        database.remove(user.getEmail());
    }

    @Override
    public void deleteAllById(final Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(final Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends User> S insert(final S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> insert(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends User> Optional<S> findOne(final Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(final Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends User> List<S> findAll(final Example<S> example, final Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(final Example<S> example, final Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(final Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(final Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(final Example<S> example, final Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAll(final Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<User> findById(final Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(final Long aLong) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<User> findAllById(final Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(final Long aLong) {

    }

    @Override
    public List<User> findAll(final Sort sort) {
        return List.of();
    }

    @Override
    public Page<User> findAll(final Pageable pageable) {
        return null;
    }
}

